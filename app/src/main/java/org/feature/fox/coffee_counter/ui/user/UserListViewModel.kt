package org.feature.fox.coffee_counter.ui.user

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import org.feature.fox.coffee_counter.data.local.database.tables.User
import org.feature.fox.coffee_counter.data.repository.UserRepository
import javax.inject.Inject

interface IUserListViewModel {
    val userList: MutableList<User>
}

@HiltViewModel
class UserListViewModel @Inject constructor(
    private val userRepository: UserRepository,
) : ViewModel(), IUserListViewModel {
    override val userList = mutableListOf<User>()

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
            // TODO
        }
    }
}

class UserListViewModelPreview : IUserListViewModel {
    override val userList = mutableListOf<User>()
}
