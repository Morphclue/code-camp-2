package org.feature.fox.coffee_counter

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import dagger.hilt.android.AndroidEntryPoint
import org.feature.fox.coffee_counter.ui.authentication.HomeView
import org.feature.fox.coffee_counter.ui.theme.CoffeeCounterTheme

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            CoffeeCounterTheme {
                HomeView()
            }
        }
    }
}
