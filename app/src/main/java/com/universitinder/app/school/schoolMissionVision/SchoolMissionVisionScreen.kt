package com.universitinder.app.school.schoolMissionVision

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
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
import com.universitinder.app.models.ResultMessageType

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SchoolMissionVisionScreen(viewModel: SchoolMissionVisionViewModel) {
    val uiState by viewModel.uiState.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                navigationIcon = {
                    IconButton(onClick = { viewModel.popActivity() } ) {
                        Icon(Icons.Filled.ArrowBack, contentDescription = "Go Back")
                    }
                },
                title = {
                    Text(text = "Mission/Vision/Core Values")
                }
            )
        }
    ){ innerPadding ->
        when (uiState.fetchingDataLoading) {
            true -> {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator()
                }
            }
            false -> {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(20.dp)
                        .padding(innerPadding)
                        .verticalScroll(rememberScrollState())
                ){
                    Text(text = "General Information", style = MaterialTheme.typography.headlineMedium)
                    OutlinedTextField(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 10.dp),
                        label = { Text(text = "Mission") },
                        value = uiState.mission,
                        onValueChange = viewModel::onMissionChange
                    )
                    OutlinedTextField(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 10.dp),
                        label = { Text(text = "Vision") },
                        value = uiState.vision,
                        onValueChange = viewModel::onVisionChange
                    )
                    OutlinedTextField(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 10.dp),
                        label = { Text(text = "Core Values") },
                        value = uiState.coreValues,
                        onValueChange = viewModel::onCoreValuesChange
                    )
                    if (uiState.resultMessage.show)
                        Text(text = uiState.resultMessage.message, color = if (uiState.resultMessage.type == ResultMessageType.FAILED) Color.Red else MaterialTheme.colorScheme.primary, textAlign = TextAlign.Center, fontSize = 12.sp)
                    Button(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 40.dp),
                        onClick = viewModel::setSchoolInformation
                    ) {
                        if (uiState.setInformationLoading)
                            CircularProgressIndicator(color = MaterialTheme.colorScheme.onPrimary)
                        else
                            Text("Set Information")
                    }
                }
            }
        }
    }
}

