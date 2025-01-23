package com.example.mangiaebasta.view.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.mangiaebasta.view.screens.OrderScreen

@Composable
fun MainNavigator(
    navController: NavHostController,
    modifier: Modifier = Modifier
) {
    NavHost(
        navController = navController,
        startDestination = "home_stack",
        modifier = modifier
    ) {
        // Home stack
        homeStack(navController)

        // Order tab
        composable("order") {
            OrderScreen(navController)
        }

        // Profile stack
        profileStack(navController)
    }
}
