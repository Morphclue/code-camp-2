package org.feature.fox.coffee_counter.ui

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.material.Scaffold
import androidx.navigation.compose.rememberNavController
import dagger.hilt.android.AndroidEntryPoint
import org.feature.fox.coffee_counter.ui.common.BottomNavBar
import org.feature.fox.coffee_counter.ui.common.Navigation
import org.feature.fox.coffee_counter.ui.profile.ProfileViewModel
import org.feature.fox.coffee_counter.ui.theme.CoffeeCounterTheme

@AndroidEntryPoint
class CoreActivity : ComponentActivity() {
    private val profileViewModel: ProfileViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        profileViewModel.toastMessage.observe(this) { message ->
            Toast.makeText(this@CoreActivity, message, Toast.LENGTH_SHORT).show()
        }

        setContent {
            CoffeeCounterTheme {
                val navController = rememberNavController()
                Scaffold(
                    bottomBar = { BottomNavBar(navController = navController) }
                ) {
                    Navigation(
                        navController = navController,
                        profileViewModel = profileViewModel,
                    )
                }
            }
        }
    }
}
