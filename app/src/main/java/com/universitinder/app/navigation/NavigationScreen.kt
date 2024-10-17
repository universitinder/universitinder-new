package com.universitinder.app.navigation

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.outlined.AccountCircle
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.universitinder.app.home.HomeScreen
import com.universitinder.app.matches.MatchesScreen
import com.universitinder.app.models.UserState
import com.universitinder.app.models.UserType
import com.universitinder.app.profile.ProfileScreen
import com.universitinder.app.school.list.SchoolListScreen
import compose.icons.FeatherIcons
import compose.icons.feathericons.Heart

@Composable
fun NavigationScreen(navigationViewModel: NavigationViewModel) {
    val uiState by navigationViewModel.uiState.collectAsState()
    val navController = rememberNavController()
    var selectedIndex by remember { mutableIntStateOf(1) }
    val currentUser = UserState.currentUser

    Scaffold (
        bottomBar = {
            BottomAppBar {
                NavigationBar {
                    when (uiState.user.type) {
                        UserType.STUDENT -> {
                            NavigationBarItem(
                                selected = selectedIndex == 0,
                                onClick = {
                                    selectedIndex = 0
                                    navController.navigate("Matches")
                                    navigationViewModel.matchesViewModel.refresh()
                                },
                                icon = { Icon(if (selectedIndex == 0) FeatherIcons.Heart else FeatherIcons.Heart, "Matches")},
                                label = { Text(text = "Matches") }
                            )
                            NavigationBarItem(
                                selected = selectedIndex == 1,
                                onClick = {
                                    if (selectedIndex == 1) {
                                        navigationViewModel.homeViewModel.refresh()
                                    } else {
                                        selectedIndex = 1
                                        navController.navigate("Home")
                                    }
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
                        UserType.INSTITUTION -> {
//                            NavigationBarItem(
//                                selected = selectedIndex == 0,
//                                onClick = {
//                                    selectedIndex = 0
//                                    navController.navigate("Institution")
//                                },
//                                icon = { Icon(if (selectedIndex == 0) Icons.Filled.List else Icons.Outlined.List, "Institution")},
//                                label = { Text(text = "Institution") }
//                            )
                            NavigationBarItem(
                                selected = selectedIndex == 1,
                                onClick = {
                                    selectedIndex = 1
                                    navController.navigate("Schools")
//                                    navigationViewModel.schoolViewModel.refresh()
                                    navigationViewModel.schoolListViewModel.refresh()
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
                        UserType.UNKNOWN -> {  }
                    }
                }
            }
        }
    ){ innerPadding ->
        Box(modifier = Modifier.padding(innerPadding)) {
            if (currentUser != null) {
                NavHost(
                    navController = navController,
                    startDestination = if (currentUser.type == UserType.INSTITUTION) "Schools" else "Home"
                ) {
                    composable("Matches") { MatchesScreen(matchesViewModel = navigationViewModel.matchesViewModel) }
//                    composable("Filters") { FiltersScreen(filtersViewModel = navigationViewModel.filtersViewModel) }
//                    composable("Institution") { SchoolInformationNavigationScreen(viewModel = navigationViewModel.schoolInformationNavigationViewModel) }
//                    composable("School") { SchoolScreen(schoolViewModel = navigationViewModel.schoolViewModel) }
                    composable("Schools") { SchoolListScreen(schoolListViewModel = navigationViewModel.schoolListViewModel) }
                    composable("Home") { HomeScreen(homeViewModel = navigationViewModel.homeViewModel) }
                    composable("Profile") { ProfileScreen(profileViewModel = navigationViewModel.profileViewModel) }
                }
            }
        }
    }
}