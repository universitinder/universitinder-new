package com.universitinder.app.profile.editAccount

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.CornerSize
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
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.universitinder.app.models.ResultMessageType
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditAccountScreen(editAccountViewModel: EditAccountViewModel) {
    val uiState by editAccountViewModel.uiState.collectAsState()
    val coroutineScope = rememberCoroutineScope()

   Scaffold(
       topBar = {
           TopAppBar(
               navigationIcon = {
                   IconButton(onClick = { editAccountViewModel.popActivity() } ) {
                       Icon(Icons.Filled.ArrowBack, contentDescription = "Go Back")
                   }
               },
               title = { Text(text = "Edit Account") }
           )
       }
   ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
                .padding(20.dp),
            verticalArrangement = Arrangement.SpaceAround
        ){
            Column {
                OutlinedTextField(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 10.dp),
                    label = { Text(text = "Email") },
                    value = uiState.email,
                    onValueChange = editAccountViewModel::onEmailChange,
                    readOnly = true
                )
                Box (
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 10.dp),
                ){
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .border(
                                width = 0.5.dp, color = Color.Black, shape = CircleShape.copy(
                                    CornerSize(5.dp)
                                )
                            ),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ){
                        Text(
                            modifier = Modifier.padding(vertical = 14.dp, horizontal = 18.dp),
                            text = uiState.type.toString()
                        )
                    }
                }
                OutlinedTextField(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 10.dp),
                    label = { Text(text = "Name") },
                    value = uiState.name,
                    onValueChange = editAccountViewModel::onNameChange
                )
                OutlinedTextField(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 10.dp),
                    label = { Text(text = "Address") },
                    value = uiState.address,
                    onValueChange = editAccountViewModel::onAddressChange
                )
                OutlinedTextField(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 10.dp),
                    label = { Text(text = "Contact Number") },
                    leadingIcon = { Text(text = "+63 ", modifier = Modifier.padding(start = 16.dp)) },
                    value = uiState.contactNumber,
                    onValueChange = editAccountViewModel::onContactNumberChange,
                    placeholder = { Text(text = "9XXXXXXXXX") }
                )
                if (uiState.resultMessage.show)
                    Text(text = uiState.resultMessage.message, color = if (uiState.resultMessage.type == ResultMessageType.FAILED) Color.Red else MaterialTheme.colorScheme.primary, textAlign = TextAlign.Center, fontSize = 12.sp)
                Button(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 40.dp),
                    onClick = { coroutineScope.launch { editAccountViewModel.updateUser() } }
                ) {
                    if (uiState.createLoading)
                        CircularProgressIndicator(color = MaterialTheme.colorScheme.onPrimary)
                    else
                        Text(text = "Update")
                }
            }
        }
    }
}