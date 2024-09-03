package com.universitinder.app.login

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
fun LoginScreen(loginViewModel: LoginViewModel) {
    val uiState by loginViewModel.uiState.collectAsState()

    Scaffold { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .padding(20.dp)
                .fillMaxSize(),
            verticalArrangement = Arrangement.SpaceAround
        ){
            Text(text = "Universitinder")
            Column {
                OutlinedTextField(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp),
                    value = uiState.email,
                    onValueChange = loginViewModel::onEmailChange,
                    label = { Text(text = "Email") },
                    leadingIcon = { Icon(Icons.Filled.Email, contentDescription = "Email Icon") },
                )
                OutlinedTextField(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp),
                    value = uiState.password,
                    onValueChange = loginViewModel::onPasswordChange,
                    visualTransformation = if (!uiState.showPassword) PasswordVisualTransformation() else VisualTransformation.None,
                    label = { Text(text = "Password") },
                    leadingIcon = { Icon(Icons.Filled.Lock, contentDescription = "Password Icon") },
                )
                Row (
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ){
                    Row (
                        modifier = Modifier.clickable { loginViewModel.onShowPasswordChange(!uiState.showPassword) },
                        verticalAlignment = Alignment.CenterVertically
                    ){
                        Checkbox(checked = uiState.showPassword, onCheckedChange = loginViewModel::onShowPasswordChange)
                        Text(text = "Show Password", fontSize = 16.sp)
                    }
                    Text(
                        modifier = Modifier.clickable { loginViewModel.startForgotPasswordActivity() },
                        text = "Forgot Password",
                        fontSize = 16.sp
                    )
                }
                if (uiState.resultMessage.show)
                    Text(text = uiState.resultMessage.message, color = if (uiState.resultMessage.type == ResultMessageType.FAILED) Color.Red else MaterialTheme.colorScheme.primary, textAlign = TextAlign.Center, fontSize = 12.sp)
                Button(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 40.dp),
                    onClick = loginViewModel::login
                ) {
                    if (uiState.loginLoading)
                        CircularProgressIndicator(color = MaterialTheme.colorScheme.onPrimary)
                    else
                        Text("Login")
                }
            }
            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ){
                Text(text = "Don't have an account?")
                Text(
                    modifier = Modifier.clickable { loginViewModel.startRegistrationActivity() },
                    text = "Register Here"
                )
            }
        }
    }
}