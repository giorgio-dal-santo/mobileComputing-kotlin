package com.example.mangiaebasta.view.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.navigation.NavController

@Composable
fun EditProfileScreen(onBackwardClick: () -> Unit) {
    Column {
        Text("Edit Profile Screen")
        Button(onClick = {onBackwardClick()}) {
            Text("Back")
        }
    }
}
