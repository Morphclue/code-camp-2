package org.feature.fox.coffee_counter.ui.transaction

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import org.feature.fox.coffee_counter.BuildConfig
import org.feature.fox.coffee_counter.R
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
    val purchases: MutableList<Purchase>

    suspend fun refreshTransactions()
    suspend fun getTotalBalance()
    suspend fun getPurchasesOfUser()
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
    override var purchases = mutableListOf<Purchase>()

    init {
        viewModelScope.launch {
            refreshTransactions()
            getTotalBalance()
            getPurchasesOfUser()
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
    }

    //FIXME: Maybe use "observeTotalBalance" instead of calling this Method after each change
    override suspend fun getTotalBalance() {
        val response = userRepository.getUserById(preference.getTag(BuildConfig.USER_ID))

        if (response.data == null) {
            toastChannel.send(response.message?.let { UIText.DynamicString(it) }
                ?: UIText.StringResource(R.string.unknown_error))
            return
        }
        balance.value = response.data.balance
    }

    override suspend fun getPurchasesOfUser() {
        // Get Purchases from DB since new purchases only occur if the user buys items.
        // This is done inside ItemsView and updated at DB
        purchases =
            userRepository.getPurchasesOfUserByIdDb(preference.getTag(BuildConfig.USER_ID)) as MutableList<Purchase>
    }
}

class TransactionViewModelPreview : ITransactionViewModel {

    override val showMainActivity = MutableLiveData<Boolean>()
    override val toastMessage = MutableLiveData<String>()
    override val transactions: MutableList<TransactionResponse>
        get() = TODO("Not yet implemented")
    override val purchases: MutableList<Purchase>
        get() = TODO("Not yet implemented")
    override val balance = mutableStateOf(13.0)
    override val toastChannel = Channel<UIText>()
    override val toast = toastChannel.receiveAsFlow()

    override suspend fun refreshTransactions() {}

    override suspend fun getTotalBalance() {
        TODO("Not yet implemented")
    }

    override suspend fun getPurchasesOfUser() {
        TODO("Not yet implemented")
    }

}
