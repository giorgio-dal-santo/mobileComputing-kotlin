package com.example.mangiaebasta.view.screens

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.navigation.NavController
import com.example.mangiaebasta.viewmodel.MainViewModel

@Composable
fun OrderScreen(navController: NavController, viewModel: MainViewModel) {
    Text("Benvenuto alla schermata Order!")
}
