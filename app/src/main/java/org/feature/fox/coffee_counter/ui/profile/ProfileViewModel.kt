package org.feature.fox.coffee_counter.ui.profile

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.text.input.TextFieldValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import org.feature.fox.coffee_counter.BuildConfig
import org.feature.fox.coffee_counter.data.repository.UserRepository
import org.feature.fox.coffee_counter.di.services.AppPreference
import javax.inject.Inject

interface IProfileViewModel {
    val nameState: MutableState<TextFieldValue>
    val idState: MutableState<TextFieldValue>
    val passwordState: MutableState<TextFieldValue>
    val retypePasswordState: MutableState<TextFieldValue>
    val isAdminState: MutableState<Boolean>
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
    override val isAdminState = mutableStateOf(true)

    init {
        viewModelScope.launch {
            // FIXME: currently receiving 401 Unauthorized
            val response = userRepository.getUserById(preference.getTag(BuildConfig.USER_ID))
        }
    }
}

class ProfileViewModelPreview : IProfileViewModel {
    override val nameState = mutableStateOf(TextFieldValue("Max"))
    override val idState = mutableStateOf(TextFieldValue("a-cool-id"))
    override val passwordState = mutableStateOf(TextFieldValue("123456789"))
    override val retypePasswordState = mutableStateOf(TextFieldValue("123456789"))
    override val isAdminState = mutableStateOf(false)
}
