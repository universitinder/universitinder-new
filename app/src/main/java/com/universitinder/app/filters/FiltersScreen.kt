package com.universitinder.app.filters

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
fun FiltersScreen(filtersViewModel: FiltersViewModel) {
    Scaffold (
        topBar = {
            TopAppBar(
                title = { Text(text = "Filters") }
            )
        }
    ){ innerPadding ->
        LazyColumn(
            modifier = Modifier.padding(innerPadding)
        ){
            item {
                ListItem(
                    modifier = Modifier.clickable { filtersViewModel.startProvinceActivity() },
                    headlineContent = { Text(text = "Province") },
                    trailingContent = { Icon(Icons.Filled.KeyboardArrowRight, contentDescription = "Go Right") }
                )
            }
            item { 
                ListItem(
                    modifier = Modifier.clickable { filtersViewModel.startCityActivity() },
                    headlineContent = { Text(text = "City/Municipality") },
                    trailingContent = { Icon(Icons.Filled.KeyboardArrowRight, contentDescription = "Go Right") }
                )
            }
            item {
                ListItem(
                    modifier = Modifier.clickable { filtersViewModel.startAffordabilityActivity() },
                    headlineContent = { Text(text = "Affordability") },
                    trailingContent = { Icon(Icons.Filled.KeyboardArrowRight, contentDescription = "Go Right") }
                )
            }
            item {
                ListItem(
                    modifier = Modifier.clickable { filtersViewModel.startCoursesActivity() },
                    headlineContent = { Text(text = "Courses") },
                    trailingContent = { Icon(Icons.Filled.KeyboardArrowRight, contentDescription = "Go Right") }
                )
            }
        }
    }
}

/**
 * PROVINCE
 * CITY/MUNICIPALITY
 * AFFORDABILITY
 * COURSES
 */