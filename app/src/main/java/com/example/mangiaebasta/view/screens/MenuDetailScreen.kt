package com.example.mangiaebasta.view.screens

import android.widget.Button
import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.navigation.NavController
import com.example.mangiaebasta.view.utils.MenuHomePreviewDetailed
import com.example.mangiaebasta.viewmodel.MainViewModel

@Composable
fun MenuDetailScreen(viewModel: MainViewModel, onForwardClick: () -> Unit, onBackwardClick: () -> Unit, menuId: Int) {

    val appState by viewModel.appState.collectAsState()
    val menuState by viewModel.menusExplorationState.collectAsState()

    LaunchedEffect(menuId) {
        viewModel.fetchMenuDetail(menuId)
    }

    if(appState.isLoading || menuState.selectedMenu == null) {
        return Column {
            Text("Loading...")
        }
    }

    Column {
        Text("Menu Detail Screen")
        MenuHomePreviewDetailed(menuState.selectedMenu!!, onPress = {onForwardClick()})


        
        Button(onClick = {onBackwardClick()}) {
            Text("Back")
        }
    }
}
