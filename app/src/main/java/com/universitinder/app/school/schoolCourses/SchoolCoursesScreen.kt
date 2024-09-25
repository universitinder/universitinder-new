package com.universitinder.app.school.schoolCourses

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ListItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.universitinder.app.models.Course

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SchoolCoursesScreen(schoolCoursesViewModel: SchoolCoursesViewModel) {
    val uiState by schoolCoursesViewModel.uiState.collectAsState()

    Scaffold(
        floatingActionButton = {
            FloatingActionButton(
                onClick = schoolCoursesViewModel::startCreateCourseActivity,
                shape = CircleShape
            ) {
                Icon(Icons.Filled.Add, contentDescription = "Create")
            }
        },
        topBar = {
            TopAppBar(
                navigationIcon = {
                    IconButton(onClick = { schoolCoursesViewModel.popActivity() }) {
                        Icon(Icons.Filled.ArrowBack, "Go Back")
                    }
                },
                title = { Text(text = "Courses") }
            )
        }
    ){ innerPadding ->
        when (uiState.fetchingLoading) {
            true -> {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator()
                }
            }
            false -> {
                when (uiState.courses.size) {
                    0 -> {
                        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                            Text(text = "No Courses Available")
                        }
                    }
                    else -> {
                        LazyColumn(
                            modifier = Modifier
                                .padding(innerPadding)
                                .fillMaxWidth()
                        ){
                            itemsIndexed(uiState.courses) { index, document ->
                                val course = document.toObject(Course::class.java)
                                if (course != null) {
                                    ListItem(
                                        modifier = Modifier.clickable { schoolCoursesViewModel.startEditCourseActivity(documentID = document.id) },
                                        leadingContent = { Text(text = (index+1).toString()) },
                                        headlineContent = { Text(text = course.name) },
                                        trailingContent = { Icon(Icons.Filled.KeyboardArrowRight, contentDescription = "Go Right") }
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}