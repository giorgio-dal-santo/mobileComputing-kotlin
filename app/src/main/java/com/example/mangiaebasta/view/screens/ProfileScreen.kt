package com.example.mangiaebasta.view.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.navigation.NavController

@Composable
fun ProfileScreen(onEditClick: () -> Unit) {
    Column  {
        Text("Profile Screen")
        Button(onClick = {onEditClick()}) {
            Text("Edit Profile")
        }
    }
}
