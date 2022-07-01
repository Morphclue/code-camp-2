package org.feature.fox.coffee_counter.ui.authentication

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.text.input.TextFieldValue
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import org.feature.fox.coffee_counter.BuildConfig
import org.feature.fox.coffee_counter.R
import org.feature.fox.coffee_counter.data.models.body.LoginBody
import org.feature.fox.coffee_counter.data.models.body.UserBody
import org.feature.fox.coffee_counter.data.repository.UserRepository
import org.feature.fox.coffee_counter.di.services.AppPreference
import org.feature.fox.coffee_counter.di.services.ResourcesProvider
import javax.inject.Inject

interface IAuthenticationViewModel {
    val nameState: MutableState<TextFieldValue>
    val idState: MutableState<TextFieldValue>
    val passwordState: MutableState<TextFieldValue>
    val reEnteredPasswordState: MutableState<TextFieldValue>
    val showCoreActivity: MutableLiveData<Boolean>
    val toastMessage: MutableLiveData<String>
    val loginState: MutableState<Boolean>

    suspend fun login()
    suspend fun register()
}

@HiltViewModel
class AuthenticationViewModel @Inject constructor(
    private val userRepository: UserRepository,
    private val preference: AppPreference,
    private val resource: ResourcesProvider,
) : ViewModel(), IAuthenticationViewModel {
    override val nameState = mutableStateOf(TextFieldValue())
    override val idState = mutableStateOf(TextFieldValue())
    override val passwordState = mutableStateOf(TextFieldValue())
    override val reEnteredPasswordState = mutableStateOf(TextFieldValue())
    override val showCoreActivity = MutableLiveData<Boolean>()
    override val toastMessage = MutableLiveData<String>()
    override val loginState = mutableStateOf(false)

    override suspend fun login() {
        val loginBody = LoginBody(idState.value.text, passwordState.value.text)
        val response = userRepository.postLogin(loginBody)

        if (response.data == null) {
            toastMessage.value = response.message ?: resource.getString(R.string.unknown_error)
            return
        }

        preference.setTag(BuildConfig.USER_ID, idState.value.text)
        preference.setTag(BuildConfig.USER_PASSWORD, passwordState.value.text)
        preference.setTag(BuildConfig.EXPIRATION, response.data.expiration.toString())
        preference.setTag(BuildConfig.BEARER_TOKEN, response.data.token)

        showCoreActivity.value = true
    }

    override suspend fun register() {
        if (passwordState.value.text != reEnteredPasswordState.value.text) {
            toastMessage.value = resource.getString(R.string.match_password)
            return
        }

        val registerBody = UserBody(
            idState.value.text,
            nameState.value.text,
            passwordState.value.text,
        )
        val response = userRepository.signUp(registerBody)

        if (response.data == null) {
            toastMessage.value = response.message ?: resource.getString(R.string.unknown_error)
            return
        }

        switchToLogin()
    }

    private fun switchToLogin() {
        resetValues()
        loginState.value = true
        toastMessage.value = resource.getString(R.string.created_account)
    }

    private fun resetValues() {
        nameState.value = TextFieldValue()
        passwordState.value = TextFieldValue()
        reEnteredPasswordState.value = TextFieldValue()
    }
}

class AuthenticationViewModelPreview : IAuthenticationViewModel {
    override val nameState = mutableStateOf(TextFieldValue("Peter"))
    override val idState = mutableStateOf(TextFieldValue("4242"))
    override val passwordState = mutableStateOf(TextFieldValue("1234"))
    override val reEnteredPasswordState = mutableStateOf(TextFieldValue("1234"))
    override val showCoreActivity = MutableLiveData<Boolean>()
    override val toastMessage = MutableLiveData<String>()
    override val loginState = mutableStateOf(true)

    override suspend fun login() {
        TODO("Not yet implemented")
    }

    override suspend fun register() {
        TODO("Not yet implemented")
    }
}
