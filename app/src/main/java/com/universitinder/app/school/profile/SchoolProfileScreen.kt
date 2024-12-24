package com.universitinder.app.school.profile

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.universitinder.app.components.AffordabilityIndicator
import com.universitinder.app.components.EmailText
import com.universitinder.app.helpers.CurrencyFormatter

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SchoolProfileScreen(schoolProfileViewModel: SchoolProfileViewModel) {
    val uiState by schoolProfileViewModel.uiState.collectAsState()

    Scaffold (
        topBar = {
            TopAppBar(
                title = { Text(text = "School Profile") },
                navigationIcon = {
                    IconButton(onClick = { schoolProfileViewModel.popActivity() }) {
                        Icon(Icons.Filled.ArrowBack, "Go Back")
                    }
                }
            )
        }
    ){ innerPadding ->
        LazyColumn(
            modifier = Modifier.padding(innerPadding)
        ){
            item {
                Card(
                    elevation = CardDefaults.cardElevation(defaultElevation = 5.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = Color.White
                    ),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(20.dp)
                ){
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier
                            .padding(20.dp)
                            .fillMaxSize()
                    ){
                        Box(modifier = Modifier
                            .clip(CircleShape)
                            .background(MaterialTheme.colorScheme.primaryContainer)
                            .size(100.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            AsyncImage(model = uiState.logo, contentDescription = "Logo", contentScale = ContentScale.FillWidth, modifier = Modifier.fillMaxWidth())
                        }
                        Text(text = uiState.school.name, modifier = Modifier.padding(top = 10.dp))
                    }
                    OutlinedCard(
                        border = BorderStroke(0.2.dp, Color.Black),
                        colors = CardDefaults.cardColors(
                            containerColor = Color.White
                        ),
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(20.dp)
                    ){
                        ListItem(
                            modifier = Modifier.background(Color.White),
                            leadingContent = { Icon(Icons.Filled.Email, "Email") },
                            headlineContent = { EmailText(email = uiState.school.email) }
                        )
                        ListItem(
                            leadingContent = { Icon(Icons.Filled.Phone, "Contact Number") },
                            headlineContent = { Text(text = uiState.school.contactNumber) }
                        )
                    }
                }
            }
            item {
                ListItem(
                    overlineContent = { Text(text = "Province") },
                    headlineContent = { Text(text = uiState.school.province) }
                )
            }
            item {
                ListItem(
                    overlineContent = { Text(text = "Municipality/City") },
                    headlineContent = { Text(text = uiState.school.municipalityOrCity) }
                )
            }
            item {
                ListItem(
                    overlineContent = { Text(text = "Barangay") },
                    headlineContent = { Text(text = uiState.school.barangay) }
                )
            }
            item {
                ListItem(
                    overlineContent = { Text(text = "Street") },
                    headlineContent = { Text(text = uiState.school.street) }
                )
            }
            item {
                ListItem(
                    overlineContent = { Text(text = "Tuition") },
                    headlineContent = { Text(text = CurrencyFormatter.format(uiState.school.maximum.toDouble())) }
                )
            }
            item {
                ListItem(
                    overlineContent = { Text(text = "Affordability") },
                    headlineContent = { AffordabilityIndicator(affordability = uiState.school.affordability) }
                )
            }
            item {
                ListItem(
                    modifier = Modifier.padding(top = 20.dp),
                    overlineContent = { Text(text = "Mission") },
                    headlineContent = { Text(text = uiState.school.mission) }
                )
            }
            item {
                ListItem(
                    overlineContent = { Text(text = "Vision") },
                    headlineContent = { Text(text = uiState.school.vision) }
                )
            }
            item {
                ListItem(
                    overlineContent = { Text(text = "Core Values") },
                    headlineContent = { Text(text = uiState.school.coreValues) }
                )
            }
        }
    }
}