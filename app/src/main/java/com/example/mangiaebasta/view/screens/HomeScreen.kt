package com.example.mangiaebasta.view.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import com.example.mangiaebasta.view.utils.cards.MenuCardWithButton
import com.example.mangiaebasta.viewmodel.MainViewModel

@Composable
fun HomeScreen(
    viewModel: MainViewModel,
    onMenuClick: (Int) -> Unit
) {

    val appState by viewModel.appState.collectAsState()
    val userState by viewModel.userState.collectAsState()
    val menusState by viewModel.menusExplorationState.collectAsState()
    val locationState by viewModel.locationState.collectAsState()

    LaunchedEffect(menusState.reloadMenus, appState.isLoading, locationState.lastKnownLocation) {
        if (appState.isLoading) return@LaunchedEffect
        if (menusState.nearbyMenus.isEmpty() || menusState.reloadMenus) {
            viewModel.fetchNearbyMenus()
        }
    }

    Column {
        Text("Home Screen")
        Text("Nearby Menus")

        LazyColumn {
            items(menusState.nearbyMenus) { menu ->
                MenuCardWithButton(
                    menu = menu,
                    onPress = {
                        onMenuClick(menu.menu.mid) // Passa l'id del menu al click
                    }
                )
            }
        }


    }
}
