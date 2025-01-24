package com.example.mangiaebasta.view.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.navigation.NavController

@Composable
fun HomeScreen(onMenuClick: () -> Unit) {
    Column {
        Text("Home Screen")
        Button(onClick = { onMenuClick() }) {
            Text("Detail")
        }
    }
}
