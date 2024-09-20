package com.universitinder.app.changePassword

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.Checkbox
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
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.universitinder.app.models.ResultMessageType

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChangePasswordScreen(changePasswordViewModel: ChangePasswordViewModel) {
    val uiState by changePasswordViewModel.uiState.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(text = "Change Password") },
                navigationIcon = {
                    IconButton(onClick = { changePasswordViewModel.popActivity() }) {
                        Icon(Icons.Filled.ArrowBack, contentDescription = "Go Back")
                    }
                }
            )
        }
    ){ innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .padding(20.dp),
        ){
            OutlinedTextField(
                modifier = Modifier.fillMaxWidth().padding(vertical = 10.dp),
                label = { Text(text = "New Password") },
                value = uiState.newPassword,
                onValueChange = changePasswordViewModel::onNewPasswordChange,
                visualTransformation = if (uiState.showPassword) VisualTransformation.None else PasswordVisualTransformation()
            )
            OutlinedTextField(
                modifier = Modifier.fillMaxWidth().padding(vertical = 10.dp),
                label = { Text(text = "Confirm New Password") },
                value = uiState.confirmNewPassword,
                onValueChange = changePasswordViewModel::onConfirmNewPasswordChange,
                visualTransformation = if (uiState.showPassword) VisualTransformation.None else PasswordVisualTransformation()
            )
            Row (
                modifier = Modifier.clickable { changePasswordViewModel.toggleShowPassword() },
                verticalAlignment = Alignment.CenterVertically
            ){
                Checkbox(checked = uiState.showPassword, onCheckedChange = { changePasswordViewModel.toggleShowPassword() })
                Text(text = "Show Password", fontSize = 16.sp)
            }
            if (uiState.resultMessage.show)
                Text(text = uiState.resultMessage.message, color = if (uiState.resultMessage.type == ResultMessageType.FAILED) Color.Red else MaterialTheme.colorScheme.primary, textAlign = TextAlign.Center, fontSize = 12.sp)

            Button(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 40.dp),
                onClick = changePasswordViewModel::onChangePasswordClick
            ) {
                if (uiState.loading)
                    CircularProgressIndicator(color = MaterialTheme.colorScheme.onPrimary)
                else
                    Text("Change Password")
            }
        }
    }
}