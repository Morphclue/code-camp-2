package org.feature.fox.coffee_counter.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountBox
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.List
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import org.feature.fox.coffee_counter.ui.theme.CrayolaBrown
import org.feature.fox.coffee_counter.ui.theme.CrayolaCopper
import org.feature.fox.coffee_counter.ui.theme.LiverOrgan

@Composable
fun BottomNavigationView(){
    val navController = rememberNavController()
    Scaffold(
        bottomBar = {
            BottomNavBar(
                items = listOf(
                    BottomNavItem(
                        name = "Items",
                        route = "items",
                        icon = Icons.Default.Add
                    ),
                    BottomNavItem(
                        name = "History",
                        route = "history",
                        icon = Icons.Default.List,
                    ),
                    BottomNavItem(
                        name = "Profile",
                        route = "profile",
                        icon = Icons.Default.AccountCircle,
                    ),
                    BottomNavItem(
                        name = "Users",
                        route = "users",
                        icon = Icons.Default.AccountBox,
                    ),
                ),
                navController = navController,
                onItemClick = {
                    navController.navigate(it.route)
                }
            )
        }
    ) {
        Navigation(navController = navController)
    }
}

@Composable
fun Navigation(navController: NavHostController) {
    NavHost(navController = navController, startDestination = "items") {
        composable("items") {
            ItemsView()
        }
        composable("history") {
            HistoryView()
        }
        composable("profile") {
            ProfileView()
        }
        composable("users") {
            UsersView()
        }
    }
}

@Composable
fun BottomNavBar(
    items: List<BottomNavItem>,
    navController: NavController,
    modifier: Modifier = Modifier,
    onItemClick: (BottomNavItem) -> Unit
) {
    val backStackEntry = navController.currentBackStackEntryAsState()
    BottomNavigation(
        modifier = modifier,
        backgroundColor = CrayolaBrown,
        elevation = 5.dp
    ) {
        items.forEach { item ->
            val selected = item.route == backStackEntry.value?.destination?.route
            BottomNavigationItem(
                selected = selected,
                onClick = { onItemClick(item) },
                selectedContentColor = Color.White,
                unselectedContentColor = LiverOrgan,
                icon = { NavBarIcon(item) }
            )
        }
    }
}

@Composable
fun NavBarIcon(
    item: BottomNavItem
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
