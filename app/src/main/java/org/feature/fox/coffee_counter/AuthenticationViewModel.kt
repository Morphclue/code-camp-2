package org.feature.fox.coffee_counter

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

interface IAuthenticationViewModel

@HiltViewModel
class AuthenticationViewModel @Inject constructor() : ViewModel(), IAuthenticationViewModel

class AuthenticationViewModelPreview : IAuthenticationViewModel
