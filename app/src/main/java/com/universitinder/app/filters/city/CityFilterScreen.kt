package com.universitinder.app.filters.city

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Button
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CityFilterScreen(cityFilterViewModel: CityFilterViewModel) {
    val uiState by cityFilterViewModel.uiState.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                navigationIcon = { IconButton(onClick = { cityFilterViewModel.popActivity() }) {
                    Icon(Icons.Filled.ArrowBack, contentDescription = "Go Back")
                }
                },
                title = { Text(text = "Cities/Municipalities Filter") }
            )
        },
        bottomBar = {
            BottomAppBar {
                Button(modifier = Modifier.fillMaxWidth(), onClick = cityFilterViewModel::save) {
                    if (uiState.loading) {
                        CircularProgressIndicator(color = MaterialTheme.colorScheme.onPrimary)
                    } else {
                        Text(text = "Save")
                    }
                }
            }
        }
    ){ innerPadding ->
        LazyColumn(
            modifier = Modifier.padding(innerPadding)
        ){
            items(uiState.cities) {province ->
                ListItem(
                    modifier = Modifier.clickable { cityFilterViewModel.onCheckChange(province) },
                    headlineContent = { Text(text = province) },
                    trailingContent = { Checkbox(checked = uiState.checkedCities.contains(province), onCheckedChange = { cityFilterViewModel.onCheckChange(province) }) }
                )
            }
        }
    }
}