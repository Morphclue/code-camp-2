package org.feature.fox.coffee_counter.ui.authentication

import android.util.Base64
import androidx.compose.runtime.MutableState
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
import org.feature.fox.coffee_counter.data.local.database.tables.Purchase
import org.feature.fox.coffee_counter.data.local.database.tables.User
import org.feature.fox.coffee_counter.data.models.body.LoginBody
import org.feature.fox.coffee_counter.data.models.body.UserBody
import org.feature.fox.coffee_counter.data.repository.UserRepository
import org.feature.fox.coffee_counter.di.services.AppPreference
import org.feature.fox.coffee_counter.util.IToast
import org.feature.fox.coffee_counter.util.UIText
import org.json.JSONObject
import javax.inject.Inject

/**
 * Interface for the [AuthenticationViewModel].
 */
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

/**
 * View model for the authentication screen.
 *
 * @property userRepository The user repository.
 * @property preference The app preference.
 */
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

    /**
     * Loads initial values if remember me is checked.
     */
    init {
        viewModelScope.launch {
            loadValues()
        }
    }

    /**
     * Send a login request to the server.
     */
    override suspend fun login() {
        val loginBody = LoginBody(idState.value.text.trim(), passwordState.value.text.trim())
        val response = userRepository.postLogin(loginBody)

        if (response.data == null) {
            toastChannel.send(response.message?.let { UIText.DynamicString(it) }
                ?: UIText.StringResource(R.string.unknown_error))
            return
        }

        preference.setTag(BuildConfig.USER_ID, idState.value.text.trim())
        preference.setTag(BuildConfig.USER_PASSWORD, passwordState.value.text.trim())
        preference.setTag(BuildConfig.EXPIRATION, response.data.expiration.toString())
        preference.setTag(BuildConfig.BEARER_TOKEN, response.data.token)

        val elements = response.data.token.split('.')
        if (elements.size == 3) {
            val (_, payload, _) = elements
            preference.setTag(BuildConfig.IS_ADMIN,
                JSONObject(Base64.decode(payload, Base64.DEFAULT)
                    .decodeToString()).getBoolean("isAdmin"))
        } else {
            error("Invalid token")
        }

        // Fetch User Data to get name of user
        val user = userRepository.getUserById(idState.value.text.trim())

        if (user.data == null) {
            toastChannel.send(response.message?.let { UIText.DynamicString(it) }
                ?: UIText.StringResource(R.string.unknown_error))
            return
        }
        // Insert User into db in case the user registered on another device
        userRepository.insertUserDb(
            User(
                userId = user.data.id,
                name = user.data.name,
                isAdmin = preference.getTag(BuildConfig.IS_ADMIN, true),
            )
        )

        // Fetch Transactions of User and insert/update DB
        val transactions = userRepository.getTransactions(preference.getTag(BuildConfig.USER_ID))
        if (transactions.data == null) {
            toastChannel.send(transactions.message?.let { UIText.DynamicString(it) }
                ?: UIText.StringResource(R.string.unknown_error))
            return
        }
        transactions.data.forEach { transactionResponse ->
            when (transactionResponse.type) {
                "purchase" -> userRepository.insertPurchaseDb(
                    Purchase(
                        timestamp = transactionResponse.timestamp,
                        userId = preference.getTag(BuildConfig.USER_ID),
                        totalValue = transactionResponse.value,
                        itemId = transactionResponse.itemId!!,
                        itemName = transactionResponse.itemName!!,
                        amount = transactionResponse.amount!!,
                    )
                )
                "funding" -> userRepository.insertFundingDb(
                    Funding(
                        timestamp = transactionResponse.timestamp,
                        userId = preference.getTag(BuildConfig.USER_ID),
                        value = transactionResponse.value,
                    )
                )
            }
        }
        showCoreActivity.value = true
    }

    /**
     * Send a register request to the server.
     */
    override suspend fun register() {
        if (passwordState.value.text.trim() != reEnteredPasswordState.value.text.trim()) {
            toastChannel.send(UIText.StringResource(R.string.match_password))
            return
        }

        if (passwordState.value.text.trim().length < 8) {
            toastChannel.send(UIText.StringResource(R.string.password_length))
            return
        }

        if (nameState.value.text.trim().isBlank()) {
            toastChannel.send(UIText.StringResource(R.string.empty_name))
            return
        }

        val registerBody = UserBody(
            idState.value.text.trim().ifEmpty { java.util.UUID.randomUUID().toString() },
            nameState.value.text.trim(),
            passwordState.value.text.trim(),
        )
        val response = userRepository.signUp(registerBody)

        if (response.data == null) {
            toastChannel.send(response.message?.let { UIText.DynamicString(it) }
                ?: UIText.StringResource(R.string.unknown_error))
            return
        }

        idState.value = TextFieldValue(response.data)

        userRepository.insertUserDb(
            User(
                userId = idState.value.text.trim(),
                name = nameState.value.text.trim(),
                false,
            )
        )
        switchToLogin()
        toastChannel.send(UIText.StringResource(R.string.created_account))
    }

    /**
     * Update the remember me checkbox.
     * @param value The new value of the checkbox.
     */
    override fun updateRememberMe(value: Boolean) {
        isChecked.value = value
        preference.setTag(BuildConfig.REMEMBER_ME, value)
    }

    /**
     * Loads the values of the fields if remember me is checked.
     */
    private fun loadValues() {
        isChecked.value = preference.getTag(BuildConfig.REMEMBER_ME, true)
        if (!isChecked.value) {
            return
        }
        idState.value = TextFieldValue(preference.getTag(BuildConfig.USER_ID))
        passwordState.value = TextFieldValue(preference.getTag(BuildConfig.USER_PASSWORD))
    }

    /**
     * Switch to the login view.
     */
    private fun switchToLogin() {
        resetValues()
        loginState.value = true
    }

    /**
     * Resets all necessary TextInputFields.
     */
    private fun resetValues() {
        nameState.value = TextFieldValue()
        passwordState.value = TextFieldValue()
        reEnteredPasswordState.value = TextFieldValue()
    }
}

/**
 * Preview for the authentication screen.
 */
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
