package com.universitinder.app.forgotPassword

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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.universitinder.app.models.ResultMessageType

@Composable
fun ForgotPasswordScreen(forgotPasswordViewModel: ForgotPasswordViewModel) {
    val uiState by forgotPasswordViewModel.uiState.collectAsState()

    Scaffold { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .padding(20.dp)
                .fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ){
            Column {
                Text(text = "Forgot Password", fontSize = 24.sp, fontWeight = FontWeight.SemiBold)
            }
            OutlinedTextField(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 40.dp),
                label = { Text(text = "Email") },
                value = uiState.email,
                onValueChange = forgotPasswordViewModel::onEmailChange
            )
            if (uiState.resultMessage.show)
                Text(modifier = Modifier.padding(top = 8.dp), text = uiState.resultMessage.message, color = if (uiState.resultMessage.type == ResultMessageType.FAILED) Color.Red else MaterialTheme.colorScheme.primary, textAlign = TextAlign.Center, fontSize = 12.sp)
            Button(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 40.dp),
                onClick = forgotPasswordViewModel::sendEmail
            ) {
                if (uiState.loading)
                    CircularProgressIndicator(color = MaterialTheme.colorScheme.onPrimary)
                else
                    Text(text = "Send Email")
            }
            FilledTonalButton(
                modifier = Modifier
                    .padding(top = 8.dp)
                    .fillMaxWidth(),
                onClick = { forgotPasswordViewModel.popActivity() }
            ) {
                Text(text = "Cancel")
            }
        }
    }
}