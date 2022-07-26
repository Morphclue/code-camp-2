package org.feature.fox.coffee_counter.ui

import android.app.PendingIntent
import android.app.PendingIntent.FLAG_IMMUTABLE
import android.content.Intent
import android.content.IntentFilter
import android.nfc.FormatException
import android.nfc.NdefMessage
import android.nfc.NfcAdapter
import android.nfc.Tag
import android.nfc.TagLostException
import android.nfc.tech.Ndef
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.material.Scaffold
import androidx.navigation.compose.rememberNavController
import dagger.hilt.android.AndroidEntryPoint
import org.feature.fox.coffee_counter.ui.common.BottomNavBar
import org.feature.fox.coffee_counter.ui.common.Navigation
import org.feature.fox.coffee_counter.ui.items.ItemsViewModel
import org.feature.fox.coffee_counter.ui.profile.ProfileViewModel
import org.feature.fox.coffee_counter.ui.theme.CoffeeCounterTheme
import org.feature.fox.coffee_counter.ui.transaction.TransactionViewModel
import org.feature.fox.coffee_counter.ui.user.UserListViewModel
import java.io.IOException

@AndroidEntryPoint
class CoreActivity : ComponentActivity(), NfcAdapter.ReaderCallback{
    private val itemsViewModel: ItemsViewModel by viewModels()
    private val profileViewModel: ProfileViewModel by viewModels()
    private val transactionsViewModel: TransactionViewModel by viewModels()
    private val userListViewModel: UserListViewModel by viewModels()
    private var mNfcAdapter: NfcAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            CoffeeCounterTheme {
                val navController = rememberNavController()
                Scaffold(
                    bottomBar = { BottomNavBar(navController = navController) }
                ) {
                    Navigation(
                        navController = navController,
                        profileViewModel = profileViewModel,
                        transactionsViewModel = transactionsViewModel,
                        userListViewModel = userListViewModel,
                        itemsViewModel = itemsViewModel,
                    )
                }
            }
        }
        mNfcAdapter = NfcAdapter.getDefaultAdapter(this);
    }

    override fun onResume() {
        super.onResume()
        if (mNfcAdapter != null) {
            val options = Bundle()
            // Work around for some broken Nfc firmware implementations that poll the card too fast
            options.putInt(NfcAdapter.EXTRA_READER_PRESENCE_CHECK_DELAY, 250)

            // Enable ReaderMode for all types of card and disable platform sounds
            mNfcAdapter!!.enableReaderMode(
                this,
                this,
                NfcAdapter.FLAG_READER_NFC_A or
                        NfcAdapter.FLAG_READER_NFC_B or
                        NfcAdapter.FLAG_READER_NFC_F or
                        NfcAdapter.FLAG_READER_NFC_V or
                        NfcAdapter.FLAG_READER_NFC_BARCODE or
                        NfcAdapter.FLAG_READER_NO_PLATFORM_SOUNDS,
                options
            )
        }
    }

    override fun onPause() {
        super.onPause()
        if (mNfcAdapter != null) mNfcAdapter!!.disableReaderMode(this)
    }

    private fun enableForegroundDispatch(activity: ComponentActivity, adapter: NfcAdapter?) {
        val intent = Intent(activity.applicationContext, activity.javaClass)
        intent.flags = Intent.FLAG_ACTIVITY_SINGLE_TOP
        val pendingIntent =
            PendingIntent.getActivity(activity.applicationContext, 0, intent, FLAG_IMMUTABLE)
        val filters = arrayOfNulls<IntentFilter>(1)
        val techList = arrayOf<Array<String>>()
        filters[0] = IntentFilter()
        with(filters[0]) {
            this?.addAction(NfcAdapter.ACTION_NDEF_DISCOVERED)
            this?.addCategory(Intent.CATEGORY_DEFAULT)
            try {
                this?.addDataType("text/plain")
            } catch (ex: IntentFilter.MalformedMimeTypeException) {
                throw RuntimeException()
            }
        }
        adapter?.enableForegroundDispatch(activity, pendingIntent, filters, techList)
    }

    override fun onTagDiscovered(tag: Tag) {

        var mNdef: Ndef = Ndef.get(tag);

        if (mNdef!= null) {
            try {
                Log.w("fuck", readPayload(mNdef));

            } catch (e: FormatException) {
                // if the NDEF Message to write is malformed
            } catch (e: TagLostException) {
                // Tag went out of range before operations were complete
            } catch (e: IOException) {
                // if there is an I/O failure, or the operation is cancelled
            } finally {
                // Be nice and try and close the tag to
                // Disable I/O operations to the tag from this TagTechnology object, and release resources.
                try {
                    mNdef.close();
                } catch (e: IOException) {
                    // if there is an I/O failure, or the operation is cancelled
                }
            }
        }
    }

    private fun readPayload(tag: Ndef): String{
        val message: NdefMessage = tag.cachedNdefMessage

        var payload = StringBuffer()
        val record = message.records[0]
        record.payload.forEach { byte -> payload.append(byte.toChar()) }
        return payload.toString().substring(3)
    }
}
