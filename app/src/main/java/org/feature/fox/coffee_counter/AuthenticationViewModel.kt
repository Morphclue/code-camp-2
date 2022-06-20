package org.feature.fox.coffee_counter

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

interface IAuthenticationViewModel {
    var name: String
    var id: String
    var password: String
}

@HiltViewModel
class AuthenticationViewModel @Inject constructor() : ViewModel(), IAuthenticationViewModel {
    override var name: String by mutableStateOf("")
    override var id: String by mutableStateOf("")
    override var password: String by mutableStateOf("")
}

class AuthenticationViewModelPreview : IAuthenticationViewModel {
    override var name: String by mutableStateOf("Peter")
    override var id: String by mutableStateOf("4242")
    override var password: String by mutableStateOf("1234")
}
