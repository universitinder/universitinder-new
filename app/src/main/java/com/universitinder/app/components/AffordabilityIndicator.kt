package com.universitinder.app.components

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

@Composable
fun AffordabilityIndicator(affordability: Int) {
    Row {
        Text(text = "₱", fontWeight = FontWeight.SemiBold, color = if (affordability >= 1) MaterialTheme.colorScheme.primary else Color.LightGray)
        Text(
            modifier = Modifier.padding(horizontal = 8.dp),
            text = "₱", fontWeight = FontWeight.SemiBold, color = if (affordability >= 2) MaterialTheme.colorScheme.primary else Color.LightGray)
        Text(text = "₱", fontWeight = FontWeight.SemiBold, color = if (affordability == 3) MaterialTheme.colorScheme.primary else Color.LightGray)
    }
}