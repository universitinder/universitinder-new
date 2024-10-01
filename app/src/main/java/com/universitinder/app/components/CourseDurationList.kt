package com.universitinder.app.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Checkbox
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

data class CourseDurationMap(
    val title: String,
    val clicked: Boolean,
    val onChange: () -> Unit,
)

@Composable
fun CourseDurationList(padding: PaddingValues, courseDurationMapList: List<CourseDurationMap>) {
    LazyColumn(
        modifier = Modifier.padding(padding)
    ){
        items(courseDurationMapList.size) {
            CourseDurationItem(courseDurationMap = courseDurationMapList[it])
        }
    }
}

@Composable
fun CourseDurationItem(courseDurationMap: CourseDurationMap) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(10.dp)
            .border(
                1.dp,
                if (courseDurationMap.clicked) MaterialTheme.colorScheme.primary else Color.LightGray,
                CircleShape
            )
            .clip(CircleShape)
            .clickable { courseDurationMap.onChange() }
            .background(if (courseDurationMap.clicked) MaterialTheme.colorScheme.primaryContainer else Color.Transparent),
        contentAlignment = Alignment.Center,
    ) {
        Row(
            modifier = Modifier.fillMaxSize().padding(10.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ){
            Text(text = courseDurationMap.title)
            Checkbox(checked = courseDurationMap.clicked, onCheckedChange = {})
        }
    }
}