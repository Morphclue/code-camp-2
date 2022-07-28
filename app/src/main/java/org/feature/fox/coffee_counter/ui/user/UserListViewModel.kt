package org.feature.fox.coffee_counter.ui.user

import androidx.compose.foundation.ScrollState
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.text.input.TextFieldValue
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
import org.feature.fox.coffee_counter.data.local.database.tables.User
import org.feature.fox.coffee_counter.data.models.body.FundingBody
import org.feature.fox.coffee_counter.data.models.body.UserBody
import org.feature.fox.coffee_counter.data.models.response.UserIdResponse
import org.feature.fox.coffee_counter.data.repository.UserRepository
import org.feature.fox.coffee_counter.di.services.AppPreference
import org.feature.fox.coffee_counter.util.IToast
import org.feature.fox.coffee_counter.util.UIText
import javax.inject.Inject

interface IUserListViewModel : IToast {
    val userList: MutableList<UserIdResponse>
    val scrollState: ScrollState
    val isLoaded: MutableState<Boolean>
    val fundingDialogVisible: MutableState<Boolean>
    val editDialogVisible: MutableState<Boolean>
    val sendMoneyDialogVisible: MutableState<Boolean>
    val funding: MutableState<TextFieldValue>
    val editName: MutableState<TextFieldValue>
    val editId: MutableState<TextFieldValue>
    val editPassword: MutableState<TextFieldValue>
    val isAdminState: MutableState<Boolean>
    val editReEnterPassword: MutableState<TextFieldValue>
    var currentUser: MutableLiveData<UserIdResponse>
    val balance: MutableState<Double>
    val sendAmount: MutableState<TextFieldValue>

    suspend fun addFunding()
    suspend fun createUser()
    suspend fun getTotalBalance()
    suspend fun sendMoney()
}

@HiltViewModel
class UserListViewModel @Inject constructor(
    private val userRepository: UserRepository,
    private val preference: AppPreference,
) : ViewModel(), IUserListViewModel {
    override val userList = mutableStateListOf<UserIdResponse>()
    override val scrollState = ScrollState(0)
    override val isLoaded = mutableStateOf(false)
    override val fundingDialogVisible = mutableStateOf(false)
    override val editDialogVisible = mutableStateOf(false)
    override val sendMoneyDialogVisible = mutableStateOf(false)
    override val funding = mutableStateOf(TextFieldValue())
    override val editName = mutableStateOf(TextFieldValue())
    override val editId = mutableStateOf(TextFieldValue())
    override val editPassword = mutableStateOf(TextFieldValue())
    override val isAdminState = mutableStateOf(false)
    override val editReEnterPassword = mutableStateOf(TextFieldValue())
    override var currentUser = MutableLiveData<UserIdResponse>()
    override val toastChannel = Channel<UIText>()
    override val toast = toastChannel.receiveAsFlow()
    override val balance = mutableStateOf(0.0)
    override val sendAmount = mutableStateOf(TextFieldValue())

    init {
        viewModelScope.launch {
            isAdminState.value = preference.getTag(BuildConfig.IS_ADMIN, true)
            loadUsers()
            getTotalBalance()
        }
    }

    override suspend fun addFunding() {
        funding.value = TextFieldValue(funding.value.text.replace(",", "."))
        if (funding.value.text.count { '.' == it } > 1) {
            toastChannel.send(UIText.StringResource(R.string.incorrect_money_format))
            return
        }
        val fundingAmount = funding.value.text.toDouble()

        currentUser.value?.let { userIdResponse ->
            val response = userRepository.addFunding(userIdResponse.id, FundingBody(fundingAmount))
            val transactions = userRepository.getTransactions(userIdResponse.id)
            if (response.data == null || transactions.data == null) {
                toastChannel.send(response.message?.let { UIText.DynamicString(it) }
                    ?: UIText.StringResource(R.string.unknown_error))
                return
            }

            userRepository.insertFundingDb(
                Funding(
                    timestamp = transactions.data.last().timestamp,
                    userId = userIdResponse.id,
                    value = transactions.data.last().value
                )
            )

            val index = userList.indexOf(currentUser.value)
            userList.remove(currentUser.value)
            currentUser.value
            userList.add(
                index, UserIdResponse(
                    userIdResponse.id,
                    userIdResponse.name,
                    userIdResponse.balance + fundingAmount
                )
            )
        }

        funding.value = TextFieldValue()
        fundingDialogVisible.value = false
    }

    override suspend fun createUser() {
        if (editPassword.value.text != editReEnterPassword.value.text) {
            toastChannel.send(UIText.StringResource(R.string.match_password))
            return
        }

        val response = userRepository.adminSignUp(
            UserBody(
                editId.value.text,
                editName.value.text,
                editPassword.value.text,
                isAdminState.value
            )
        )

        if (response.data == null) {
            toastChannel.send(response.message?.let { UIText.DynamicString(it) }
                ?: UIText.StringResource(R.string.unknown_error))
            return
        }

        // Assuming that this method only creates new Users and does not update them
        userRepository.insertUserDb(
            User(
                userId = editId.value.text,
                name = editName.value.text,
                isAdmin = isAdminState.value
            )
        )

        toastChannel.send(UIText.StringResource(R.string.created_account))
        editDialogVisible.value = false
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

    override suspend fun sendMoney() {
        // TODO: implement sendMoney
    }

    private suspend fun loadUsers() {
        val response = userRepository.getUsers()
        if (response.data == null) {
            return
        }

        if (isAdminState.value) {
            response.data.forEach { user ->
                val idResponse = userRepository.getUserById(user.id)

                if (idResponse.data == null) {
                    return@forEach
                }

                userList.add(idResponse.data)
            }
        } else {
            response.data.forEach { user ->
                userList.add(UserIdResponse(
                    id = user.id,
                    name = user.name,
                    balance = 0.0,
                ))
            }

        }
        isLoaded.value = true
    }
}

class UserListViewModelPreview : IUserListViewModel {
    override val userList = mutableListOf<UserIdResponse>()
    override val scrollState = ScrollState(0)
    override val isLoaded = mutableStateOf(true)
    override val fundingDialogVisible = mutableStateOf(false)
    override val editDialogVisible = mutableStateOf(true)
    override val sendMoneyDialogVisible = mutableStateOf(false)
    override val funding = mutableStateOf(TextFieldValue())
    override val editName = mutableStateOf(TextFieldValue())
    override val editId = mutableStateOf(TextFieldValue())
    override val editPassword = mutableStateOf(TextFieldValue())
    override val editReEnterPassword = mutableStateOf(TextFieldValue())
    override val isAdminState = mutableStateOf(false)
    override var currentUser = MutableLiveData<UserIdResponse>()
    override val balance = mutableStateOf(13.0)
    override val toastChannel = Channel<UIText>()
    override val toast = toastChannel.receiveAsFlow()
    override val sendAmount = mutableStateOf(TextFieldValue())

    override suspend fun addFunding() {
        TODO("Not yet implemented")
    }

    override suspend fun createUser() {
        TODO("Not yet implemented")
    }

    override suspend fun getTotalBalance() {
        TODO("Not yet implemented")
    }

    override suspend fun sendMoney() {
        TODO("Not yet implemented")
    }
}
