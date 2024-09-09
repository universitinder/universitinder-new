package com.universitinder.app.school.schoolCourses.editCourse

import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.universitinder.app.models.EducationLevel
import com.universitinder.app.models.ResultMessageType

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditCourseScreen(editCourseViewModel: EditCourseViewModel) {
    val uiState by editCourseViewModel.uiState.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                navigationIcon = {
                    IconButton(onClick = { editCourseViewModel.popActivity() }) {
                        Icon(Icons.Filled.ArrowBack, contentDescription = "Go Back")
                    }
                },
                title = { Text(text = "Create Course") },
                actions = {
                    TextButton(
                        colors = ButtonDefaults.textButtonColors(
                            contentColor = MaterialTheme.colorScheme.error
                        ),
                        onClick = editCourseViewModel::onDeleteDialogToggle
                    ) {
                        Text(text = "Delete")
                    }
                }
            )
        }
    ){ innerPadding ->
        Box {
            LazyColumn(
                modifier = Modifier
                    .padding(innerPadding)
                    .padding(20.dp)
            ) {
                item {
                    OutlinedTextField(
                        label = { Text(text = "Name") },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 10.dp),
                        value = uiState.name,
                        onValueChange = editCourseViewModel::onNameChange
                    )
                }
                item {
                    OutlinedTextField(
                        label = { Text(text = "Duration in Years") },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 10.dp),
                        value = uiState.duration,
                        onValueChange = editCourseViewModel::onDurationChange,
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
                    )
                }
                item {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 10.dp),
                    ) {
                        Column {
                            Text(text = "Level of Education")
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .border(
                                        width = 0.5.dp,
                                        color = Color.Black,
                                        shape = CircleShape.copy(
                                            CornerSize(5.dp)
                                        )
                                    )
                                    .clickable { editCourseViewModel.onLevelMenuToggle() },
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    modifier = Modifier.padding(
                                        vertical = 16.dp,
                                        horizontal = 18.dp
                                    ),
                                    text = uiState.level.toString()
                                )
                                Icon(
                                    Icons.Filled.ArrowDropDown,
                                    contentDescription = "Dropdown Arrow"
                                )
                            }
                        }
                        DropdownMenu(
                            expanded = uiState.levelMenuExpanded,
                            onDismissRequest = editCourseViewModel::onLevelMenuToggle
                        ) {
                            EducationLevel.entries.forEach {
                                DropdownMenuItem(
                                    text = { Text(text = it.toString()) },
                                    onClick = { editCourseViewModel.onLevelChange(it.toString()) })
                            }
                        }
                    }
                }
                item {
                    if (uiState.resultMessage.show)
                        Text(
                            modifier = Modifier.padding(top = 10.dp),
                            text = uiState.resultMessage.message,
                            color = if (uiState.resultMessage.type == ResultMessageType.FAILED) Color.Red else MaterialTheme.colorScheme.primary,
                            textAlign = TextAlign.Center,
                            fontSize = 12.sp
                        )
                }
                item {
                    Button(
                        modifier = Modifier.fillMaxWidth(),
                        onClick = editCourseViewModel::updateCourse
                    ) {
                        if (uiState.updateLoading) {
                            CircularProgressIndicator(color = MaterialTheme.colorScheme.onPrimary)
                        } else {
                            Text(text = "Update Course")
                        }
                    }
                }
            }

            if (uiState.showDeleteDialog) {
                Dialog(onDismissRequest = editCourseViewModel::onDeleteDialogToggle) {
                    Surface(
                        modifier = Modifier
                            .fillMaxWidth()
                            .wrapContentHeight(),
                        shape = MaterialTheme.shapes.medium
                    ) {
                        Column(
                            modifier = Modifier
                                .padding(16.dp)
                                .fillMaxWidth()
                        ) {
                            Text(text = "Delete Confirmation", style = MaterialTheme.typography.titleLarge)
                            Text(
                                text = "Are you sure you want to delete course named ${uiState.name}",
                                style = MaterialTheme.typography.bodyMedium,
                                modifier = Modifier.padding(vertical = 24.dp)
                            )
                            if (uiState.deleteResultMessage.show)
                                Text(
                                    modifier = Modifier.padding(top = 10.dp),
                                    text = uiState.deleteResultMessage.message,
                                    color = if (uiState.deleteResultMessage.type == ResultMessageType.FAILED) Color.Red else MaterialTheme.colorScheme.primary,
                                    textAlign = TextAlign.Center,
                                    fontSize = 12.sp
                                )
                            Row {
                                TextButton(
                                    modifier = Modifier
                                        .fillMaxWidth(0.5f)
                                        .padding(end = 5.dp),
                                    onClick = editCourseViewModel::onDeleteDialogToggle
                                ) {
                                    Text(text = "Cancel")
                                }
                                Button(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(start = 5.dp),
                                    onClick = editCourseViewModel::deleteCourse
                                ) {
                                    if (uiState.deleteLoading) {
                                        CircularProgressIndicator(modifier = Modifier.size(20.dp))
                                    } else {
                                        Text(text = "Delete")
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}