package com.universitinder.app.school.list

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.ListItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.universitinder.app.models.School


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SchoolListScreen(schoolListViewModel: SchoolListViewModel) {
    val uiState by schoolListViewModel.uiState.collectAsState()

    Scaffold (
        topBar = {
            TopAppBar(title = { Text(text = "Schools") })
        },
        floatingActionButton = {
            FloatingActionButton(onClick = { schoolListViewModel.startRegisterSchool() }) {
                Icon(Icons.Filled.Add, contentDescription = "Add School")
            }
        }
    ){ innerPadding ->
        when (uiState.fetchingLoading) {
            true -> {
                Box(modifier = Modifier
                    .padding(innerPadding)
                    .fillMaxSize(), contentAlignment = Alignment.Center ) {
                    CircularProgressIndicator()
                }
            }
            else -> {
                LazyColumn(
                    modifier = Modifier.padding(innerPadding)
                ){
                    itemsIndexed(uiState.schools) { index, it ->
                        val schoolObject = it.toObject(School::class.java)
                        if (schoolObject != null) {
                            ListItem(
                                modifier = Modifier.clickable { schoolListViewModel.startSchoolActivity(schoolObject) },
                                leadingContent = { Text(text = "${index+1}") },
                                headlineContent = { Text(text = schoolObject.name) },
                                supportingContent = { Text(text = schoolObject.email) }
                            )
                        }
                    }
                }
            }
        }
    }
}