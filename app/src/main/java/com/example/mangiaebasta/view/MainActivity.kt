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
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import androidx.navigation.NavHostController
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
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
    private val Context.dataStore by preferencesDataStore(name = "appStatus")

    private lateinit var preferencesController: PreferencesController

    private lateinit var navController: NavHostController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val apiController = CommunicationController()
        val dbController = DBController(this)
        preferencesController = PreferencesController.getInstance(dataStore)

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
        // Recupera l'ultima schermata salvata
        lifecycleScope.launch {
            val lastScreen = preferencesController.getLastScreen()
            setContent {
                MangiaEBastaTheme {
                    navController = rememberNavController()
                    MainScreen(
                        viewModel,
                        navController,
                        lastScreen
                    )
                }
            }
        }

    }

    //On pause x salvataggio schermate
    // Metodo per salvare l'intero stack di navigazione
    override fun onPause() {
        super.onPause()
        lifecycleScope.launch {
            val currentRoute =
                navController.currentDestination?.route  // Ottieni la route attuale
            val stackRoute = when (currentRoute) {
                "profile", "edit_profile" -> "profile_stack"
                "home", "menu_detail/{menuId}", "order_confirm" -> "home_stack"
                "order" -> "order"
                else -> "home_stack"  // Default di sicurezza
            }
            preferencesController.setLastScreen(stackRoute)  // Salva la route completa
            Log.d("MainActivity", "Current Screen Saved is: $stackRoute")
        }
    }

}


@Composable
fun MainScreen(
    viewModel: MainViewModel,
    navController: NavHostController,
    startDestination: String
) {
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
            modifier = Modifier
                .padding(innerPadding)
                .padding(16.dp),
            viewModel = viewModel,
            startDestination = startDestination  // 🔹 Passiamo la destinazione iniziale
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


    NavigationBar(
        modifier = Modifier.background(Color.White)
    ) {
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
                        contentDescription = screen.title,
                        modifier = Modifier.size(35.dp)
                    )
                },
                label = null
            )
        }
    }
}

