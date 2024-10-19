package com.universitinder.app.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun SchoolBasicInfo(label: String, value: String) {
    Column(
        modifier = Modifier.padding(vertical = 8.dp)
    ){
        Text(text = label, fontSize = 12.sp)
        Text(text = value, fontSize = 16.sp)
    }
}

@Composable
fun SchoolBasicInfoComposable(label: String, component: @Composable () -> Unit) {
    Column (
        modifier = Modifier.padding(vertical = 8.dp)
    ) {
        Text(text = label, fontSize = 12.sp)
        component()
    }
}