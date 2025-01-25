package com.example.mangiaebasta.view.screens

import android.util.Log
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.mangiaebasta.model.dataSource.CommunicationController
import com.example.mangiaebasta.viewmodel.MainViewModel

@Composable
fun ProfileScreen(viewModel: MainViewModel, onEditClick: () -> Unit) {




    val appState by viewModel.appState.collectAsState()
    val userState by viewModel.userState.collectAsState()
    val menusState by viewModel.menusExplorationState.collectAsState()







    Log.d("ProfileScreen", "ISREGISTRED in Profile = ${userState.isUserRegistered}")

    if (userState.isUserRegistered == false) {
        return Column {
            Text("Profile Screen")
            IsNotRegistered(onEditClick)
        }
    }


    Column  {
        Text("Profile Screen")

        Text("Card full name: ${userState.user?.cardFullName}")
        Text("Card number: ${userState.user?.cardNumber}")
        Text("Expire Date: ${userState.user?.cardExpireMonth}/${userState.user?.cardExpireYear}")
        Text("CVV: ${userState.user?.cardCVV}")

        Button(onClick = {onEditClick()}) {
            Text("Edit Profile" )
        }

        Text("Last Order:")

    }
}

 @Composable
 fun IsNotRegistered(onEditClick: () -> Unit) {
    Column {
        Text("User not registered")
    }
     Button(onClick = {onEditClick()}) {
         Text("New Profile" )
     }
}


