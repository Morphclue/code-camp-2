package org.feature.fox.coffee_counter.ui.transaction

import android.graphics.Bitmap
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.zxing.BarcodeFormat
import com.google.zxing.qrcode.QRCodeWriter
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import org.feature.fox.coffee_counter.BuildConfig
import org.feature.fox.coffee_counter.R
import org.feature.fox.coffee_counter.data.local.database.tables.Funding
import org.feature.fox.coffee_counter.data.local.database.tables.Purchase
import org.feature.fox.coffee_counter.data.models.response.TransactionResponse
import org.feature.fox.coffee_counter.data.repository.UserRepository
import org.feature.fox.coffee_counter.di.services.AppPreference
import org.feature.fox.coffee_counter.util.IToast
import org.feature.fox.coffee_counter.util.UIText
import javax.inject.Inject

interface ITransactionViewModel : IToast {
    val showMainActivity: MutableLiveData<Boolean>
    val toastMessage: MutableLiveData<String>
    val transactions: MutableList<TransactionResponse>
    val balance: MutableState<Double>
    val qrCode: MutableState<Bitmap?>

    suspend fun refreshTransactions()
    suspend fun getTotalBalance()
    suspend fun shareQRCode()
}

@HiltViewModel
class TransactionViewModel @Inject constructor(
    private val userRepository: UserRepository,
    private val preference: AppPreference,
) : ViewModel(), ITransactionViewModel {
    override val showMainActivity = MutableLiveData<Boolean>()
    override val toastMessage = MutableLiveData<String>()
    override val toastChannel = Channel<UIText>()
    override val toast = toastChannel.receiveAsFlow()
    override var transactions = mutableListOf<TransactionResponse>()
    override var balance = mutableStateOf(0.0)
    override val qrCode = mutableStateOf<Bitmap?>(null)

    init {
        viewModelScope.launch {
            refreshTransactions()
            getTotalBalance()
        }
    }

    override suspend fun refreshTransactions() {
        val response = userRepository.getTransactions(preference.getTag(BuildConfig.USER_ID))

        if (response.data == null) {
            toastChannel.send(response.message?.let { UIText.DynamicString(it) }
                ?: UIText.StringResource(R.string.unknown_error))
            return
        }
        transactions = response.data as MutableList<TransactionResponse>
        transactions.forEach { transactionResponse ->

            when (transactionResponse.type) {
                "funding" -> userRepository.insertFunding(
                    Funding(
                        transactionResponse.timestamp,
                        preference.getTag(BuildConfig.USER_ID),
                        transactionResponse.value
                    )
                )
                "purchase" -> userRepository.insertPurchase(
                    Purchase(
                        transactionResponse.timestamp,
                        preference.getTag(BuildConfig.USER_ID),
                        transactionResponse.value,
                        transactionResponse.itemId!!,
                        transactionResponse.itemName!!,
                        transactionResponse.amount!!
                    )
                )
            }
        }
    }

    // reference: https://stackoverflow.com/questions/28232116/android-using-zxing-generate-qr-code
    override suspend fun shareQRCode() {
        val writer = QRCodeWriter()
        val bitMatrix = writer.encode(
            "https://github.com/morphclue",
            BarcodeFormat.QR_CODE,
            200,
            200
        )

        val pixels = IntArray(bitMatrix.width * bitMatrix.height)
        for (y in 0 until bitMatrix.height) {
            for (x in 0 until bitMatrix.width) {
                pixels[y * bitMatrix.width + x] = if (bitMatrix.get(x, y)) -0x1000000 else -0x1
            }
        }

        val bitmap = Bitmap.createBitmap(bitMatrix.width, bitMatrix.height, Bitmap.Config.ARGB_8888)
        bitmap.setPixels(pixels, 0, bitMatrix.width, 0, 0, bitMatrix.width, bitMatrix.height)
        qrCode.value = bitmap
    }

    override suspend fun getTotalBalance() {
        val response = userRepository.getUserById(preference.getTag(BuildConfig.USER_ID))

        if (response.data == null) {
            toastChannel.send(response.message?.let { UIText.DynamicString(it) }
                ?: UIText.StringResource(R.string.unknown_error))
            return
        }
        balance.value = response.data.balance
    }
}

class TransactionViewModelPreview : ITransactionViewModel {

    override val showMainActivity = MutableLiveData<Boolean>()
    override val toastMessage = MutableLiveData<String>()
    override val transactions: MutableList<TransactionResponse>
        get() = TODO("Not yet implemented")
    override val balance = mutableStateOf(13.0)
    override val toastChannel = Channel<UIText>()
    override val toast = toastChannel.receiveAsFlow()
    override val qrCode = mutableStateOf<Bitmap?>(null)

    override suspend fun refreshTransactions() {}

    override suspend fun getTotalBalance() {
        TODO("Not yet implemented")
    }

    override suspend fun shareQRCode() {
        TODO("Not yet implemented")
    }
}
