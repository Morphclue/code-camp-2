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
    val funding: MutableState<TextFieldValue>
    val editName: MutableState<TextFieldValue>
    val editId: MutableState<TextFieldValue>
    val editPassword: MutableState<TextFieldValue>
    val isAdminState: MutableState<Boolean>
    val editReEnterPassword: MutableState<TextFieldValue>
    var currentUser: MutableLiveData<UserIdResponse>
    val balance: MutableLiveData<Double>

    suspend fun addFunding()
    suspend fun createUser()
    suspend fun getTotalBalance()
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
    override val funding = mutableStateOf(TextFieldValue())
    override val editName = mutableStateOf(TextFieldValue())
    override val editId = mutableStateOf(TextFieldValue())
    override val editPassword = mutableStateOf(TextFieldValue())
    override val isAdminState = mutableStateOf(false)
    override val editReEnterPassword = mutableStateOf(TextFieldValue())
    override var currentUser = MutableLiveData<UserIdResponse>()
    override val toastChannel = Channel<UIText>()
    override val toast = toastChannel.receiveAsFlow()
    override var balance = MutableLiveData<Double>()

    init {
        viewModelScope.launch {
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

            toastChannel.send(response.data?.let { UIText.DynamicString(it) }
                ?: UIText.StringResource(R.string.unknown_error))

            val index = userList.indexOf(currentUser.value)
            userList.remove(currentUser.value)
            currentUser.value
            userList.add(index, UserIdResponse(
                userIdResponse.id,
                userIdResponse.name,
                userIdResponse.balance + fundingAmount
            ))
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

        toastChannel.send(UIText.StringResource(R.string.created_account))
    }

    override suspend fun getTotalBalance() {
        val response = userRepository.getUserById(preference.getTag(BuildConfig.USER_ID))

        if (response.data == null) {
            toastChannel.send(response.message?.let { UIText.DynamicString(it) }
                ?: UIText.StringResource(R.string.unknown_error))
            balance.value =
                userRepository.observeTotalBalanceOfUser(preference.getTag(BuildConfig.USER_ID)).value
            return
        }
        balance.value = response.data.balance
    }

    private suspend fun loadUsers() {
        val response = userRepository.getUsers()
        if (response.data == null) {
            return
        }

        response.data.forEach { user ->
            val idResponse = userRepository.getUserById(user.id)

            if (idResponse.data == null) {
                return@forEach
            }

            userList.add(idResponse.data)
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
    override val funding = mutableStateOf(TextFieldValue())
    override val editName = mutableStateOf(TextFieldValue())
    override val editId = mutableStateOf(TextFieldValue())
    override val editPassword = mutableStateOf(TextFieldValue())
    override val editReEnterPassword = mutableStateOf(TextFieldValue())
    override val isAdminState = mutableStateOf(false)
    override var currentUser = MutableLiveData<UserIdResponse>()
    override val balance = MutableLiveData<Double>()
    override val toastChannel = Channel<UIText>()
    override val toast = toastChannel.receiveAsFlow()

    override suspend fun addFunding() {
        TODO("Not yet implemented")
    }

    override suspend fun createUser() {
        TODO("Not yet implemented")
    }

    override suspend fun getTotalBalance() {
        TODO("Not yet implemented")
    }
}
