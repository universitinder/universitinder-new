package com.universitinder.app.school.schoolInformation

import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
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
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.universitinder.app.components.AffordabilityIndicator
import com.universitinder.app.components.PopUpDropDown
import com.universitinder.app.components.PrivatePublicSelector
import com.universitinder.app.models.ResultMessageType
import com.universitinder.app.models.PROVINCES

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
                    PrivatePublicSelector(isPrivate = uiState.private, isPublic = uiState.public, toggle = schoolInformationViewModel::privateToggle)
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
                        label = { Text(text = "Contact Number") },
                        value = uiState.contactNumber,
                        onValueChange = schoolInformationViewModel::onContactNumberChange
                    )
                    OutlinedTextField(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 10.dp),
                        label = { Text(text = "Link") },
                        value = uiState.link,
                        onValueChange = schoolInformationViewModel::onLinkChange
                    )
                    Text(modifier = Modifier.padding(top = 20.dp), text = "Address", style = MaterialTheme.typography.headlineMedium)
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
                                    .clickable { schoolInformationViewModel.onProvinceMenuExpand() },
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
                    PopUpDropDown(
                        label = "Select Province",
                        items = PROVINCES.toList(),
                        show = uiState.provinceMenuExpand,
                        onDismissRequest = schoolInformationViewModel::onProvinceMenuDismiss,
                        onItemSelected = { schoolInformationViewModel.onProvinceChange(it) }
                    )
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
                                    .clickable { schoolInformationViewModel.onMunicipalityOrCityMenuExpand() },
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
                    PopUpDropDown(
                        label = "Select Municipality/City",
                        items = uiState.municipalitiesAndCities,
                        show = uiState.municipalityOrCityMenuExpand,
                        onDismissRequest = schoolInformationViewModel::onMunicipalityOrCityMenuDismiss,
                        onItemSelected = { schoolInformationViewModel.onMunicipalityOrCityChange(it) }
                    )
                    OutlinedTextField(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 10.dp),
                        label = { Text(text = "Barangay") },
                        value = uiState.barangay,
                        onValueChange = schoolInformationViewModel::onBarangayChange
                    )
                    OutlinedTextField(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 10.dp),
                        label = { Text(text = "Street") },
                        value = uiState.street,
                        onValueChange = schoolInformationViewModel::onStreetChange
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
                        label = { Text(text = "Tuition") },
                        value = uiState.maximum.toString(),
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        onValueChange = schoolInformationViewModel::onMaximumChange
                    )

                    Column {
                        Text(text = "Affordability")
                        AffordabilityIndicator(affordability = uiState.affordability)
                    }
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
