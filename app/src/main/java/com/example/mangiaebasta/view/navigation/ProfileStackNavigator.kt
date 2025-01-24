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
            ProfileScreen(onEditClick = {navController.navigate("edit_profile") })
        }
        composable("edit_profile") {
            EditProfileScreen(onBackwardClick = {navController.navigateUp()} )
        }
    }
}
