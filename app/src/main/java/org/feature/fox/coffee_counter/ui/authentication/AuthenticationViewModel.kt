package org.feature.fox.coffee_counter.ui.authentication

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.text.input.TextFieldValue
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import org.feature.fox.coffee_counter.BuildConfig
import org.feature.fox.coffee_counter.data.models.body.LoginBody
import org.feature.fox.coffee_counter.data.models.body.UserBody
import org.feature.fox.coffee_counter.data.repository.UserRepository
import org.feature.fox.coffee_counter.di.module.ApiModule
import org.feature.fox.coffee_counter.di.services.AppPreference
import javax.inject.Inject

interface IAuthenticationViewModel {
    var nameState: MutableState<TextFieldValue>
    var idState: MutableState<TextFieldValue>
    var passwordState: MutableState<TextFieldValue>
    var reEnteredPasswordState: MutableState<TextFieldValue>
    val showCoreActivity: MutableLiveData<Boolean>

    suspend fun login()
    suspend fun register()
}

@HiltViewModel
class AuthenticationViewModel @Inject constructor(
    private val userRepository: UserRepository,
    private val preference: AppPreference,
) : ViewModel(), IAuthenticationViewModel {
    override var nameState = mutableStateOf(TextFieldValue())
    override var idState = mutableStateOf(TextFieldValue())
    override var passwordState = mutableStateOf(TextFieldValue())
    override var reEnteredPasswordState = mutableStateOf(TextFieldValue())
    override val showCoreActivity = MutableLiveData<Boolean>()

    override suspend fun login() {
        val loginBody = LoginBody(idState.value.text, passwordState.value.text)
        val response = userRepository.postLogin(loginBody)

        if (response.data == null) {
            return
        }

        preference.setTag(BuildConfig.USER_ID, idState.value.text)
        preference.setTag(BuildConfig.USER_PASSWORD, passwordState.value.text)

        ApiModule.providesBearerInterceptor().expiration = response.data.expiration
        ApiModule.providesBearerInterceptor().bearerToken = response.data.token
        showCoreActivity.value = true
    }

    override suspend fun register() {
        if (passwordState.value.text != reEnteredPasswordState.value.text) {
            return
        }

        val registerBody = UserBody(
            idState.value.text,
            nameState.value.text,
            passwordState.value.text,
        )
        val response = userRepository.signUp(registerBody)

        if (response.data == null) {
            return
        }

        resetValues()
    }

    private fun resetValues() {
        nameState.value = TextFieldValue()
        passwordState.value = TextFieldValue()
        reEnteredPasswordState.value = TextFieldValue()
    }
}

class AuthenticationViewModelPreview : IAuthenticationViewModel {
    override var nameState = mutableStateOf(TextFieldValue("Peter"))
    override var idState = mutableStateOf(TextFieldValue("4242"))
    override var passwordState = mutableStateOf(TextFieldValue("1234"))
    override var reEnteredPasswordState = mutableStateOf(TextFieldValue("1234"))
    override val showCoreActivity = MutableLiveData<Boolean>()

    override suspend fun login() {
        TODO("Not yet implemented")
    }

    override suspend fun register() {
        TODO("Not yet implemented")
    }
}
