package org.feature.fox.coffee_counter.ui.transaction

import android.graphics.Bitmap
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.text.input.TextFieldValue
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
import org.feature.fox.coffee_counter.data.models.body.SendMoneyBody
import org.feature.fox.coffee_counter.data.models.response.TransactionResponse
import org.feature.fox.coffee_counter.data.repository.UserRepository
import org.feature.fox.coffee_counter.di.services.AppPreference
import org.feature.fox.coffee_counter.util.IToast
import org.feature.fox.coffee_counter.util.UIText
import javax.inject.Inject

/**
 * Interface for the [TransactionViewModel].
 */
interface ITransactionViewModel : IToast {
    val showMainActivity: MutableLiveData<Boolean>
    val toastMessage: MutableLiveData<String>
    val transactions: MutableList<TransactionResponse>
    val balance: MutableState<Double>
    val purchases: MutableList<Purchase>
    val balanceList: MutableList<Pair<Long, Double>>
    val qrCodeDialogVisible: MutableState<Boolean>
    val qrCodeSendState: MutableState<Boolean>
    val qrCodeReceiveState: MutableState<Boolean>
    val qrCode: MutableState<Bitmap?>
    val sendAmount: MutableState<TextFieldValue>

    suspend fun refreshTransactions()
    suspend fun getTotalBalance()
    suspend fun getPurchasesOfUser()
    suspend fun getFundingOfUser()
    suspend fun getBalanceOfUser()
    suspend fun generateQRCode()
    suspend fun sendMoney(qrCodeText: String)
}

