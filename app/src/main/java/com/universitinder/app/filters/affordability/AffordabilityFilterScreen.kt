package com.universitinder.app.filters.affordability

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.universitinder.app.components.AffordabilityIndicator

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AffordabilityFilterScreen(affordabilityFilterViewModel: AffordabilityFilterViewModel) {
    val uiState by affordabilityFilterViewModel.uiState.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                navigationIcon = { IconButton(onClick = { affordabilityFilterViewModel.popActivity() }) {
                    Icon(Icons.Filled.ArrowBack, contentDescription = "Go Back")
                }
                },
                title = { Text(text = "Affordability") }
            )
        },
        bottomBar = {
            BottomAppBar {
                Button(modifier = Modifier.fillMaxWidth(), onClick = affordabilityFilterViewModel::save) {
                    if (uiState.loading) {
                        CircularProgressIndicator(color = MaterialTheme.colorScheme.onPrimary)
                    } else {
                        Text(text = "Save")
                    }
                }
            }
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier.padding(innerPadding).padding(20.dp)
        ){
            OutlinedTextField(
                singleLine = true,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 10.dp),
                label = { Text(text = "Minimum Price Range") },
                value = uiState.minimum.toString(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                onValueChange = affordabilityFilterViewModel::onMinimumChange
            )
            OutlinedTextField(
                singleLine = true,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 10.dp),
                label = { Text(text = "Maximum Price Range") },
                value = uiState.maximum.toString(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                onValueChange = affordabilityFilterViewModel::onMaximumChange
            )

            Column {
                Text(text = "Affordability")
                AffordabilityIndicator(affordability = uiState.affordability)
            }
        }
    }
}