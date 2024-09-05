package com.universitinder.app.home

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
fun HomeScreen(homeViewModel: HomeViewModel) {
    Scaffold(
        topBar = {  },
    ){ innerPadding ->
        Column(modifier = Modifier.padding(innerPadding)){
            Text(text = "Hello Home")
        }
    }
}