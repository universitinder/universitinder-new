package com.universitinder.app.home

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.universitinder.app.components.SwipeableCard
import compose.icons.FeatherIcons
import compose.icons.feathericons.Filter

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
                    contentAlignment = Alignment.TopCenter
                ) {
                    if (uiState.currentIndex < uiState.schools.size) {
                        SwipeableCard(
                            school = uiState.schools[uiState.currentIndex],
                            onSwipedLeft = { homeViewModel.onSwipeLeft(uiState.schools[uiState.currentIndex].id) },
                            onSwipedRight = { homeViewModel.onSwipeRight(school = uiState.schools[uiState.currentIndex]) },
                            onMiddleClick = { homeViewModel.startSchoolProfileActivity(uiState.schools[uiState.currentIndex]) }
                        )
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
                        }
                    }
                    Row(
                        modifier = Modifier.fillMaxWidth().padding(top = 12.dp, end = 10.dp),
                        horizontalArrangement = Arrangement.End
                    ) {
                        Column(
                            modifier = Modifier.clickable { homeViewModel.startFilterActivity() },
                            horizontalAlignment = Alignment.CenterHorizontally
                        ){
                            Icon(FeatherIcons.Filter, contentDescription = "Filter", tint = Color.White)
                            Text(text = "Filters", fontSize = 12.sp, color = Color.White)
                        }
                    }
                }
            }
        }
    }
}