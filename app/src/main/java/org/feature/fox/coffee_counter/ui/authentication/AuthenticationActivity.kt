package org.feature.fox.coffee_counter.ui.authentication

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import dagger.hilt.android.AndroidEntryPoint
import org.feature.fox.coffee_counter.ui.theme.CoffeeCounterTheme

@AndroidEntryPoint
class AuthenticationActivity : ComponentActivity() {

    private val authenticationViewModel: AuthenticationViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        authenticationViewModel.loginState.value = intent?.extras?.getBoolean("login") ?: true

        setContent {
            CoffeeCounterTheme {
                AuthenticationView(
                    authenticationViewModel
                )
            }
        }
    }
}
