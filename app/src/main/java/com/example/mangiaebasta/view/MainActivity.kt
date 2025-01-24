package com.example.mangiaebasta.view

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.mangiaebasta.view.navigation.MainNavigator
import com.example.mangiaebasta.view.navigation.NavigationItem
import com.example.mangiaebasta.view.styles.MangiaEBastaTheme
import androidx.compose.material3.*
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MangiaEBastaTheme {
                MainScreen()
            }
        }
    }
}

@Composable
fun MainScreen() {
    val navController = rememberNavController()
    Scaffold(
        modifier = Modifier.fillMaxSize(),
        bottomBar = { BottomNavigationBar(navController) }
    ) { innerPadding ->
        MainNavigator(
            navController = navController,
            modifier = Modifier.padding(innerPadding)
        )
    }
}

@Composable
fun BottomNavigationBar(navController: NavController) {
    val items = listOf(
        NavigationItem.HomeStack,
        NavigationItem.Order,
        NavigationItem.ProfileStack
    )

    var navBarState by rememberSaveable {
        mutableIntStateOf(0)
    }

    NavigationBar {
        items.forEachIndexed { index, screen ->
            NavigationBarItem(
                selected = navBarState == index,
                onClick = {
                    navBarState = index
                    when(index){
                        0 -> navController.navigate("home_stack")
                        1 -> navController.navigate("order")
                        2 -> navController.navigate("profile_stack")
                    }
                },
                icon = {
                    Icon(
                        imageVector = if (navBarState == index) screen.selectedIcon
                        else screen.unselectedIcon,
                        contentDescription = screen.title
                    )
                },
                label = { Text(screen.title) },
            )
        }
    }
}