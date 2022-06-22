package org.feature.fox.coffee_counter

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import org.feature.fox.coffee_counter.data.models.body.LoginBody
import org.feature.fox.coffee_counter.data.repository.UserRepository
import org.feature.fox.coffee_counter.di.module.ApiModule
import javax.inject.Inject

interface IAuthenticationViewModel {

    var name: String
    var id: String
    var password: String

    suspend fun login()
}

@HiltViewModel
class AuthenticationViewModel @Inject constructor(
    private val userRepository: UserRepository,
) : ViewModel(), IAuthenticationViewModel {
    override var name: String by mutableStateOf("")
    override var id: String by mutableStateOf("")
    override var password: String by mutableStateOf("")

    override suspend fun login() {
        TODO("Not yet implemented")
    }
}

class AuthenticationViewModelPreview : IAuthenticationViewModel {
    override var name: String by mutableStateOf("Peter")
    override var id: String by mutableStateOf("4242")
    override var password: String by mutableStateOf("1234")

    override suspend fun login() {
        TODO("Not yet implemented")
    }
}