/**
 * ViewModel for the transaction view.
 * @property userRepository The user repository.
 * @property preference The app preference.
 */
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
    override val qrCodeDialogVisible = mutableStateOf(false)
    override val qrCodeSendState = mutableStateOf(false)
    override val qrCodeReceiveState = mutableStateOf(false)
    override val qrCode = mutableStateOf<Bitmap?>(null)
    override val sendAmount = mutableStateOf(TextFieldValue())
    override var purchases = mutableListOf<Purchase>()
    override var balanceList = mutableListOf<Pair<Long, Double>>()

    /**
     * Loads initial values of transactions.
     */
    init {
        viewModelScope.launch {
            refreshTransactions()
            getTotalBalance()
            getPurchasesOfUser()
            getFundingOfUser()
            getBalanceOfUser()
            generateQRCode()
        }
    }

    /**
     * Refreshes the transactions.
     */
    override suspend fun refreshTransactions() {
        val response = userRepository.getTransactions(preference.getTag(BuildConfig.USER_ID))

        if (response.data == null) {
            toastChannel.send(response.message?.let { UIText.DynamicString(it) }
                ?: UIText.StringResource(R.string.unknown_error))
            return
        }
        transactions = response.data as MutableList<TransactionResponse>
    }

    /**
     * Generates a QR code.
     * reference: https://stackoverflow.com/questions/28232116/android-using-zxing-generate-qr-code
     */
    override suspend fun generateQRCode() {
        val writer = QRCodeWriter()
        val qrCodeSize = 300
        val bitMatrix = writer.encode(
            preference.getTag(BuildConfig.USER_ID),
            BarcodeFormat.QR_CODE,
            qrCodeSize,
            qrCodeSize
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

    /**
     * Sends money to the given QR code.
     * @param qrCodeText The recipient's QR code.
     */
    override suspend fun sendMoney(qrCodeText: String) {
        sendAmount.value = TextFieldValue(sendAmount.value.text.replace(",", "."))
        if (sendAmount.value.text.count { '.' == it } > 1) {
            toastChannel.send(UIText.StringResource(R.string.incorrect_money_format))
            return
        }
        val sendMoneyAmount = sendAmount.value.text.toDouble()
        val response = userRepository.sendMoney(
            preference.getTag(BuildConfig.USER_ID),
            SendMoneyBody(sendMoneyAmount, qrCodeText)
        )
        if (response.data == null) {
            toastChannel.send(response.message?.let { UIText.DynamicString(it) }
                ?: UIText.StringResource(R.string.unknown_error))
            return
        }
        toastChannel.send(UIText.StringResource(R.string.money_sent_success))
        qrCodeDialogVisible.value = false
        qrCodeSendState.value = false
        qrCodeReceiveState.value = false
    }

    /**
     * Gets the total balance of the user.
     * FIXME: Maybe use "observeTotalBalance" instead of calling this Method after each change
     */
    override suspend fun getTotalBalance() {
        val response = userRepository.getUserById(preference.getTag(BuildConfig.USER_ID))

        if (response.data == null) {
            toastChannel.send(response.message?.let { UIText.DynamicString(it) }
                ?: UIText.StringResource(R.string.unknown_error))
            return
        }
        balance.value = response.data.balance
    }

    /**
     * Gets the purchases of the user.
     */
    override suspend fun getPurchasesOfUser() {
        purchases = userRepository.getPurchaseListOfUserDb(
            preference.getTag(BuildConfig.USER_ID)
        ) as MutableList<Purchase>
    }


    /**
     * Gets the funding of the user.
     */
    override suspend fun getFundingOfUser() {
        val response = userRepository.getTransactions(preference.getTag(BuildConfig.USER_ID))
        if (response.data == null) {
            toastChannel.send(response.message?.let { UIText.DynamicString(it) }
                ?: UIText.StringResource(R.string.unknown_error))
            return
        }

        val fundingList = mutableListOf<Funding>()
        response.data.toMutableList().forEach { transactionResponse ->
            if (transactionResponse.type == "funding")
                fundingList.add(
                    Funding(
                        transactionResponse.timestamp,
                        preference.getTag(BuildConfig.USER_ID),
                        transactionResponse.value
                    )
                )
        }

        if (userRepository.getFundingOfUserByIdDb(preference.getTag(BuildConfig.USER_ID)).size != fundingList.size) {
            fundingList.forEach { funding ->
                userRepository.insertFundingDb(funding)
            }
        }
    }

    /**
     * Gets the balance of the user.
     */
    override suspend fun getBalanceOfUser() {
        balanceList.clear()
        if (transactions.size == 0) {
            balanceList.add(Pair(System.currentTimeMillis() / 1000, 0.0))
        } else {
            balanceList.add(Pair(transactions[0].timestamp, transactions[0].value))
        }
        for (i in 1 until transactions.size) {
            balanceList.add(
                Pair(
                    transactions[i].timestamp,
                    balanceList[i - 1].second + transactions[i - 1].value
                )
            )
        }
    }
}

/**
 * Preview for the transaction screen.
 */
class TransactionViewModelPreview : ITransactionViewModel {
    override val showMainActivity = MutableLiveData<Boolean>()
    override val toastMessage = MutableLiveData<String>()
    override val transactions: MutableList<TransactionResponse>
        get() = TODO("Not yet implemented")
    override val purchases: MutableList<Purchase>
        get() = TODO("Not yet implemented")
    override val balance = mutableStateOf(13.0)
    override val balanceList: MutableList<Pair<Long, Double>>
        get() = TODO("Not yet implemented")
    override val toastChannel = Channel<UIText>()
    override val toast = toastChannel.receiveAsFlow()
    override val qrCodeDialogVisible = mutableStateOf(true)
    override val qrCodeSendState = mutableStateOf(true)
    override val qrCodeReceiveState = mutableStateOf(false)
    override val qrCode = mutableStateOf<Bitmap?>(null)
    override val sendAmount = mutableStateOf(TextFieldValue())

    override suspend fun refreshTransactions() {}
    override suspend fun getTotalBalance() {}
    override suspend fun getPurchasesOfUser() {}
    override suspend fun getFundingOfUser() {}
    override suspend fun getBalanceOfUser() {}
    override suspend fun generateQRCode() {
        TODO("Not yet implemented")
    }

    override suspend fun sendMoney(qrCodeText: String) {
        TODO("Not yet implemented")
    }
}
