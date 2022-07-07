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
import org.feature.fox.coffee_counter.R
import org.feature.fox.coffee_counter.data.models.body.FundingBody
import org.feature.fox.coffee_counter.data.models.response.UserIdResponse
import org.feature.fox.coffee_counter.data.repository.UserRepository
import org.feature.fox.coffee_counter.util.IToast
import org.feature.fox.coffee_counter.util.UIText
import javax.inject.Inject

interface IUserListViewModel : IToast {
    val userList: MutableList<UserIdResponse>
    val scrollState: ScrollState
    val isLoaded: MutableState<Boolean>
    val dialogVisible: MutableState<Boolean>
    val funding: MutableState<TextFieldValue>
    var currentUser: MutableLiveData<UserIdResponse>

    suspend fun addFunding()
    suspend fun updateUser()
}

@HiltViewModel
class UserListViewModel @Inject constructor(
    private val userRepository: UserRepository,
) : ViewModel(), IUserListViewModel {
    override val userList = mutableStateListOf<UserIdResponse>()
    override val scrollState = ScrollState(0)
    override val isLoaded = mutableStateOf(false)
    override val dialogVisible = mutableStateOf(false)
    override val funding = mutableStateOf(TextFieldValue())
    override var currentUser = MutableLiveData<UserIdResponse>()
    override val toastChannel = Channel<UIText>()
    override val toast = toastChannel.receiveAsFlow()

    init {
        viewModelScope.launch {
            loadUsers()
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
        dialogVisible.value = false
    }

    override suspend fun updateUser() {
        TODO("Not yet implemented")
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
    override val dialogVisible = mutableStateOf(true)
    override val funding = mutableStateOf(TextFieldValue())
    override var currentUser = MutableLiveData<UserIdResponse>()
    override val toastChannel = Channel<UIText>()
    override val toast = toastChannel.receiveAsFlow()

    override suspend fun addFunding() {
        TODO("Not yet implemented")
    }

    override suspend fun updateUser() {
        TODO("Not yet implemented")
    }
}
