package com.example.mangiaebasta.view.navigation

import com.example.mangiaebasta.view.screens.HomeScreen
import com.example.mangiaebasta.view.screens.MenuDetailScreen
import com.example.mangiaebasta.view.screens.OrderConfirmScreen
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import com.example.mangiaebasta.viewmodel.MainViewModel


fun NavGraphBuilder.homeStack(navController: NavHostController, viewModel: MainViewModel) {
    navigation(
        startDestination = "home",
        route = "home_stack"
    ) {
        composable("home") {
            HomeScreen(viewModel, onMenuClick = {navController.navigate("menu_detail") })
        }
        composable("menu_detail") {
            MenuDetailScreen(viewModel, onForwardClick = {navController.navigate("order_confirm")}, onBackwardClick = {navController.navigateUp()} )
        }
        composable("order_confirm") {
            OrderConfirmScreen(viewModel, onOrderStatusClick = {
                navController.navigate("order") {
                    popUpTo("home_stack") { inclusive = true } // Pulisce lo stack precedente
                }
            }, onBackwardClick = {navController.navigateUp()} )
        }
    }
}
