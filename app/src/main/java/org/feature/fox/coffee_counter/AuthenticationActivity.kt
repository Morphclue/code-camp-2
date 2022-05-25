package org.feature.fox.coffee_counter

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import org.feature.fox.coffee_counter.ui.AuthenticationView
import org.feature.fox.coffee_counter.ui.theme.CoffeeCounterTheme

class AuthenticationActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            CoffeeCounterTheme {
                AuthenticationView()
            }
        }
    }
}

