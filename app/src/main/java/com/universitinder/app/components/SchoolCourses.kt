package com.universitinder.app.components

import androidx.compose.material3.ListItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable

@Composable
fun SchoolCourses(courses: List<String>) {
    courses.map {
        ListItem(headlineContent = { Text(text = it) })
    }
}