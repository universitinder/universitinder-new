package com.universitinder.app.home

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.PermissionStatus
import com.google.accompanist.permissions.rememberPermissionState
import com.universitinder.app.components.SwipeableSchoolCard
import compose.icons.FeatherIcons
import compose.icons.feathericons.Filter
import compose.icons.feathericons.RefreshCcw

@OptIn(ExperimentalPermissionsApi::class, ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(homeViewModel: HomeViewModel) {
    val uiState by homeViewModel.uiState.collectAsState()
    val fineLocationPermissionState = rememberPermissionState(
        android.Manifest.permission.ACCESS_FINE_LOCATION,
        onPermissionResult = {
            if (it) homeViewModel.refresh()
        }
    )

    LaunchedEffect(uiState.currentIndex) {
        if (uiState.schoolsTwo.isNotEmpty() && uiState.currentIndex < uiState.schoolsTwo.size) {
            homeViewModel.fetchImages(uiState.schoolsTwo[uiState.currentIndex].documentID)
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(text = "Home") },
                actions = {
                    Column(
                        modifier = Modifier
                            .padding(horizontal = 8.dp)
                            .clickable { homeViewModel.refresh() },
                        horizontalAlignment = Alignment.CenterHorizontally
                    ){
                        Icon(FeatherIcons.RefreshCcw, contentDescription = "Refresh")
                        Text(text = "Refresh", fontSize = 12.sp)
                    }
                    Column(
                        modifier = Modifier
                            .padding(horizontal = 8.dp)
                            .clickable { homeViewModel.startFilterActivity() },
                        horizontalAlignment = Alignment.CenterHorizontally
                    ){
                        Icon(FeatherIcons.Filter, contentDescription = "Filter")
                        Text(text = "Filters", fontSize = 12.sp)
                    }
                }
            )
        },
    ){ innerPadding ->
        when (fineLocationPermissionState.status) {
            PermissionStatus.Granted -> {
                LaunchedEffect(Unit) {
                    homeViewModel.startLocationUpdates()
                }
                when (uiState.fetchingLoading) {
                    true -> {
                        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                            CircularProgressIndicator()
                        }
                    }
                    false -> {
                        Box(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(innerPadding),
                            contentAlignment = Alignment.TopCenter
                        ) {
                            if (uiState.middleClickLoading) {
                                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                                    CircularProgressIndicator()
                                }
                            }

                            if (uiState.currentIndex < uiState.schoolsTwo.size) {
                                uiState.schoolsTwo.forEachIndexed{ index, school ->
                                    // Show SwipeableCard Component
                                    SwipeableSchoolCard(
                                        index = index,
                                        currentCardIndex = uiState.currentIndex,
                                        school = school,
                                        onSwipedLeft = { homeViewModel.onSwipeLeft(school.documentID) },
                                        onSwipedRight = { homeViewModel.onSwipeRightTwo(school) },
                                        images = uiState.images[uiState.currentIndex],
                                        onMiddleClick = { homeViewModel.startSchoolProfileActivityTwo(school) }
                                    )
                                }
                            } else {
                                Column(
                                    modifier = Modifier.fillMaxSize(),
                                    horizontalAlignment = Alignment.CenterHorizontally,
                                    verticalArrangement = Arrangement.Center
                                ){
                                    Text(text = "No More Institutions to show!", fontSize = 16.sp, textAlign = TextAlign.Center)
                                    FilledTonalButton(modifier = Modifier.padding(top = 12.dp), onClick = homeViewModel::refresh) {
                                        Text(text = "Refresh")
                                    }
                                    TextButton(onClick = homeViewModel::startFilterActivity) {
                                        Text(text = "Filters")
                                    }
                                }
                            }
                        }
                    }
                }
            }
            else -> {
                Column(
                    modifier = Modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(text = "Location Permission Required to access universities", textAlign = TextAlign.Center)
                    Button(modifier = Modifier.padding(top=12.dp), onClick = { fineLocationPermissionState.launchPermissionRequest() }) {
                        Text(text = "Request Location Permission")
                    }
                }
            }
        }
    }
}