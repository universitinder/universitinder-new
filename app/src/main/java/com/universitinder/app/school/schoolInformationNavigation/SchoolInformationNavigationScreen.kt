package com.universitinder.app.school.schoolInformationNavigation

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.ListItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SchoolInformationNavigationScreen(viewModel: SchoolInformationNavigationViewModel) {
    Scaffold(
        topBar = {
            TopAppBar(title = { Text(text = "Institution Menu") })
        }
    ){ innerPadding ->
        LazyColumn(
            modifier = Modifier.padding(innerPadding)
        ){
            item {
                ListItem(
                    modifier = Modifier.clickable { viewModel.startSchoolInformationActivity() },
                    headlineContent = { Text(text = "Information") },
                    trailingContent = { Icon(Icons.Filled.KeyboardArrowRight, "Arrow Right") }
                )
            }
            item {
                ListItem(
                    modifier = Modifier.clickable { viewModel.startSchoolCoursesActivity() },
                    headlineContent = { Text(text = "Courses") },
                    trailingContent = { Icon(Icons.Filled.KeyboardArrowRight, "Arrow Right") }
                )
            }
            item {
                ListItem(
                    modifier = Modifier.clickable { viewModel.startSchoolImagesActivity() },
                    headlineContent = { Text(text = "Images") },
                    trailingContent = { Icon(Icons.Filled.KeyboardArrowRight, "Arrow Right") }
                )
            }
        }
    }
}