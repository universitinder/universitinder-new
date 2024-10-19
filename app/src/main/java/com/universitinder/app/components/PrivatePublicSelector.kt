package com.universitinder.app.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun PrivatePublicSelector(
    isPrivate: Boolean,
    isPublic: Boolean,
    toggle: () -> Unit
) {
    Column(
        modifier = Modifier.padding(vertical = 10.dp)
    ) {
        Text(text = "Private/Public")
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 4.dp)
                .clickable { toggle() }
        ){
            Row(
                verticalAlignment = Alignment.CenterVertically
            ){
//                Text(text = isPrivate.toString())
//                Text(text = isPublic.toString())
                Text(text = if (isPrivate && !isPublic) "PRIVATE" else "PUBLIC")
            }
        }
    }
}