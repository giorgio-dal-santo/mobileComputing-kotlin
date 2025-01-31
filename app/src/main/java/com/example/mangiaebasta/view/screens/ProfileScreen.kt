package com.example.mangiaebasta.view.screens

import android.util.Log
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.outlined.AccountCircle
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.mangiaebasta.view.styles.GlobalDimensions
import com.example.mangiaebasta.view.styles.buttonTextWhiteStyle
import com.example.mangiaebasta.view.styles.orderButtonModifier
import com.example.mangiaebasta.view.styles.signUpButtonModifier
import com.example.mangiaebasta.view.utils.ErrorDialog
import com.example.mangiaebasta.view.utils.Header
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


    Log.d("ProfileScreen", "ISREGISTRED in Profile = ${userState.isUserRegistered}")

    if (userState.isUserRegistered == false) {
        return Column {
            Header("Profile")
            IsNotRegistered(onEditClick)
        }
    }

    val user = userState.user

    Column {
        Header("Profile")

        if (user != null) {
            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally // Centra orizzontalmente
            ) {
                Icon(
                    imageVector = Icons.Filled.AccountCircle, // Icona stile "person-circle-outline"
                    contentDescription = "Profile Icon",
                    modifier = Modifier.size(64.dp), // Ingrandisce l'icona
                )
                Spacer(modifier = Modifier.height(8.dp)) // Aggiunge spazio tra icona e testo
                Text(
                    text = "${user.firstName} ${user.lastName}",
                    style = MaterialTheme.typography.titleLarge, // Testo più grande
                    textAlign = TextAlign.Center // Centra il testo
                )
                Spacer(modifier = Modifier.height(16.dp)) // Aggiunge spazio tra testo e card
                ProfileCard(user, onEditClick)
            }
        }

        Text(
            text = "Last Order:",
            style = MaterialTheme.typography.titleLarge
        )

        return if (orderState.lastOrderMenu == null) {
            Column {
                Text("No order yet", style = MaterialTheme.typography.titleSmall)

                Spacer(modifier = Modifier.height(GlobalDimensions.DefaultPadding))

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
        Text("User not registered", style = MaterialTheme.typography.titleSmall)
    }

    StyledButton(
        text = "New Profile",
        modifier = signUpButtonModifier,
        textStyle = buttonTextWhiteStyle,
        onClick = { onEditClick() },
    )
}


