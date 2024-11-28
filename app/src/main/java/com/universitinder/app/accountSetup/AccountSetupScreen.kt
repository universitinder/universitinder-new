package com.universitinder.app.accountSetup

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.universitinder.app.models.ResultMessageType
import kotlinx.coroutines.launch

@Composable
fun AccountSetupScreen(accountSetupViewModel: AccountSetupViewModel) {
    val uiState by accountSetupViewModel.uiState.collectAsState()
    val coroutineScope = rememberCoroutineScope()

    Scaffold { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
                .padding(20.dp),
            verticalArrangement = Arrangement.SpaceAround
        ){
            Column {
                Text(
                    text = "Account Information",
                    fontSize = 36.sp,
                    fontWeight = FontWeight.SemiBold
                )
                Text(
                    text = "Please fill in the form to continue",
                    fontSize = 18.sp,
                )
            }
            Column {
                OutlinedTextField(
                    modifier = Modifier.fillMaxWidth().padding(vertical = 10.dp),
                    label = { Text(text = "Email") },
                    value = uiState.email,
                    onValueChange = accountSetupViewModel::onEmailChange,
                    readOnly = true
                )
                OutlinedTextField(
                    modifier = Modifier.fillMaxWidth().padding(vertical = 10.dp),
                    label = { Text(text = "Name") },
                    value = uiState.name,
                    onValueChange = accountSetupViewModel::onNameChange
                )
                OutlinedTextField(
                    modifier = Modifier.fillMaxWidth().padding(vertical = 10.dp),
                    label = { Text(text = "Address") },
                    value = uiState.address,
                    onValueChange = accountSetupViewModel::onAddressChange
                )
                OutlinedTextField(
                    modifier = Modifier.fillMaxWidth().padding(vertical = 10.dp),
                    label = { Text(text = "Contact Number") },
                    leadingIcon = { Text(text = "+63 ", modifier = Modifier.padding(start = 16.dp)) },
                    value = uiState.contactNumber,
                    onValueChange = accountSetupViewModel::onContactNumberChange,
                    placeholder = { Text(text = "09XXXXXXXXX") }
                )

                if (uiState.resultMessage.show)
                    Text(text = uiState.resultMessage.message, color = if (uiState.resultMessage.type == ResultMessageType.FAILED) Color.Red else MaterialTheme.colorScheme.primary, textAlign = TextAlign.Center, fontSize = 12.sp)
                Button(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 40.dp),
                    onClick = { coroutineScope.launch { accountSetupViewModel.createUser() } }
                ) {
                    if (uiState.createLoading)
                        CircularProgressIndicator(color = MaterialTheme.colorScheme.onPrimary)
                    else
                        Text(text = "Continue")
                }
            }
            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ){
                FilledTonalButton(onClick = accountSetupViewModel::signOut) {
                    Text(text = "Sign Out")
                }
            }
        }
    }
}