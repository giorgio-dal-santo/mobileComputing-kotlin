package com.example.mangiaebasta.view.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import com.example.mangiaebasta.view.utils.cards.MenuCardWithButtonDetailed
import com.example.mangiaebasta.viewmodel.MainViewModel

@Composable
fun MenuDetailScreen(
    viewModel: MainViewModel,
    onForwardClick: () -> Unit,
    onBackwardClick: () -> Unit,
    menuId: Int
) {

    val appState by viewModel.appState.collectAsState()
    val menuState by viewModel.menusExplorationState.collectAsState()

    LaunchedEffect(menuId) {
        viewModel.fetchMenuDetail(menuId)
    }

    if (appState.isLoading || menuState.selectedMenu == null) {
        return Column {
            Text("Loading...")
        }
    }

    Column {
        Text("Menu Detail Screen")
        MenuCardWithButtonDetailed(
            menuState.selectedMenu!!,
            onPress = { onForwardClick() },
            onBack = { onBackwardClick() })
    }
}
