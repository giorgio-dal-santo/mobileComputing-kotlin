package com.example.mangiaebasta.view.screens

import android.util.Log
import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.navigation.NavController
import com.example.mangiaebasta.model.dataClasses.APILocation
import com.example.mangiaebasta.viewmodel.MainViewModel

@Composable
fun OrderConfirmScreen(viewModel: MainViewModel, onOrderStatusClick: () -> Unit, onBackwardClick: () -> Unit) {

    val appState by viewModel.appState.collectAsState()
    val menuState by viewModel.menusExplorationState.collectAsState()
    val orderState by viewModel.lastOrderState.collectAsState()

    if(appState.isLoading) {
        return Column {
            Text("Loading...")
        }
    }

    val hardCodedLocation = APILocation(45.4642, 9.19)

    LaunchedEffect(hardCodedLocation, menuState.selectedMenu?.menuDetails?.mid) {
        viewModel.newOrder(hardCodedLocation, menuState.selectedMenu?.menuDetails?.mid)
    }


    Column {
        Text("Order Confirm Screen")
        Text("Thank you for your order!")
        Text("map here")
        Button(onClick = {onOrderStatusClick()}) {
            Text("Go to Order Status")
        }
        Button(onClick = {onBackwardClick()}) {
            Text("Back")
        }
    }

}
