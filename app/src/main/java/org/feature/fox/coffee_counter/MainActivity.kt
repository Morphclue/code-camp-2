package org.feature.fox.coffee_counter

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import dagger.hilt.android.AndroidEntryPoint
import org.feature.fox.coffee_counter.ui.authentication.HomeView
import org.feature.fox.coffee_counter.ui.theme.CoffeeCounterTheme

/**
 * Main activity of the application.
 */
@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    /**
     * Called when the activity is created.
     * TODO: Add further description.
     *
     * @param savedInstanceState the saved instance state.
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            CoffeeCounterTheme {
                HomeView()
            }
        }
    }
}
