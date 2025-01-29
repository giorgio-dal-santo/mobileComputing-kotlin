package com.example.mangiaebasta.view

import android.Manifest
import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.mangiaebasta.model.dataSource.CommunicationController
import com.example.mangiaebasta.model.dataSource.DBController
import com.example.mangiaebasta.model.dataSource.PreferencesController
import com.example.mangiaebasta.model.repository.MenuRepository
import com.example.mangiaebasta.model.repository.OrderRepository
import com.example.mangiaebasta.model.repository.UserRepository
import com.example.mangiaebasta.view.navigation.MainNavigator
import com.example.mangiaebasta.view.navigation.NavigationItem
import com.example.mangiaebasta.view.styles.MangiaEBastaTheme
import com.example.mangiaebasta.viewmodel.MainViewModel
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationServices

class MainActivity : ComponentActivity() {
    private val Context.dataStore by preferencesDataStore(name = "appStatus")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val apiController = CommunicationController()
        val dbController = DBController(this)
        val preferencesController = PreferencesController.getInstance(dataStore)

        val userRepository = UserRepository(
            communicationController = apiController,
            dbController = dbController,
            preferencesController = preferencesController
        )

        val menuRepository = MenuRepository(
            communicationController = apiController,
            dbController = dbController,
            preferencesController = preferencesController
        )

        val orderRepository = OrderRepository(
            communicationController = apiController,
            dbController = dbController,
            preferencesController = preferencesController
        )

        val locationClient = LocationServices.getFusedLocationProviderClient(this)

        val viewModelFactory = viewModelFactory {
            initializer {
                MainViewModel(
                    userRepository = userRepository,
                    menuRepository = menuRepository,
                    orderRepository = orderRepository,
                    locationClient = locationClient
                )
            }
        }
        val viewModel by viewModels<MainViewModel> { viewModelFactory }

        enableEdgeToEdge()

        setContent {
            MangiaEBastaTheme {
                MainScreen(viewModel)
            }
        }
    }
}

@Composable
fun MainScreen(viewModel: MainViewModel) {
    val navController = rememberNavController()
    val context = LocalContext.current

    val locationCallback = remember {
        object : LocationCallback() {
            override fun onLocationResult(locationResult: com.google.android.gms.location.LocationResult) {
                super.onLocationResult(locationResult)
                val location = locationResult.lastLocation
                if (location != null) {
                    Log.d("MainActivity", "Saved new Location $location")
                    viewModel.allowLocation(location)
                }
            }

            override fun onLocationAvailability(availability: com.google.android.gms.location.LocationAvailability) {
                super.onLocationAvailability(availability)
                if (!availability.isLocationAvailable) {
                    Log.w("MainActivity", "Location not available")
                }
            }
        }
    }
    val permissionLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        if (isGranted) {
            viewModel.subscribeToLocationUpdates(locationCallback)
            Log.d("MainActivity", "Permission granted: $isGranted")
        } else {
            Log.d("MainActivity", "Permission not granted: $isGranted")
        }
    }

    LaunchedEffect(Unit) {
        val hasPermission = viewModel.checkLocationPermission(context)
        if (!hasPermission) {
            Log.d("MainActivity", "Richiedo i permessi")
            permissionLauncher.launch(Manifest.permission.ACCESS_FINE_LOCATION)
        } else {
            viewModel.subscribeToLocationUpdates(locationCallback)
        }
    }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        bottomBar = { BottomNavigationBar(navController) } // Passa il NavController alla barra di navigazione
    ) { innerPadding ->
        MainNavigator(
            navController = navController,
            modifier = Modifier.padding(innerPadding),
            viewModel = viewModel
        )
    }
}

@Composable
fun BottomNavigationBar(navController: androidx.navigation.NavController) {
    val items = listOf(
        NavigationItem.HomeStack,
        NavigationItem.Order,
        NavigationItem.ProfileStack
    )

    // Stato corrente del back stack
    val currentBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = currentBackStackEntry?.destination?.route // Ottieni la route attuale


    NavigationBar {
        items.forEach { screen ->
            NavigationBarItem(
                selected = when (screen) {
                    is NavigationItem.HomeStack -> currentRoute in listOf(
                        "home",
                        "menu_detail/{menuId}",
                        "order_confirm"
                    )

                    is NavigationItem.Order -> currentRoute == "order"
                    is NavigationItem.ProfileStack -> currentRoute in listOf(
                        "profile",
                        "edit_profile"
                    )
                },
                onClick = {
                    if (currentRoute != screen.route) {
                        navController.navigate(screen.route) {
                            popUpTo(navController.graph.startDestinationId) {
                                saveState = true
                            }
                            launchSingleTop = true
                            restoreState = true
                        }
                    }
                },
                icon = {
                    Icon(
                        imageVector = if (when (screen) {
                                is NavigationItem.HomeStack -> currentRoute in listOf(
                                    "home",
                                    "menu_detail/{menuId}",
                                    "order_confirm"
                                )

                                is NavigationItem.Order -> currentRoute == "order"
                                is NavigationItem.ProfileStack -> currentRoute in listOf(
                                    "profile",
                                    "edit_profile"
                                )
                            }
                        ) screen.selectedIcon else screen.unselectedIcon,
                        contentDescription = screen.title
                    )
                },
                label = { Text(screen.title) }
            )
        }
    }
}

