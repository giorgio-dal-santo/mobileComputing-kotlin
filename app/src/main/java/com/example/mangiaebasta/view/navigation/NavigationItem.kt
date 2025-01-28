package com.example.mangiaebasta.view.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material.icons.outlined.ShoppingCart
import androidx.compose.ui.graphics.vector.ImageVector

sealed class NavigationItem(
    val route: String,
    val title: String,
    val selectedIcon: ImageVector,
    val unselectedIcon: ImageVector
) {
    data object HomeStack : NavigationItem(
        "home_stack",
        "Home",
        selectedIcon = Icons.Filled.Home,
        unselectedIcon = Icons.Outlined.Home
    )

    data object Order : NavigationItem(
        "order",
        "Order",
        selectedIcon = Icons.Filled.ShoppingCart,
        unselectedIcon = Icons.Outlined.ShoppingCart
    )

    data object ProfileStack : NavigationItem(
        "profile_stack",
        "Profile",
        selectedIcon = Icons.Filled.Person,
        unselectedIcon = Icons.Outlined.Person
    )
}

