package org.feature.fox.coffee_counter.ui

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.material.Scaffold
import androidx.navigation.compose.rememberNavController
import dagger.hilt.android.AndroidEntryPoint
import org.feature.fox.coffee_counter.ui.common.BottomNavBar
import org.feature.fox.coffee_counter.ui.common.Navigation
import org.feature.fox.coffee_counter.ui.items.ItemsViewModel
import org.feature.fox.coffee_counter.ui.profile.ProfileViewModel
import org.feature.fox.coffee_counter.ui.theme.CoffeeCounterTheme
import org.feature.fox.coffee_counter.ui.transaction.TransactionViewModel
import org.feature.fox.coffee_counter.ui.user.UserListViewModel

@AndroidEntryPoint
class CoreActivity : ComponentActivity() {
    private val itemsViewModel: ItemsViewModel by viewModels()

    private val profileViewModel: ProfileViewModel by viewModels()
    private val transactionsViewModel: TransactionViewModel by viewModels()
    private val userListViewModel: UserListViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            CoffeeCounterTheme {
                val navController = rememberNavController()
                Scaffold(
                    bottomBar = { BottomNavBar(navController = navController) }
                ) {
                    Navigation(
                        navController = navController,
                        profileViewModel = profileViewModel,
                        transactionsViewModel = transactionsViewModel,
                        userListViewModel = userListViewModel,
                        itemsViewModel = itemsViewModel,
                    )
                }
            }
        }
    }
}
