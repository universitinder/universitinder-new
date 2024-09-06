package com.universitinder.app.school.schoolInformation

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
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
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.universitinder.app.models.ResultMessageType

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SchoolInformationScreen(schoolInformationViewModel: SchoolInformationViewModel) {
    val uiState by schoolInformationViewModel.uiState.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                navigationIcon = {
                 IconButton(onClick = { schoolInformationViewModel.popActivity() } ) {
                     Icon(Icons.Filled.ArrowBack, contentDescription = "Go Back")
                 }
                },
                title = {
                    Text(text = "Institution Information")
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
                        label = { Text(text = "Name") },
                        value = uiState.name,
                        onValueChange = schoolInformationViewModel::onNameChange
                    )
                    OutlinedTextField(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 10.dp),
                        label = { Text(text = "Email") },
                        value = uiState.email,
                        onValueChange = schoolInformationViewModel::onEmailChange
                    )
                    OutlinedTextField(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 10.dp),
                        label = { Text(text = "Address") },
                        value = uiState.address,
                        onValueChange = schoolInformationViewModel::onAddressChange
                    )
                    OutlinedTextField(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 10.dp),
                        label = { Text(text = "Contact Number") },
                        value = uiState.contactNumber,
                        onValueChange = schoolInformationViewModel::onContactNumberChange
                    )
                    Text(
                        modifier = Modifier.padding(top = 20.dp),
                        text = "Affordability",
                        style = MaterialTheme.typography.headlineMedium
                    )
                    OutlinedTextField(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 10.dp),
                        label = { Text(text = "Minimum Price Range") },
                        value = uiState.minimum.toString(),
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        onValueChange = schoolInformationViewModel::onMinimumChange
                    )
                    OutlinedTextField(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 10.dp),
                        label = { Text(text = "Maximum Price Range") },
                        value = uiState.maximum.toString(),
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        onValueChange = schoolInformationViewModel::onMaximumChange
                    )
                    if (uiState.resultMessage.show)
                        Text(text = uiState.resultMessage.message, color = if (uiState.resultMessage.type == ResultMessageType.FAILED) Color.Red else MaterialTheme.colorScheme.primary, textAlign = TextAlign.Center, fontSize = 12.sp)
                    Button(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 40.dp),
                        onClick = schoolInformationViewModel::setSchoolInformation
                    ) {
                        if (uiState.createSchoolLoading)
                            CircularProgressIndicator(color = MaterialTheme.colorScheme.onPrimary)
                        else
                            Text("Set Information")
                    }
                }
            }
        }
    }
}