package com.example.mangiaebasta.view.screens

import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
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
import com.example.mangiaebasta.view.utils.button.StyledButton
import com.example.mangiaebasta.view.utils.cards.MenuCard
import com.example.mangiaebasta.view.utils.cards.ProfileCard
import com.example.mangiaebasta.viewmodel.MainViewModel

@Composable
fun ProfileScreen(viewModel: MainViewModel, onEditClick: () -> Unit, onOrderNowCLick: () -> Unit) {
    val appState by viewModel.appState.collectAsState()
    val userState by viewModel.userState.collectAsState()
    val orderState by viewModel.lastOrderState.collectAsState()

    val scrollState = rememberScrollState()

    if (appState.isLoading) {
        return Column {
            Text("Loading...")
        }
    }

    LaunchedEffect(Unit) {
        viewModel.fetchLastOrderDetail()
    }

    Log.d("ProfileScreen", "ISREGISTRED in Profile = ${userState.isUserRegistered}")

    if (!userState.isUserRegistered) {
        return Column {
            IsNotRegistered(onEditClick)
        }
    }

    val user = userState.user

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(scrollState)
            .padding(GlobalDimensions.DefaultPadding)
    ) {
        if (user != null) {
            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Spacer(modifier = Modifier.height(16.dp))
                Icon(
                    imageVector = Icons.Filled.AccountCircle,
                    contentDescription = "Profile Icon",
                    modifier = Modifier.size(64.dp),
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "${user.firstName} ${user.lastName}",
                    style = MaterialTheme.typography.titleLarge,
                    textAlign = TextAlign.Center
                )
                Spacer(modifier = Modifier.height(16.dp))
                ProfileCard(user, onEditClick)
            }
        }

        if (orderState.lastOrderMenu == null) {

            Box(
                modifier = Modifier
                    .fillMaxSize()
            ) {
                Column(
                    modifier = Modifier
                        .align(Alignment.Center)
                        .fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally
                )

                {
                    Spacer(modifier = Modifier.height(16.dp))

                    Text(
                        text = "It seems like you haven’t placed any orders yet.",
                        style = MaterialTheme.typography.titleSmall,
                        textAlign = TextAlign.Center
                    )

                    Spacer(modifier = Modifier.height(GlobalDimensions.DefaultPadding))

                    StyledButton(
                        text = "Order now",
                        modifier = orderButtonModifier,
                        textStyle = buttonTextWhiteStyle,
                        onClick = { onOrderNowCLick() }
                    )
                }
            }
        } else {
            Column(
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = "Last Order:",
                    style = MaterialTheme.typography.titleLarge
                )
                MenuCard(orderState.lastOrderMenu!!)
            }
        }
    }
}

@Composable
fun IsNotRegistered(onEditClick: () -> Unit) {

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(
                top = 25.dp,
                start = 16.dp,
                end = 16.dp,
                bottom = 12.dp
            ),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Row {
            Text(
                text = "Complete your profile",
                style = MaterialTheme.typography.titleLarge,
            )
        }

        Row {
            Text(
                text = "To access your profile and manage your orders, please sign up.",
                style = MaterialTheme.typography.titleSmall,
                modifier = Modifier.padding(bottom = 16.dp)
            )
        }

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            StyledButton(
                text = "New Profile",
                modifier = signUpButtonModifier,
                textStyle = buttonTextWhiteStyle,
                onClick = { onEditClick() },
            )
        }
    }
}