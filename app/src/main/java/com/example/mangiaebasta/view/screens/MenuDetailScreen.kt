package com.example.mangiaebasta.view.screens

import android.widget.Button
import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.navigation.NavController
import com.example.mangiaebasta.viewmodel.MainViewModel

@Composable
fun MenuDetailScreen(viewModel: MainViewModel, onForwardClick: () -> Unit, onBackwardClick: () -> Unit) {
    Column {
        Text("Menu Detail Screen")
        Button(onClick = {onForwardClick()}) {
            Text("Buy")
        }
        Button(onClick = {onBackwardClick()}) {
            Text("Back")
        }
    }
}
