package com.example.mangiaebasta.view.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import com.example.mangiaebasta.view.styles.buttonTextBlackStyle
import com.example.mangiaebasta.view.styles.goBackButtonModifier
import com.example.mangiaebasta.view.utils.ErrorDialog
import com.example.mangiaebasta.view.utils.Header
import com.example.mangiaebasta.view.utils.button.StyledButton
import com.example.mangiaebasta.viewmodel.MainViewModel
import com.mapbox.geojson.Point
import com.mapbox.maps.extension.compose.MapboxMap
import com.mapbox.maps.extension.compose.animation.viewport.rememberMapViewportState


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
        return Column {
            Text(appState.error!!.message)
            StyledButton(
                text = "Got it!",
                modifier = goBackButtonModifier,
                textStyle = buttonTextBlackStyle,
                onClick = {
                    viewModel.resetError() // Resetta l'errore
                    onBackwardClick()
                }
            )
        }

    }

    Column {
        Header("Mangia e Basta")

        Text("Thank you for your order!",  style = MaterialTheme.typography.titleLarge)

        Button(onClick = { onOrderStatusClick() }) {
            Text("Go to Order Status")
        }
        Button(onClick = { onBackwardClick() }) {
            Text("Back")
        }

        // se abbiamo tempo mettere mappa

    }

}
