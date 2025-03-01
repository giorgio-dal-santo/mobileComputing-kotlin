package com.example.mangiaebasta.view.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import androidx.navigation.navigation
import com.example.mangiaebasta.model.dataClasses.APILocation
import com.example.mangiaebasta.view.screens.HomeScreen
import com.example.mangiaebasta.view.screens.MenuDetailScreen
import com.example.mangiaebasta.view.screens.OrderConfirmScreen
import com.example.mangiaebasta.viewmodel.MainViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

fun NavGraphBuilder.homeStack(navController: NavHostController, viewModel: MainViewModel) {
    navigation(
        startDestination = "home",
        route = "home_stack"
    ) {
        composable("home") {
            HomeScreen(viewModel, onMenuClick = { menuId ->
                navController.navigate("menu_detail/$menuId")
            })
        }

        composable(
            route = "menu_detail/{menuId}",
            arguments = listOf(
                navArgument("menuId") {
                    type = NavType.StringType
                }
            )
        ) { backStackEntry ->
            val menuId = backStackEntry.arguments?.getString("menuId") ?: ""
            MenuDetailScreen(
                viewModel,
                onForwardClick = {
                    CoroutineScope(Dispatchers.Main).launch {
                        //val hardCodedLocation = APILocation(45.4642, 9.19)
                        viewModel.newOrder(viewModel.getCurrentAPILocation(), menuId.toInt())
                        navController.navigate("order_confirm")
                    }
                },
                onBackwardClick = { navController.navigateUp() },
                menuId = menuId.toInt()
            )
        }

        composable("order_confirm") {
            OrderConfirmScreen(
                viewModel,
                onOrderStatusClick = {
                    navController.navigate("order") {
                        popUpTo("home_stack") { inclusive = true }
                    }
                },
                onBackwardClick = { navController.navigateUp() }
            )
        }
    }
}

