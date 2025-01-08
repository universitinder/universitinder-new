package com.universitinder.app.accountSetup

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
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
        ) {
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
                    value = uiState.contactNumber,
                    onValueChange = accountSetupViewModel::onContactNumberChange,
                    keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Phone)
                )
                if (uiState.resultMessage.show)
                    Text(text = uiState.resultMessage.message, color = if (uiState.resultMessage.type == ResultMessageType.FAILED) Color.Red else MaterialTheme.colorScheme.primary, textAlign = TextAlign.Center, fontSize = 12.sp)
                Button(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 40.dp),
                    onClick = { coroutineScope.launch { accountSetupViewModel.sendOtp() } }
                ) {
                    if (uiState.createLoading)
                        CircularProgressIndicator(color = MaterialTheme.colorScheme.onPrimary)
                    else
                        Text(text = "Send OTP")
                }
                if (uiState.otpSent) {
                    OutlinedTextField(
                        modifier = Modifier.fillMaxWidth().padding(vertical = 10.dp),
                        label = { Text(text = "Enter OTP") },
                        value = uiState.otp,
                        onValueChange = accountSetupViewModel::onOtpChange,
                        keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number)
                    )
                    Button(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 20.dp),
                        onClick = { coroutineScope.launch { accountSetupViewModel.verifyOtp() } }
                    ) {
                        if (uiState.verifyingOtp)
                            CircularProgressIndicator(color = MaterialTheme.colorScheme.onPrimary)
                        else
                            Text(text = "Verify OTP")
                    }
                }
            }
            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                FilledTonalButton(onClick = accountSetupViewModel::signOut) {
                    Text(text = "Sign Out")
                }
            }
        }
    }
}