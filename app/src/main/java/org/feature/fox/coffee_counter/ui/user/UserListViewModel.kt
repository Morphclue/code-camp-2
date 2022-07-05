package org.feature.fox.coffee_counter.ui.user

import androidx.compose.foundation.ScrollState
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import org.feature.fox.coffee_counter.data.local.database.tables.User
import org.feature.fox.coffee_counter.data.repository.UserRepository
import javax.inject.Inject

interface IUserListViewModel {
    val userList: MutableList<User>
    val scrollState: ScrollState
    val isLoaded: MutableState<Boolean>
    var currentUser: MutableLiveData<User>
}

@HiltViewModel
class UserListViewModel @Inject constructor(
    private val userRepository: UserRepository,
) : ViewModel(), IUserListViewModel {
    override val userList = mutableListOf<User>()
    override val scrollState = ScrollState(0)
    override val isLoaded = mutableStateOf(false)
    override var currentUser = MutableLiveData<User>()

    init {
        viewModelScope.launch {
            loadUsers()
        }
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

            userList.add(User(
                id = idResponse.data.id,
                name = idResponse.data.name,
                isAdmin = false,
                password = "",
                balance = idResponse.data.balance
            ))
        }
        isLoaded.value = true
    }
}

class UserListViewModelPreview : IUserListViewModel {
    override val userList = mutableListOf<User>()
    override val scrollState = ScrollState(0)
    override val isLoaded = mutableStateOf(true)
    override var currentUser = MutableLiveData<User>()
}
