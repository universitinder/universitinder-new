package com.universitinder.app.home

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.universitinder.app.components.SwipeableCard

@Composable
fun HomeScreen(homeViewModel: HomeViewModel) {
    val uiState by homeViewModel.uiState.collectAsState()

    Scaffold(
        topBar = {  },
    ){ innerPadding ->
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
                    contentAlignment = Alignment.Center
                ) {
                    if (uiState.currentIndex < uiState.schools.size) {
                        SwipeableCard(
                            school = uiState.schools[uiState.currentIndex],
                            onSwipedLeft = homeViewModel::onSwipeLeft,
                            onSwipedRight = homeViewModel::onSwipeRight,
                            onMiddleClick = { homeViewModel.startSchoolProfileActivity(uiState.schools[uiState.currentIndex]) }
                        )
                    } else {
                        Text(text = "No More Institutions to show!", fontSize = 24.sp, fontWeight = FontWeight.Bold)
                    }
                }
            }
        }
    }
}