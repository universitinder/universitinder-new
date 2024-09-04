package com.universitinder.app.navigation

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.outlined.AccountCircle
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.List
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.universitinder.app.filters.FiltersScreen
import com.universitinder.app.home.HomeScreen
import com.universitinder.app.profile.ProfileScreen

@Composable
fun NavigationScreen(navigationViewModel: NavigationViewModel) {
    val navController = rememberNavController()
    var selectedIndex by remember { mutableIntStateOf(1) }

    Scaffold (
        bottomBar = {
            BottomAppBar {
                NavigationBar {
                    NavigationBarItem(
                        selected = selectedIndex == 0,
                        onClick = {
                            selectedIndex = 0
                            navController.navigate("Filters")
                        },
                        icon = { Icon(if (selectedIndex == 0) Icons.Filled.List else Icons.Outlined.List, "Filters")},
                        label = { Text(text = "Filters") }
                    )
                    NavigationBarItem(
                        selected = selectedIndex == 1,
                        onClick = {
                            selectedIndex = 1
                            navController.navigate("Home")
                        },
                        icon = { Icon(if (selectedIndex == 1) Icons.Filled.Home else Icons.Outlined.Home, "Home")},
                        label = { Text(text = "Home") }
                    )
                    NavigationBarItem(
                        selected = selectedIndex == 2,
                        onClick = {
                            selectedIndex = 2
                            navController.navigate("Profile")
                        },
                        icon = { Icon(if (selectedIndex == 2) Icons.Filled.AccountCircle else Icons.Outlined.AccountCircle, "Profile")},
                        label = { Text(text = "Profile") }
                    )
                }
            }
        }
    ){ innerPadding ->
        Box(modifier = Modifier.padding(innerPadding)) {
            NavHost(
                navController = navController,
                startDestination = "Home"
            ) {
                composable("Filters") { FiltersScreen() }
                composable("Home") { HomeScreen(homeViewModel = navigationViewModel.homeViewModel) }
                composable("Profile") { ProfileScreen() }
            }
        }
    }
}