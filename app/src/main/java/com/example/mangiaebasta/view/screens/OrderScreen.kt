package com.example.mangiaebasta.view.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.navigation.NavController
import com.example.mangiaebasta.view.utils.MenuHomePreview
import com.example.mangiaebasta.viewmodel.MainViewModel

@Composable
fun OrderScreen(navController: NavController, viewModel: MainViewModel) {

    val appState by viewModel.appState.collectAsState()
    val menuState by viewModel.menusExplorationState.collectAsState()
    val orderState by viewModel.lastOrderState.collectAsState()

    if(appState.isLoading) {
        return Column {
            Text("Loading...")
        }
    }

    val oid = orderState.lastOrder?.oid

    LaunchedEffect(oid) {
        viewModel.fetchLastOrderedMenu() // Fetch last ordered menu
        viewModel.fetchOrderDetail(oid) // Fetch order detail
    }

    Column {
        Text("Order Screen")
        Text("status: ${orderState.lastOrder?.status}")
        Text("map here")
        //MenuHomePreview() aggiungere una preview senza bottone
    }
}
