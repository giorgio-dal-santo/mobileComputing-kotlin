package com.example.mangiaebasta.view.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import com.example.mangiaebasta.view.screens.ProfileScreen
import com.example.mangiaebasta.view.screens.EditProfileScreen
import com.example.mangiaebasta.viewmodel.MainViewModel


fun NavGraphBuilder.profileStack(navController: NavHostController, viewModel: MainViewModel) {
    navigation(
        startDestination = "profile",
        route = "profile_stack"
    ) {
        composable("profile") {
            ProfileScreen(
                viewModel,
                onEditClick = {navController.navigate("edit_profile") },
                onOrderNowCLick = {
                    navController.navigate("home_stack") {
                        popUpTo("profile_stack") { inclusive = true }
                    }
                } )
        }
        composable("edit_profile") {
            EditProfileScreen(viewModel,onBackwardClick = {navController.navigateUp()} )
        }


    }
}
