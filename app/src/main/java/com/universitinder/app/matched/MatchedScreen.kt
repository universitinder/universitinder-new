package com.universitinder.app.matched

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import compose.icons.FeatherIcons
import compose.icons.feathericons.MessageSquare

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MatchedScreen(matchedViewModel: MatchedViewModel) {
    Scaffold (
        topBar = {
            TopAppBar(
                title = { Text(text = "Matched") },
                navigationIcon = { IconButton(onClick = { matchedViewModel.popActivity() }) {
                    Icon(Icons.Filled.ArrowBack, contentDescription = "Go Back")
                } }
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = matchedViewModel::startFAQActivity,
                shape = CircleShape
            ) {
                Icon(FeatherIcons.MessageSquare, contentDescription = "Message")
            }
        }
    ){ innerPadding ->
        LazyColumn(
            modifier = Modifier.padding(innerPadding)
        ){
        }
    }
}