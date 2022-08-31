package org.feature.fox.coffee_counter.ui

import android.nfc.NdefMessage
import android.nfc.NfcAdapter
import android.nfc.Tag
import android.nfc.tech.Ndef
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.material.Scaffold
import androidx.navigation.compose.rememberNavController
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.runBlocking
import org.feature.fox.coffee_counter.ui.common.BottomNavBar
import org.feature.fox.coffee_counter.ui.common.Navigation
import org.feature.fox.coffee_counter.ui.items.ItemsViewModel
import org.feature.fox.coffee_counter.ui.profile.ProfileViewModel
import org.feature.fox.coffee_counter.ui.theme.CoffeeCounterTheme
import org.feature.fox.coffee_counter.ui.transaction.TransactionViewModel
import org.feature.fox.coffee_counter.ui.user.UserListViewModel

/**
 * The core activity of the application.
 * It contains all 4 different viewModels.
 */
@AndroidEntryPoint
class CoreActivity : ComponentActivity(), NfcAdapter.ReaderCallback {
    private val itemsViewModel: ItemsViewModel by viewModels()
    private val profileViewModel: ProfileViewModel by viewModels()
    private val transactionsViewModel: TransactionViewModel by viewModels()
    private val userListViewModel: UserListViewModel by viewModels()
    private var nfcAdapter: NfcAdapter? = null

    /**
     * Called when the activity is created.
     * Passes all viewModels to the Navigation.
     *
     * @param savedInstanceState the saved instance state.
     */
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
        nfcAdapter = NfcAdapter.getDefaultAdapter(this)
    }

    /**
     * Called when the activity is resumed.
     * Enables the reader mode for the nfc adapter.
     */
    override fun onResume() {
        super.onResume()
        if (nfcAdapter != null) {
            val options = Bundle()
            // Work around for some broken Nfc firmware implementations that poll the card too fast
            options.putInt(NfcAdapter.EXTRA_READER_PRESENCE_CHECK_DELAY, 250)

            // Enable ReaderMode for all types of card and disable platform sounds
            nfcAdapter?.enableReaderMode(
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

    /**
     * Called when the activity is paused.
     * Disables the reader mode for the nfc adapter.
     */
    override fun onPause() {
        super.onPause()
        if (nfcAdapter != null) nfcAdapter?.disableReaderMode(this)
    }

    /**
     * Called when a nfc tag is read/discovered.
     *
     * @param tag the tag that was read/discovered.
     */
    override fun onTagDiscovered(tag: Tag) {
        val nNdef: Ndef? = Ndef.get(tag)

        if (nNdef == null) {
            runOnUiThread {
                Toast.makeText(
                    applicationContext,
                    "something went wrong",
                    Toast.LENGTH_SHORT
                ).show()
            }
        } else {
            runOnUiThread {
                runBlocking {
                    itemsViewModel.addStringItemToShoppingCart(readPayload(nNdef))
                }
            }
        }
    }

    /**
     * Reads the payload of the ndef tag.
     *
     * @param tag Ndef tag that was read/discovered.
     * @return the payload of the ndef tag.
     */
    private fun readPayload(tag: Ndef): String {
        val startOfMessage = 3
        val message: NdefMessage = tag.cachedNdefMessage
        val payload = StringBuffer()
        val record = message.records[0]
        record.payload.forEach { byte -> payload.append(byte.toInt().toChar()) }
        return payload.toString().substring(startOfMessage)
    }
}
