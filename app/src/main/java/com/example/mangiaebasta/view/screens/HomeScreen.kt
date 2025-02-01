package com.example.mangiaebasta.view.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.mangiaebasta.view.utils.cards.MenuCardWithButton
import com.example.mangiaebasta.viewmodel.MainViewModel

@Composable
fun HomeScreen(
    viewModel: MainViewModel,
    onMenuClick: (Int) -> Unit
) {
    val appState by viewModel.appState.collectAsState()
    val menusState by viewModel.menusExplorationState.collectAsState()
    val locationState by viewModel.locationState.collectAsState()

    if (appState.isLoading) {
        return Column {
            Text("Loading...", style = MaterialTheme.typography.titleSmall)
        }
    }

    if (!locationState.isLocationAllowed) {
        return Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(
                    top = 25.dp,
                    start = 16.dp,
                    end = 16.dp,
                    bottom = 12.dp
                )
        ) {
            Text(
                text = "Location access is denied. To enable location services, go to Settings and grant the necessary permissions.",
                style = MaterialTheme.typography.titleLarge,
            )
        }
    }

    LaunchedEffect(
        menusState.reloadMenus,
        appState.isLoading,
        locationState.lastKnownLocation,
        locationState.isLocationAllowed
    ) {
        if (appState.isLoading) return@LaunchedEffect
        viewModel.fetchNearbyMenus()
    }

    Column {
        LazyColumn {
            item {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(
                            top = 25.dp,
                            start = 16.dp,
                            end = 16.dp,
                            bottom = 12.dp
                        )
                ) {
                    Text(
                        text = "Best Menus Around You",
                        style = MaterialTheme.typography.titleLarge,
                        modifier = Modifier.align(Alignment.Center)
                    )
                }
            }

            items(menusState.nearbyMenus) { menu ->
                MenuCardWithButton(
                    menu = menu,
                    onPress = {
                        onMenuClick(menu.menu.mid)
                    }
                )
            }
        }
    }
}