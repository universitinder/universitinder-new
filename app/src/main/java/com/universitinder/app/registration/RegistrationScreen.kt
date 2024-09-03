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
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

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
                    modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp),
                    leadingIcon = { Icon(Icons.Filled.Email, contentDescription = "Email Icon") },
                    label = { Text(text = "Email") },
                    value = uiState.email,
                    onValueChange = registrationViewModel::onEmailChange
                )
                OutlinedTextField(
                    modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp),
                    leadingIcon = { Icon(Icons.Filled.Lock, contentDescription = "Password Icon") },
                    label = { Text(text = "Password") },
                    value = uiState.password,
                    onValueChange = registrationViewModel::onPasswordChange
                )
                OutlinedTextField(
                    modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp),
                    leadingIcon = { Icon(Icons.Filled.Lock, contentDescription = "Password Icon") },
                    label = { Text(text = "Confirm Password") },
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
                Button(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 40.dp),
                    onClick = { }
                ) {
                    Text("Register")
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