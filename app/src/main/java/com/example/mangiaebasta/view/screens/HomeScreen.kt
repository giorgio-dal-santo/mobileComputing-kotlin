package com.example.mangiaebasta.view.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.navigation.NavController
import com.example.mangiaebasta.viewmodel.MainViewModel

@Composable
fun HomeScreen(
    viewModel: MainViewModel,
    onMenuClick: () -> Unit)
{

    val appState by viewModel.appState.collectAsState()
    val userState by viewModel.userState.collectAsState()
    val menusState by viewModel.menusExplorationState.collectAsState()

    Column {
        Text("Home Screen")
        Button(onClick = { onMenuClick() }) {
            Text("Detail")
        }

    }
}
