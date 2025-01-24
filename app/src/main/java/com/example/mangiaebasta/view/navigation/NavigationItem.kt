package com.example.mangiaebasta.view.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.ui.graphics.vector.ImageVector

sealed class NavigationItem(val route: String, val title: String, val icon: ImageVector) {
    data object HomeStack : NavigationItem("home_stack", "Home", Icons.Default.Home)
    data object Order : NavigationItem("order", "Order", Icons.Default.ShoppingCart)
    data object ProfileStack : NavigationItem("profile_stack", "Profile", Icons.Default.Person)
}

