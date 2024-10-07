package com.universitinder.app.school.schoolCourses.editCourse

import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.universitinder.app.components.PopUpDropDown
import com.universitinder.app.components.dialogs.ConfirmDialog
import com.universitinder.app.models.COURSE_DURATION
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
                title = { Text(text = "Edit Course") },
                actions = {
                    IconButton(onClick = editCourseViewModel::onDeleteDialogToggle, colors = IconButtonDefaults.iconButtonColors(contentColor = Color.Red)) {
                        Icon(Icons.Filled.Delete, contentDescription = "Delete")
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
                    Box(modifier = Modifier
                        .padding(vertical = 10.dp)
                        .fillMaxWidth()) {
                        Column {
                            Text(text = "Duration in Years")
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
                                    .clickable { editCourseViewModel.onDurationMenuToggle() },
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    modifier = Modifier.padding(vertical = 16.dp, horizontal = 18.dp),
                                    text = uiState.duration
                                )
                                Icon(Icons.Filled.ArrowDropDown, contentDescription = "Dropdown Arrow")
                            }
                        }
                    }
                    PopUpDropDown(
                        label = "Duration in Years",
                        items = COURSE_DURATION.toList(),
                        show = uiState.durationMenuExpanded,
                        onDismissRequest = editCourseViewModel::onDurationMenuToggle,
                        onItemSelected = { editCourseViewModel.onDurationChange(it) }
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
                    }
                    PopUpDropDown(
                        label = "Level of Education",
                        items = EducationLevel.entries.map { it.toString() },
                        show = uiState.levelMenuExpanded,
                        onDismissRequest = editCourseViewModel::onLevelMenuToggle,
                        onItemSelected = { editCourseViewModel.onLevelChange(it) }
                    )
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

            ConfirmDialog(
                title = "Delete Confirmation",
                text = "Are you sure you want to delete course named ${uiState.name}",
                show = uiState.showDeleteDialog,
                onDismissRequest = editCourseViewModel::onDeleteDialogToggle,
                resultMessage = uiState.deleteResultMessage,
                onCancel = editCourseViewModel::onDeleteDialogToggle,
                loading = uiState.deleteLoading,
                actionText = "Delete",
                onConfirm = editCourseViewModel::deleteCourse
            )
        }
    }
}