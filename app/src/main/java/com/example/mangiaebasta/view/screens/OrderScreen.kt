package com.example.mangiaebasta.view.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.NavController
import com.example.mangiaebasta.view.styles.buttonTextWhiteStyle
import com.example.mangiaebasta.view.styles.orderButtonModifier
import com.example.mangiaebasta.view.utils.button.StyledButton
import com.example.mangiaebasta.view.utils.cards.MenuCard
import com.example.mangiaebasta.viewmodel.MainViewModel

@Composable
fun OrderScreen(navController: NavController, viewModel: MainViewModel, onOrderNowCLick :()-> Unit) {

    val appState by viewModel.appState.collectAsState()
    val orderState by viewModel.lastOrderState.collectAsState()

    val oid = orderState.lastOrder?.oid

    val lifecycleOwner = LocalLifecycleOwner.current

    LaunchedEffect(lifecycleOwner, oid) {
        lifecycleOwner.lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
            while (true) {
                viewModel.fetchLastOrderedMenu()
                viewModel.fetchOrderDetail(oid) // Fetch order detail
                kotlinx.coroutines.delay(5000)
            }
        }
    }
    if(appState.isLoading) {
        return Column {
            Text("Loading...")
        }
    }



    return if (orderState.lastOrderMenu == null) {
         Column {
            Text("Order Screen")
            Text("No order yet")
            StyledButton(
                text = "Order Now",
                modifier = orderButtonModifier,
                textStyle = buttonTextWhiteStyle,
                onClick = { onOrderNowCLick() },
            )
        }
    } else {
         Column {
            Text("Order Screen")
            Text("status: ${orderState.lastOrder?.status}")
            Text("map here")
            MenuCard(orderState.lastOrderMenu!!)}
    }

}
