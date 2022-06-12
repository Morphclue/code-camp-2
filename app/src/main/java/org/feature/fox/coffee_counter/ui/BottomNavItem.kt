package org.feature.fox.coffee_counter.ui

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountBox
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.List
import androidx.compose.ui.graphics.vector.ImageVector

sealed class BottomNavItem(
    val name: String,
    val route: String,
    val icon: ImageVector,
) {
    object Items : BottomNavItem(
        name = "Items",
        route = "items",
        icon = Icons.Default.Add
    )

    object History : BottomNavItem(
        name = "History",
        route = "history",
        icon = Icons.Default.List,
    )

    object Profile : BottomNavItem(
        name = "Profile",
        route = "profile",
        icon = Icons.Default.AccountCircle,
    )

    object Users : BottomNavItem(
        name = "Users",
        route = "users",
        icon = Icons.Default.AccountBox,
    )
}
