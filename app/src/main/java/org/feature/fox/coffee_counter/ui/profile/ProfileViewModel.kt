package org.feature.fox.coffee_counter.ui.profile

import android.graphics.Bitmap
import android.graphics.BitmapFactory
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
import org.feature.fox.coffee_counter.data.models.body.UserBody
import org.feature.fox.coffee_counter.data.repository.UserRepository
import org.feature.fox.coffee_counter.di.services.AppPreference
import org.feature.fox.coffee_counter.util.IToast
import org.feature.fox.coffee_counter.util.UIText
import java.io.InputStream
import javax.inject.Inject

interface IProfileViewModel : IToast {
    val nameState: MutableState<TextFieldValue>
    val idState: MutableState<TextFieldValue>
    val passwordState: MutableState<TextFieldValue>
    val retypePasswordState: MutableState<TextFieldValue>
    val isAdminState: MutableState<Boolean>
    val showMainActivity: MutableLiveData<Boolean>
    val balance: MutableState<Double>
    val userImage: MutableState<String>
    val bitmap: MutableState<Bitmap?>

    suspend fun loadData()
    suspend fun updateUser()
    suspend fun deleteUser()
    suspend fun getTotalBalance()
    suspend fun getImage()
    suspend fun updateImage(stream: InputStream)
}

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val userRepository: UserRepository,
    private val preference: AppPreference,
) : ViewModel(), IProfileViewModel {
    override val nameState = mutableStateOf(TextFieldValue())
    override val idState = mutableStateOf(TextFieldValue())
    override val passwordState = mutableStateOf(TextFieldValue())
    override val retypePasswordState = mutableStateOf(TextFieldValue())
    override val isAdminState = mutableStateOf(false)
    override val showMainActivity = MutableLiveData<Boolean>()
    override val toastChannel = Channel<UIText>()
    override val toast = toastChannel.receiveAsFlow()
    override val balance = mutableStateOf(0.0)
    override val userImage = mutableStateOf("")
    override val bitmap = mutableStateOf<Bitmap?>(null)

    init {
        viewModelScope.launch {
            loadData()
            getImage()
            getTotalBalance()
        }
    }

    override suspend fun loadData() {
        val response = userRepository.getUserById(preference.getTag(BuildConfig.USER_ID))

        if (response.data == null) {
            toastChannel.send(response.message?.let { UIText.DynamicString(it) }
                ?: UIText.StringResource(R.string.unknown_error))
            return
        }

        idState.value = TextFieldValue(response.data.id)
        nameState.value = TextFieldValue(response.data.name)
    }

    override suspend fun updateUser() {
        if (passwordState.value.text != retypePasswordState.value.text) {
            toastChannel.send(UIText.StringResource(R.string.match_password))
            return
        }
        val userBody = UserBody(
            idState.value.text,
            nameState.value.text,
            passwordState.value.text,
            null
        )

        val response = userRepository.updateUser(
            preference.getTag(BuildConfig.USER_ID),
            userBody
        )

        if (response.data == null) {
            toastChannel.send(response.message?.let { UIText.DynamicString(it) }
                ?: UIText.StringResource(R.string.unknown_error))
            return
        }

        preference.setTag(BuildConfig.USER_ID, idState.value.text)
        toastChannel.send(UIText.StringResource(R.string.updated_user))
    }

    override suspend fun deleteUser() {
        val response = userRepository.deleteUser(preference.getTag(BuildConfig.USER_ID))

        if (response.data == null) {
            toastChannel.send(response.message?.let { UIText.DynamicString(it) }
                ?: UIText.StringResource(R.string.unknown_error))
            return
        }
        toastChannel.send(UIText.StringResource(R.string.deleted_user))
        removeTags()
        showMainActivity.value = true
    }

    override suspend fun getTotalBalance() {
        val response = userRepository.getUserById(preference.getTag(BuildConfig.USER_ID))

        if (response.data == null) {
            toastChannel.send(response.message?.let { UIText.DynamicString(it) }
                ?: UIText.StringResource(R.string.unknown_error))
            return
        }
        balance.value = response.data.balance
    }

    override suspend fun getImage() {
        val timestampResponse = userRepository.getImageTimestamp(
            preference.getTag(BuildConfig.USER_ID)
        )
        if (timestampResponse.data == null) {
            toastChannel.send(timestampResponse.message?.let { UIText.DynamicString(it) }
                ?: UIText.StringResource(R.string.unknown_error))
            return
        }

        val dbImage = userRepository.getImageByIdFromUser(preference.getTag(BuildConfig.USER_ID))
        if (dbImage.timestamp >= timestampResponse.data) {
            setImage(dbImage.encodedImage)
            return
        }

        val response = userRepository.getImage(preference.getTag(BuildConfig.USER_ID))
        if (response.data == null) {
            toastChannel.send(response.message?.let { UIText.DynamicString(it) }
                ?: UIText.StringResource(R.string.unknown_error))
            return
        }
        setImage(response.data.encodedImage)
    }

    private fun setImage(encodedImage: String) {
        val imageBytes = Base64.decode(encodedImage, Base64.DEFAULT)
        bitmap.value = BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.size)
    }

    override suspend fun updateImage(stream: InputStream) {
        val response = userRepository.uploadImage(
            preference.getTag(BuildConfig.USER_ID),
            stream
        )
        if (response.data == null) {
            toastChannel.send(response.message?.let { UIText.DynamicString(it) }
                ?: UIText.StringResource(R.string.unknown_error))
            return
        }
        getImage()
    }

    private fun removeTags() {
        preference.removeTag(BuildConfig.USER_ID)
        preference.removeTag(BuildConfig.USER_PASSWORD)
        preference.removeTag(BuildConfig.BEARER_TOKEN)
        preference.removeTag(BuildConfig.EXPIRATION)
    }
}

class ProfileViewModelPreview : IProfileViewModel {
    override val nameState = mutableStateOf(TextFieldValue("Max"))
    override val idState = mutableStateOf(TextFieldValue("a-cool-id"))
    override val passwordState = mutableStateOf(TextFieldValue("123456789"))
    override val retypePasswordState = mutableStateOf(TextFieldValue("123456789"))
    override val isAdminState = mutableStateOf(false)
    override val showMainActivity = MutableLiveData<Boolean>()
    override val balance = mutableStateOf(50.0)
    override val toastChannel = Channel<UIText>()
    override val toast = toastChannel.receiveAsFlow()
    override val userImage = mutableStateOf("")
    override val bitmap = mutableStateOf<Bitmap?>(null)

    override suspend fun loadData() {
        TODO("Not yet implemented")
    }

    override suspend fun updateUser() {
        TODO("Not yet implemented")
    }

    override suspend fun deleteUser() {
        TODO("Not yet implemented")
    }

    override suspend fun getTotalBalance() {
        TODO("Not yet implemented")
    }

    override suspend fun getImage() {
        TODO("Not yet implemented")
    }

    override suspend fun updateImage(stream: InputStream) {
        TODO("Not yet implemented")
    }
}
