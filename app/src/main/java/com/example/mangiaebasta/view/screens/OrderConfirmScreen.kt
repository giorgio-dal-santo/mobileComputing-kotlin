package com.example.mangiaebasta.view.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.navigation.NavController
import com.example.mangiaebasta.viewmodel.MainViewModel

@Composable
fun OrderConfirmScreen(viewModel: MainViewModel, onOrderStatusClick: () -> Unit, onBackwardClick: () -> Unit) {
    Column {
        Text("Order Confirm Screen")
        Button(onClick = {onOrderStatusClick()}) {
            Text("Go to Order Status")
        }
        Button(onClick = {onBackwardClick()}) {
            Text("Back")
        }
    }

}
