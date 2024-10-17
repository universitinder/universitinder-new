package com.universitinder.app.matches

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.ListItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
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
                Box(
                    modifier = Modifier
                        .padding(innerPadding)
                        .fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    LazyColumn(
                        modifier = Modifier.fillMaxSize()
                    ){
                        itemsIndexed(uiState.matches) { index, school ->
                            var expandMenu by remember { mutableStateOf(false) }

                            ListItem(
                                modifier = Modifier.clickable { matchesViewModel.startMatchedSchool(school) },
                                leadingContent = { Text(text = (index+1).toString()) },
                                headlineContent = { Text(text = school)},
                                trailingContent = {
                                    Icon(
                                        Icons.Filled.MoreVert,
                                        contentDescription = "More",
                                        modifier = Modifier.clickable { expandMenu = !expandMenu }
                                    )

                                    DropdownMenu(
                                        expanded = expandMenu,
                                        onDismissRequest = { expandMenu = !expandMenu }) {
                                        DropdownMenuItem(
                                            text = { Text(text = "Remove") },
                                            onClick = { matchesViewModel.removeMatch(school) }
                                        )
                                    }
                                }
                            )
                        }
                    }
                    if (uiState.matchClickLoading) {
                        CircularProgressIndicator()
                    }
                }
            }
        }
    }
}
