package org.feature.fox.coffee_counter.ui.common

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.RowScope
import androidx.compose.material.BottomNavigation
import androidx.compose.material.BottomNavigationItem
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavDestination
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import org.feature.fox.coffee_counter.ui.items.ItemsView
import org.feature.fox.coffee_counter.ui.profile.ProfileView
import org.feature.fox.coffee_counter.ui.profile.ProfileViewModel
import org.feature.fox.coffee_counter.ui.theme.CrayolaBrown
import org.feature.fox.coffee_counter.ui.theme.LiverOrgan
import org.feature.fox.coffee_counter.ui.transaction.HistoryView
import org.feature.fox.coffee_counter.ui.user.UsersView

@Preview
@Composable
fun PreviewBottomNavBar() {
    BottomNavBar(navController = rememberNavController())
}

@Composable
fun BottomNavBar(navController: NavHostController) {
    val items = listOf(
        BottomNavItem.Items,
        BottomNavItem.History,
        BottomNavItem.Profile,
        BottomNavItem.Users,
    )
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination
    BottomNavigation(
        backgroundColor = CrayolaBrown,
        elevation = 5.dp
    ) {
        items.forEach { item ->
            AddItem(
                item = item,
                currentDestination = currentDestination,
                navController = navController
            )
        }
    }
}

@Composable
fun Navigation(
    navController: NavHostController,
    profileViewModel: ProfileViewModel
) {
    NavHost(navController = navController, startDestination = BottomNavItem.Items.route) {
        composable(BottomNavItem.Items.route) {
            ItemsView()
        }
        composable(BottomNavItem.History.route) {
            HistoryView()
        }
        composable(BottomNavItem.Profile.route) {
            ProfileView(profileViewModel)
        }
        composable(BottomNavItem.Users.route) {
            UsersView()
        }
    }
}

@Composable
fun RowScope.AddItem(
    item: BottomNavItem,
    currentDestination: NavDestination?,
    navController: NavHostController,
) {
    BottomNavigationItem(
        selected = currentDestination?.hierarchy?.any {
            it.route == item.route
        } == true,
        onClick = { navController.navigate(item.route) },
        selectedContentColor = Color.White,
        unselectedContentColor = LiverOrgan,
        icon = { NavBarIcon(item) },
    )
}

@Composable
fun NavBarIcon(
    item: BottomNavItem,
) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Icon(
            imageVector = item.icon,
            contentDescription = item.name
        )
        Text(
            text = item.name,
            textAlign = TextAlign.Center,
            fontSize = 10.sp
        )
    }
}
