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
import com.example.mangiaebasta.view.styles.buttonTextWhiteStyle
import com.example.mangiaebasta.view.styles.orderButtonModifier
import com.example.mangiaebasta.view.utils.button.StyledButton
import com.example.mangiaebasta.view.utils.cards.MenuCard
import com.example.mangiaebasta.viewmodel.MainViewModel

@Composable
fun OrderScreen(
    viewModel: MainViewModel,
    onOrderNowCLick: () -> Unit
) {

    val appState by viewModel.appState.collectAsState()
    val orderState by viewModel.lastOrderState.collectAsState()

    val lifecycleOwner = LocalLifecycleOwner.current

    LaunchedEffect(lifecycleOwner) {
        lifecycleOwner.lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
            while (true) {
                viewModel.fetchLastOrderDetail()
                kotlinx.coroutines.delay(5000)
            }
        }
    }

    if (appState.isLoading) {
        return Column {
            Text("Loading...")
        }
    }

    if (orderState.lastOrder == null || orderState.lastOrderMenu == null) {
        return Column {
            Text("Order Screen")
            Text("No order yet")
            StyledButton(
                text = "Order Now",
                modifier = orderButtonModifier,
                textStyle = buttonTextWhiteStyle,
                onClick = { onOrderNowCLick() },
            )
        }
    }

    Column {
        Text("Order Screen")
        Text("status: ${orderState.lastOrder?.status}")
        Text("map here")
        Text("your order will arrive at: ${orderState.lastOrder?.expectedDeliveryTimestamp}")
        MenuCard(orderState.lastOrderMenu!!)
    }

}