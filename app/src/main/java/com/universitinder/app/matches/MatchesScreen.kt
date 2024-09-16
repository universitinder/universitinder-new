package com.universitinder.app.matches

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ListItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MatchesScreen(matchesViewModel: MatchesViewModel) {
    val uiState by matchesViewModel.uiState.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(title = { Text(text = "Matches") })
        }
    ){ innerPadding ->
        when (uiState.fetchingLoading) {
            true -> {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator()
                }
            }
            false -> {
                LazyColumn(
                    modifier = Modifier.padding(innerPadding)
                ){
                    itemsIndexed(uiState.matches) { index, school ->
                        ListItem(
                            leadingContent = { Text(text = (index+1).toString()) },
                            headlineContent = { Text(text = school)}
                        )
                    }
                }
            }
        }
    }
}
