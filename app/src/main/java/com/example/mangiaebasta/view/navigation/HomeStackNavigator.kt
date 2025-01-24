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
            HomeScreen(onMenuClick = {navController.navigate("menu_detail") })
        }
        composable("menu_detail") {
            MenuDetailScreen(onForwardClick = {navController.navigate("order_confirm")}, onBackwardClick = {navController.navigateUp()} )
        }
        composable("order_confirm") {
            OrderConfirmScreen(onOrderStatusClick = {navController.navigate("order_screen")}, onBackwardClick = {navController.navigateUp()} )
        }
    }
}
