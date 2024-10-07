package com.universitinder.app.school.schoolFAQs.editFAQ

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
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
import com.universitinder.app.components.dialogs.ConfirmDialog
import com.universitinder.app.models.ResultMessageType

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditFAQScreen(editFAQViewModel: EditFAQViewModel) {
    val uiState by editFAQViewModel.uiState.collectAsState()

    Scaffold (
        topBar = {
            TopAppBar(
                title = { Text(text = "Edit Question") },
                navigationIcon = { IconButton(onClick = { editFAQViewModel.popActivity() }) {
                    Icon(Icons.Filled.ArrowBack, contentDescription = "Go Back")
                }},
                actions = {
                    IconButton(onClick = editFAQViewModel::onDeleteDialogToggle, colors = IconButtonDefaults.iconButtonColors(contentColor = Color.Red)) {
                        Icon(Icons.Filled.Delete, contentDescription = "Delete")
                    }
                }
            )
        }
    ){ innerPadding ->
        when (uiState.fetchingLoading) {
            true -> {
                Box(modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding), contentAlignment = Alignment.Center){
                    CircularProgressIndicator()
                }
            }
            false -> {
                LazyColumn(
                    modifier = Modifier
                        .padding(innerPadding)
                        .padding(20.dp)
                ){
                    item {
                        OutlinedTextField(
                            modifier = Modifier.fillMaxWidth(),
                            label = { Text(text = "Question") },
                            value = uiState.question,
                            onValueChange = editFAQViewModel::onQuestionChange
                        )
                    }
                    item {
                        OutlinedTextField(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(top = 10.dp),
                            label = { Text(text = "Answer") },
                            value = uiState.answer,
                            onValueChange = editFAQViewModel::onAnswerChange
                        )
                    }
                    item {
                        if (uiState.resultMessage.show)
                            Text(
                                modifier = Modifier.padding(top=10.dp),
                                text = uiState.resultMessage.message, color = if (uiState.resultMessage.type == ResultMessageType.FAILED) Color.Red else MaterialTheme.colorScheme.primary, textAlign = TextAlign.Center, fontSize = 12.sp)
                    }
                    item {
                        Button(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(top = 20.dp),
                            onClick = editFAQViewModel::updateFAQ
                        ) {
                            if (uiState.updateLoading) {
                                CircularProgressIndicator(color = MaterialTheme.colorScheme.onPrimary)
                            } else {
                                Text(text = "Update Question")
                            }
                        }
                    }
                }
                ConfirmDialog(
                    title = "Delete Confirmation",
                    text = "Are you sure you want to delete this FAQ?",
                    show = uiState.showDeleteDialog,
                    onDismissRequest = editFAQViewModel::onDeleteDialogToggle,
                    resultMessage = uiState.deleteResultMessage,
                    onCancel = editFAQViewModel::onDeleteDialogToggle,
                    loading = uiState.deleteLoading,
                    actionText = "Delete",
                    onConfirm = editFAQViewModel::deleteFAQ
                )
            }
        }
    }
}