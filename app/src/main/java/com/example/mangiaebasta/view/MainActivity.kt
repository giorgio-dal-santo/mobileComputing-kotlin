package com.example.mangiaebasta.view

import android.content.Context
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
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

        val viewModelFactory = viewModelFactory {
            initializer {
                MainViewModel(
                    userRepository = userRepository,
                    menuRepository = menuRepository,
                    orderRepository = orderRepository,
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
                        "menu_detail",
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
                                    "menu_detail",
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

