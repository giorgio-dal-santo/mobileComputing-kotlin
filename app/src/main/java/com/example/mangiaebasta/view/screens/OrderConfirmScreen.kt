package com.example.mangiaebasta.view.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import com.example.mangiaebasta.view.utils.ErrorDialog
import com.example.mangiaebasta.viewmodel.MainViewModel

@Composable
fun OrderConfirmScreen(
    viewModel: MainViewModel,
    onOrderStatusClick: () -> Unit,
    onBackwardClick: () -> Unit
) {

    val appState by viewModel.appState.collectAsState()
    val menuState by viewModel.menusExplorationState.collectAsState()
    val orderState by viewModel.lastOrderState.collectAsState()

    if (appState.isLoading) {
        return Column {
            Text("Loading...")
        }
    }

    if (appState.error != null) {
        ErrorDialog(
            error = appState.error!!,
            onDismiss = {
                viewModel.resetError() // Resetta l'errore
                onBackwardClick()
            }

        )
    }




    Column {
        Text("Order Confirm Screen")
        Text("Thank you for your order!")
        Text("map here")
        Button(onClick = { onOrderStatusClick() }) {
            Text("Go to Order Status")
        }
        Button(onClick = { onBackwardClick() }) {
            Text("Back")
        }
    }

}
