package org.feature.fox.coffee_counter.ui.authentication

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.text.input.TextFieldValue
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import at.favre.lib.crypto.bcrypt.BCrypt
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import org.feature.fox.coffee_counter.BuildConfig
import org.feature.fox.coffee_counter.R
import org.feature.fox.coffee_counter.data.local.database.tables.User
import org.feature.fox.coffee_counter.data.models.body.LoginBody
import org.feature.fox.coffee_counter.data.models.body.UserBody
import org.feature.fox.coffee_counter.data.repository.UserRepository
import org.feature.fox.coffee_counter.di.services.AppPreference
import org.feature.fox.coffee_counter.util.IToast
import org.feature.fox.coffee_counter.util.UIText
import javax.inject.Inject

interface IAuthenticationViewModel : IToast {
    val nameState: MutableState<TextFieldValue>
    val idState: MutableState<TextFieldValue>
    val passwordState: MutableState<TextFieldValue>
    val reEnteredPasswordState: MutableState<TextFieldValue>
    val showCoreActivity: MutableLiveData<Boolean>
    val loginState: MutableState<Boolean>
    val isChecked: MutableState<Boolean>

    suspend fun login()
    suspend fun register()
    fun updateRememberMe(value: Boolean)
}

@HiltViewModel
class AuthenticationViewModel @Inject constructor(
    private val userRepository: UserRepository,
    private val preference: AppPreference,
) : ViewModel(), IAuthenticationViewModel {
    override val nameState = mutableStateOf(TextFieldValue())
    override val idState = mutableStateOf(TextFieldValue())
    override val passwordState = mutableStateOf(TextFieldValue())
    override val reEnteredPasswordState = mutableStateOf(TextFieldValue())
    override val showCoreActivity = MutableLiveData<Boolean>()
    override val loginState = mutableStateOf(false)
    override var isChecked = mutableStateOf(false)
    override val toastChannel = Channel<UIText>()
    override val toast = toastChannel.receiveAsFlow()

    init {
        viewModelScope.launch {
            loadValues()
        }
    }

    override suspend fun login() {
        val loginBody = LoginBody(idState.value.text, passwordState.value.text)
        val response = userRepository.postLogin(loginBody)

        if (response.data == null) {
            toastChannel.send(response.message?.let { UIText.DynamicString(it) }
                ?: UIText.StringResource(R.string.unknown_error))
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
            toastChannel.send(UIText.StringResource(R.string.match_password))
            return
        }

        val registerBody = UserBody(
            idState.value.text.ifEmpty { null },
            nameState.value.text,
            passwordState.value.text,
        )
        val response = userRepository.signUp(registerBody)

        if (response.data == null) {
            toastChannel.send(response.message?.let { UIText.DynamicString(it) }
                ?: UIText.StringResource(R.string.unknown_error))
            return
        }

        idState.value = TextFieldValue(response.data)
        userRepository.insertUser(
            User(
                id = idState.value.text,
                name = nameState.value.text,
                false,
                password = BCrypt.withDefaults()
                    .hashToString(12, passwordState.value.text.toCharArray())
            )
        )
        switchToLogin()
        toastChannel.send(UIText.StringResource(R.string.created_account))
    }

    override fun updateRememberMe(value: Boolean) {
        isChecked.value = value
        preference.setTag(BuildConfig.REMEMBER_ME, value)
    }

    private fun loadValues() {
        isChecked.value = preference.getTag(BuildConfig.REMEMBER_ME, true)
        if (!isChecked.value) {
            return
        }
        idState.value = TextFieldValue(preference.getTag(BuildConfig.USER_ID))
        passwordState.value = TextFieldValue(preference.getTag(BuildConfig.USER_PASSWORD))
    }

    private fun switchToLogin() {
        resetValues()
        loginState.value = true
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
    override val loginState = mutableStateOf(true)
    override val isChecked = mutableStateOf(true)
    override val toastChannel = Channel<UIText>()
    override val toast = toastChannel.receiveAsFlow()

    override suspend fun login() {
        TODO("Not yet implemented")
    }

    override suspend fun register() {
        TODO("Not yet implemented")
    }

    override fun updateRememberMe(value: Boolean) {
        TODO("Not yet implemented")
    }
}
