package com.example.mangiaebasta.view.screens

import android.Manifest
import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.util.Log
import android.net.Uri
import android.provider.Settings
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import androidx.core.app.ActivityCompat
import com.example.mangiaebasta.model.dataClasses.Error
import com.example.mangiaebasta.view.styles.buttonTextWhiteStyle
import com.example.mangiaebasta.view.styles.buyButtonModifier
import com.example.mangiaebasta.view.styles.enableLocationButtonModifier
import com.example.mangiaebasta.view.utils.ErrorDialog
import com.example.mangiaebasta.view.utils.Header
import com.example.mangiaebasta.view.utils.button.StyledButton
import com.example.mangiaebasta.view.utils.cards.MenuCardWithButton
import com.example.mangiaebasta.viewmodel.MainViewModel
import com.google.android.gms.location.LocationCallback

@Composable
fun HomeScreen(
    viewModel: MainViewModel,
    onMenuClick: (Int) -> Unit
) {

    val appState by viewModel.appState.collectAsState()
    val userState by viewModel.userState.collectAsState()
    val menusState by viewModel.menusExplorationState.collectAsState()
    val locationState by viewModel.locationState.collectAsState()

    if (!locationState.isLocationAllowed) {
        return Text("NO LOCATION ALLOWED, GO TO SETTINGS")
    }

    LaunchedEffect(menusState.reloadMenus, appState.isLoading, locationState.lastKnownLocation, locationState.isLocationAllowed) {
        if (appState.isLoading) return@LaunchedEffect
            viewModel.fetchNearbyMenus()
    }

    Column {
        Header("Mangia e Basta")
        Text(
            text = "Nearby Menus",
            style = MaterialTheme.typography.titleLarge
        )

        LazyColumn {
            items(menusState.nearbyMenus) { menu ->
                MenuCardWithButton(
                    menu = menu,
                    onPress = {
                        onMenuClick(menu.menu.mid) // Passa l'id del menu al click
                    }
                )
            }
        }


    }
}

fun showSettingsDialog(context: Context) {
    AlertDialog.Builder(context)
        .setTitle("Location services are disabled")
        .setMessage("Please enable location services to use this feature.")
        .setPositiveButton("Go go Settings") { _, _ ->
            val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS).apply {
                data = Uri.fromParts("package", context.packageName, null)
            }
            context.startActivity(intent)
        }
        .setNegativeButton("Cancel", null)
        .show()
}
