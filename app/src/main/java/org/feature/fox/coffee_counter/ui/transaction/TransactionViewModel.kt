package org.feature.fox.coffee_counter.ui.transaction

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
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

    suspend fun refreshTransactions()
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

    init {
        viewModelScope.launch {
            refreshTransactions()
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

}

class TransactionViewModelPreview : ITransactionViewModel {

    override val showMainActivity = MutableLiveData<Boolean>()
    override val toastMessage = MutableLiveData<String>()
    override val transactions: MutableList<TransactionResponse>
        get() = TODO("Not yet implemented")
    override val toastChannel = Channel<UIText>()
    override val toast = toastChannel.receiveAsFlow()

    override suspend fun refreshTransactions() {
        TODO("Not yet implemented")
    }

}
