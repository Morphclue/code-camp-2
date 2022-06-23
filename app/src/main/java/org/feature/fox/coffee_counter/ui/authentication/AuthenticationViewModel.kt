package org.feature.fox.coffee_counter.ui.authentication

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.text.input.TextFieldValue
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import org.feature.fox.coffee_counter.data.repository.UserRepository
import javax.inject.Inject

interface IAuthenticationViewModel {
    var nameState: MutableState<TextFieldValue>
    var idState: MutableState<TextFieldValue>
    var passwordState: MutableState<TextFieldValue>
    var reEnteredPasswordState: MutableState<TextFieldValue>

    suspend fun login()
}

@HiltViewModel
class AuthenticationViewModel @Inject constructor(
    private val userRepository: UserRepository,
) : ViewModel(), IAuthenticationViewModel {
    override var nameState = mutableStateOf(TextFieldValue())
    override var idState = mutableStateOf(TextFieldValue())
    override var passwordState = mutableStateOf(TextFieldValue())
    override var reEnteredPasswordState = mutableStateOf(TextFieldValue())

    override suspend fun login() {
        TODO("Not yet implemented")
    }
}

class AuthenticationViewModelPreview : IAuthenticationViewModel {
    override var nameState = mutableStateOf(TextFieldValue("Peter"))
    override var idState = mutableStateOf(TextFieldValue("4242"))
    override var passwordState = mutableStateOf(TextFieldValue("1234"))
    override var reEnteredPasswordState = mutableStateOf(TextFieldValue("1234"))

    override suspend fun login() {
        TODO("Not yet implemented")
    }
}
