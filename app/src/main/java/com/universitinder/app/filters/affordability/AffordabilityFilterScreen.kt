package com.universitinder.app.filters.affordability

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.*
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.universitinder.app.components.AffordabilityIndicator

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AffordabilityFilterScreen(
    minimum: String,
    onMinimumChange: (newVal: String) -> Unit,
    maximum: String,
    onMaximumChange: (newVal: String) -> Unit,
    affordability: Int
) {
    val priceRangeOptions = listOf("Custom", "5000", "10000", "15000", "20000", "25000", "50000", "75000", "100000")

    var selectedMinimum by remember { mutableStateOf(if (priceRangeOptions.contains(minimum)) minimum else "Custom") }
    var selectedMaximum by remember { mutableStateOf(if (priceRangeOptions.contains(maximum)) maximum else "Custom") }

    var customMinimum by remember { mutableStateOf(if (selectedMinimum == "Custom") minimum else "") }
    var customMaximum by remember { mutableStateOf(if (selectedMaximum == "Custom") maximum else "") }

    var expandedMinimum by remember { mutableStateOf(false) }
    var expandedMaximum by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .padding(20.dp)
            .fillMaxSize()
    ) {
        // Minimum Price Range Dropdown
        ExposedDropdownMenuBox(
            expanded = expandedMinimum,
            onExpandedChange = { expandedMinimum = !expandedMinimum }
        ) {
            TextField(
                value = selectedMinimum,
                onValueChange = { }, // Prevent direct input
                readOnly = true, // Prevent keyboard from showing
                label = { Text("Minimum Price Range") },
                trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expandedMinimum) },
                colors = ExposedDropdownMenuDefaults.textFieldColors(),
                modifier = Modifier
                    .menuAnchor()
                    .fillMaxWidth()
                    .padding(vertical = 10.dp)
            )

            ExposedDropdownMenu(
                expanded = expandedMinimum,
                onDismissRequest = { expandedMinimum = false }
            ) {
                priceRangeOptions.forEach { selectionOption ->
                    DropdownMenuItem(
                        text = { Text(selectionOption) },
                        onClick = {
                            selectedMinimum = selectionOption
                            expandedMinimum = false
                            if (selectionOption == "Custom") {
                                // Allow custom input if "Custom" is selected
                            } else {
                                customMinimum = "" // Reset if not "Custom"
                                onMinimumChange(selectionOption)
                            }
                        }
                    )
                }
            }
        }

        // Custom Input TextField for Minimum (show only when selectedMinimum is "Custom")
        if (selectedMinimum == "Custom") {
            TextField(
                value = customMinimum,
                onValueChange = {
                    customMinimum = it
                    onMinimumChange(it)
                },
                label = { Text("Custom Minimum Price") },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 10.dp)
            )
        }

        // Maximum Price Range Dropdown (similar structure to Minimum)
        ExposedDropdownMenuBox(
            expanded = expandedMaximum,
            onExpandedChange = { expandedMaximum = !expandedMaximum }
        ) {
            TextField(
                value = selectedMaximum,
                onValueChange = { },
                readOnly = true,
                label = { Text("Maximum Price Range") },
                trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expandedMaximum) },
                colors = ExposedDropdownMenuDefaults.textFieldColors(),
                modifier = Modifier
                    .menuAnchor()
                    .fillMaxWidth()
                    .padding(vertical = 10.dp)
            )

            ExposedDropdownMenu(
                expanded = expandedMaximum,
                onDismissRequest = { expandedMaximum = false }
            ) {
                priceRangeOptions.forEach { selectionOption ->
                    DropdownMenuItem(
                        text = { Text(selectionOption) },
                        onClick = {
                            selectedMaximum = selectionOption
                            expandedMaximum = false
                            if (selectionOption == "Custom") {
                                // Allow custom input if "Custom" is selected
                            } else {
                                customMaximum = ""
                                onMaximumChange(selectionOption)
                            }
                        }
                    )
                }
            }
        }

        // Custom Input TextField for Maximum (show only when selectedMaximum is "Custom")
        if (selectedMaximum == "Custom") {
            TextField(
                value = customMaximum,
                onValueChange = {
                    customMaximum = it
                    onMaximumChange(it)
                },
                label = { Text("Custom Maximum Price") },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 10.dp)
            )
        }

        // ... rest of your UI ...
        Column {
            Text("Affordability")
            AffordabilityIndicator(affordability = affordability)
        }
    }
}