package com.universitinder.app.filters.affordability

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.universitinder.app.components.AffordabilityIndicator

@Composable
fun AffordabilityFilterScreen(
    minimum: Int,
    onMinimumChange: (newVal: String) -> Unit,
    maximum: Int,
    onMaximumChange: (newVal: String) -> Unit,
    affordability: Int,
) {
    Column(
        modifier = Modifier.padding(20.dp).fillMaxSize()
    ){
        OutlinedTextField(
            singleLine = true,
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 10.dp),
            label = { Text(text = "Minimum Price Range") },
            value = minimum.toString(),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            onValueChange = onMinimumChange
        )
        OutlinedTextField(
            singleLine = true,
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 10.dp),
            label = { Text(text = "Maximum Price Range") },
            value = maximum.toString(),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            onValueChange = onMaximumChange
        )

        Column {
            Text(text = "Affordability")
            AffordabilityIndicator(affordability = affordability)
        }
    }
}