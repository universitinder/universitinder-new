package com.universitinder.app.registration

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material3.Button
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.universitinder.app.models.ResultMessageType

@Composable
fun RegistrationScreen(registrationViewModel: RegistrationViewModel) {
    val uiState by registrationViewModel.uiState.collectAsState()

    Scaffold { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(20.dp),
            verticalArrangement = Arrangement.SpaceAround,
        ){
            Text(text = "Universitinder")
            Column {
                OutlinedTextField(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp),
                    leadingIcon = { Icon(Icons.Filled.Email, contentDescription = "Email Icon") },
                    label = { Text(text = "Email") },
                    value = uiState.email,
                    onValueChange = registrationViewModel::onEmailChange
                )
                OutlinedTextField(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp),
                    leadingIcon = { Icon(Icons.Filled.Lock, contentDescription = "Password Icon") },
                    label = { Text(text = "Password") },
                    visualTransformation = if (!uiState.showPassword) PasswordVisualTransformation() else VisualTransformation.None,
                    value = uiState.password,
                    onValueChange = registrationViewModel::onPasswordChange
                )
                OutlinedTextField(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp),
                    leadingIcon = { Icon(Icons.Filled.Lock, contentDescription = "Password Icon") },
                    label = { Text(text = "Confirm Password") },
                    visualTransformation = if (!uiState.showPassword) PasswordVisualTransformation() else VisualTransformation.None,
                    value = uiState.confirmPassword,
                    onValueChange = registrationViewModel::onConfirmPasswordChange
                )
                Row (
                    modifier = Modifier.clickable { registrationViewModel.onShowPasswordChange(!uiState.showPassword) },
                    verticalAlignment = Alignment.CenterVertically
                ){
                    Checkbox(checked = uiState.showPassword, onCheckedChange = registrationViewModel::onShowPasswordChange)
                    Text(text = "Show Password", fontSize = 16.sp)
                }
                if (uiState.resultMessage.show)
                    Text(text = uiState.resultMessage.message, color = if (uiState.resultMessage.type == ResultMessageType.FAILED) Color.Red else MaterialTheme.colorScheme.primary, textAlign = TextAlign.Center, fontSize = 12.sp)
                Column (
                    modifier = Modifier.fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally
                ){
                    Button(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 40.dp),
                        onClick = registrationViewModel::register
                    ) {
                        if (uiState.registrationLoading)
                            CircularProgressIndicator(color = MaterialTheme.colorScheme.onPrimary)
                        else
                            Text("Register")
                    }
                    Text(text = "By Clicking Register, you agree on out Privacy Policy for Universitinder", textAlign = TextAlign.Center, fontSize = 14.sp)
                }
            }
            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ){
                Text(text = "Already have an account?")
                Text(
                    modifier = Modifier.clickable { registrationViewModel.startLoginActivity() },
                    text = "Login Here"
                )
            }
        }
    }
}