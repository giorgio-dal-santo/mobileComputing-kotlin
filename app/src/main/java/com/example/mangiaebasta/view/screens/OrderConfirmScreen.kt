package com.example.mangiaebasta.view.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.mangiaebasta.view.styles.GlobalDimensions
import com.example.mangiaebasta.view.styles.buttonTextBlackStyle
import com.example.mangiaebasta.view.styles.buttonTextWhiteStyle
import com.example.mangiaebasta.view.styles.detailButtonModifier
import com.example.mangiaebasta.view.styles.goBackButtonModifier
import com.example.mangiaebasta.view.utils.Header
import com.example.mangiaebasta.view.utils.button.StyledButton
import com.example.mangiaebasta.viewmodel.MainViewModel

@Composable
fun OrderConfirmScreen(
    viewModel: MainViewModel,
    onOrderStatusClick: () -> Unit,
    onBackwardClick: () -> Unit
) {

    val appState by viewModel.appState.collectAsState()

    if (appState.isLoading) {
        return Column {
            Text("Loading...", style = MaterialTheme.typography.titleSmall)
        }
    }

    if (appState.error != null) {
        return Column (
            modifier = Modifier
                .fillMaxSize()
                .padding(GlobalDimensions.DefaultPadding)
        ) {
            Text(appState.error!!.message, style = MaterialTheme.typography.titleLarge)
            Spacer(modifier = Modifier.height(16.dp))
            StyledButton(
                text = "Got it!",
                modifier = goBackButtonModifier,
                textStyle = buttonTextBlackStyle,
                onClick = {
                    viewModel.resetError()
                    onBackwardClick()
                }
            )
        }

    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(GlobalDimensions.DefaultPadding),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Thank you for your order!",
            style = MaterialTheme.typography.titleLarge,
        )

        Spacer(modifier = Modifier.height(16.dp))

        Column(
            modifier = Modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(12.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            StyledButton(
                text = "Go to Order Status",
                modifier = detailButtonModifier,
                textStyle = buttonTextWhiteStyle,
                onClick = { onOrderStatusClick() },
            )

            StyledButton(
                text = "Back",
                modifier = goBackButtonModifier,
                textStyle = buttonTextBlackStyle,
                onClick = { onBackwardClick() },
            )
        }
    }


}
