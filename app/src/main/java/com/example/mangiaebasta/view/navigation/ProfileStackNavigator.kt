package com.example.mangiaebasta.view.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import com.example.mangiaebasta.view.screens.ProfileScreen
import com.example.mangiaebasta.view.screens.EditProfileScreen


fun NavGraphBuilder.profileStack(navController: NavHostController) {
    navigation(
        startDestination = "profile",
        route = "profile_stack"
    ) {
        composable("profile") {
            ProfileScreen(navController)
        }
        composable("edit_profile") {
            EditProfileScreen(navController)
        }
    }
}
