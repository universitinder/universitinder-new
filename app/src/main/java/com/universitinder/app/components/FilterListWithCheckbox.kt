package com.universitinder.app.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Checkbox
import androidx.compose.material3.ListItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
fun FilterListWithCheckbox(
    values: List<String>,
    checkedValues: List<String>,
    onCheckChange: (item: String) -> Unit
) {
    LazyColumn(
        modifier = Modifier.fillMaxSize()
    ){
        items(values) {value ->
            ListItem(
                modifier = Modifier.clickable { onCheckChange(value) },
                headlineContent = { Text(text = value) },
                trailingContent = { Checkbox(checked = checkedValues.contains(value), onCheckedChange = { onCheckChange(value) }) }
            )
        }
    }
}