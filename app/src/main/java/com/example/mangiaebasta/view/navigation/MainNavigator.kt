package com.example.mangiaebasta.view.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.mangiaebasta.view.screens.OrderScreen
import com.example.mangiaebasta.viewmodel.MainViewModel

@Composable
fun MainNavigator(
    navController: NavHostController,
    modifier: Modifier = Modifier,
    viewModel: MainViewModel
) {
    NavHost(
        navController = navController,
        startDestination = "home_stack",
        modifier = modifier
    ) {
        // Home stack
        homeStack(navController, viewModel)

        // Order tab
        composable("order") {
            OrderScreen(
                navController,
                viewModel,
                onOrderNowCLick = {
                    navController.navigate("home_stack") {
                        popUpTo("order_screen") { inclusive = true }
                    }
                } )
        }

        // Profile stack
        profileStack(navController, viewModel)
    }
}
