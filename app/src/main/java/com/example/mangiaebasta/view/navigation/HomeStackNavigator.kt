package com.example.mangiaebasta.view.navigation

import com.example.mangiaebasta.view.screens.HomeScreen
import com.example.mangiaebasta.view.screens.MenuDetailScreen
import com.example.mangiaebasta.view.screens.OrderConfirmScreen
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.navigation



fun NavGraphBuilder.homeStack(navController: NavHostController) {
    navigation(
        startDestination = "home",
        route = "home_stack"
    ) {
        composable("home") {
            HomeScreen(navController)
        }
        composable("menu_detail") {
            MenuDetailScreen(navController)
        }
        composable("order_confirm") {
            OrderConfirmScreen(navController)
        }
    }
}
