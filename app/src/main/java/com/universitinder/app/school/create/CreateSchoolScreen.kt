package com.universitinder.app.school.create

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
import com.universitinder.app.components.PopUpDropDown
import com.universitinder.app.components.PrivatePublicSelector
import com.universitinder.app.models.PROVINCES
import com.universitinder.app.models.ResultMessageType

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CreateSchoolScreen(createSchoolViewModel: CreateSchoolViewModel) {
    val uiState by createSchoolViewModel.uiState.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(text = "Create School") },
                navigationIcon = {
                    IconButton(onClick = { createSchoolViewModel.popActivity() }) {
                        Icon(Icons.Filled.ArrowBack, contentDescription = "Go Back")
                    }
                }
            )
        }
    ){ innerPadding ->
        LazyColumn(
            modifier = Modifier.padding(innerPadding).padding(20.dp)
        ){
            item {
                OutlinedTextField(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 10.dp),
                    label = { Text(text = "Name") },
                    value = uiState.name,
                    onValueChange = createSchoolViewModel::onNameChange
                )
            }
            item { PrivatePublicSelector(isPrivate = uiState.private, isPublic = uiState.public, toggle = createSchoolViewModel::privateToggle) }
            item {
                OutlinedTextField(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 10.dp),
                    label = { Text(text = "Email") },
                    value = uiState.email,
                    onValueChange = createSchoolViewModel::onEmailChange
                )
            }
            item {
                OutlinedTextField(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 10.dp),
                    label = { Text(text = "Contact Number") },
                    value = uiState.contactNumber,
                    onValueChange = createSchoolViewModel::onContactNumberChange
                )
            }
            item {
                OutlinedTextField(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 10.dp),
                    label = { Text(text = "Link") },
                    value = uiState.link,
                    onValueChange = createSchoolViewModel::onLinkChange
                )
            }
            item {
                Text(modifier = Modifier.padding(top = 20.dp), text = "Address", style = MaterialTheme.typography.headlineMedium)
            }
            item {
                Box(modifier = Modifier
                    .padding(vertical = 10.dp)
                    .fillMaxWidth()) {
                    Column {
                        Text(text = "Province")
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
                                .clickable { createSchoolViewModel.onProvinceMenuExpand() },
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                modifier = Modifier.padding(vertical = 16.dp, horizontal = 18.dp),
                                text = uiState.province
                            )
                            Icon(Icons.Filled.ArrowDropDown, contentDescription = "Dropdown Arrow")
                        }
                    }
                }
            }
            item {
                PopUpDropDown(
                    label = "Select Province",
                    items = PROVINCES.toList(),
                    show = uiState.provinceMenuExpand,
                    onDismissRequest = createSchoolViewModel::onProvinceMenuDismiss,
                    onItemSelected = { createSchoolViewModel.onProvinceChange(it) }
                )
            }
            item {
                Box (
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 10.dp),
                ) {
                    Column {
                        Text(text = "Municipality/City")
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
                                .clickable { createSchoolViewModel.onMunicipalityOrCityMenuExpand() },
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                modifier = Modifier.padding(vertical = 16.dp, horizontal = 18.dp),
                                text = uiState.municipalityOrCity
                            )
                            Icon(Icons.Filled.ArrowDropDown, contentDescription = "Dropdown Arrow")
                        }
                    }
                }
            }
            item {
                PopUpDropDown(
                    label = "Select Municipality/City",
                    items = uiState.municipalitiesAndCities,
                    show = uiState.municipalityOrCityMenuExpand,
                    onDismissRequest = createSchoolViewModel::onMunicipalityOrCityMenuDismiss,
                    onItemSelected = { createSchoolViewModel.onMunicipalityOrCityChange(it) }
                )
            }
            item {
                OutlinedTextField(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 10.dp),
                    label = { Text(text = "Barangay") },
                    value = uiState.barangay,
                    onValueChange = createSchoolViewModel::onBarangayChange
                )
            }
            item {
                OutlinedTextField(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 10.dp),
                    label = { Text(text = "Street") },
                    value = uiState.street,
                    onValueChange = createSchoolViewModel::onStreetChange
                )
            }
            item {
                if (uiState.resultMessage.show)
                    Text(text = uiState.resultMessage.message, color = if (uiState.resultMessage.type == ResultMessageType.FAILED) Color.Red else MaterialTheme.colorScheme.primary, textAlign = TextAlign.Center, fontSize = 12.sp)
            }
            item {
                Button(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 40.dp),
                    onClick = createSchoolViewModel::createSchool
                ) {
                    if (uiState.createSchoolLoading)
                        CircularProgressIndicator(color = MaterialTheme.colorScheme.onPrimary)
                    else
                        Text("Create")
                }
            }
        }
    }
}