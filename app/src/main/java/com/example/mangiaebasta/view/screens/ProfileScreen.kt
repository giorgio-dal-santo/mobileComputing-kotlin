package com.example.mangiaebasta.view.screens

import android.util.Log
import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import com.example.mangiaebasta.view.styles.buttonTextWhiteStyle
import com.example.mangiaebasta.view.styles.orderButtonModifier
import com.example.mangiaebasta.view.styles.signUpButtonModifier
import com.example.mangiaebasta.view.utils.ErrorDialog
import com.example.mangiaebasta.view.utils.button.StyledButton
import com.example.mangiaebasta.view.utils.cards.MenuCard
import com.example.mangiaebasta.view.utils.cards.ProfileCard
import com.example.mangiaebasta.viewmodel.MainViewModel

@Composable
fun ProfileScreen(viewModel: MainViewModel, onEditClick: () -> Unit, onOrderNowCLick: () -> Unit) {
    val appState by viewModel.appState.collectAsState()
    val userState by viewModel.userState.collectAsState()
    val orderState by viewModel.lastOrderState.collectAsState()

    if (appState.isLoading) {
        return Column {
            Text("Loading...")
        }
    }

    LaunchedEffect(Unit) {
        viewModel.fetchLastOrderDetail()
    }

    if (appState.error != null) {
        ErrorDialog(
            error = appState.error!!,
            onDismiss = {
                viewModel.resetError() // Resetta l'errore
                onEditClick()
            }

        )
    }

    Log.d("ProfileScreen", "ISREGISTRED in Profile = ${userState.isUserRegistered}")

    if (userState.isUserRegistered == false) {
        return Column {
            Text("Profile Screen")
            IsNotRegistered(onEditClick)
        }
    }

    val user = userState.user




    Column {
        Text("Profile Screen")

        if (user != null) {
            ProfileCard(user, onEditClick)
        }

        Text(
            text = "Last Order:",
            style = MaterialTheme.typography.titleSmall
        )

        return if (orderState.lastOrderMenu == null) {
            Column {
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
                MenuCard(orderState.lastOrderMenu!!)
            }
        }

    }
}

@Composable
fun IsNotRegistered(onEditClick: () -> Unit) {
    Column {
        Text("User not registered")
    }

    StyledButton(
        text = "New Profile",
        modifier = signUpButtonModifier,
        textStyle = buttonTextWhiteStyle,
        onClick = { onEditClick() },
    )
}


