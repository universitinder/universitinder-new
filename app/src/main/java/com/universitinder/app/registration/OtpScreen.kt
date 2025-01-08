package com.universitinder.app.registration

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun OtpScreen(
    email: String,
    password: String,
    onSuccess: () -> Unit,  // Function type with no parameter
    onBack: () -> Unit,
    viewModel: OtpViewModel
) {
    val uiState = viewModel.uiState.collectAsState().value

    // Trigger account creation logic once
    LaunchedEffect(email, password) {
        viewModel.createAccount(
            email = email,
            password = password,
            onSuccess = {
                onSuccess() // Notify parent activity of success (close the activity)
            }
        )
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = "Email Verification",
            fontSize = 24.sp,
            style = MaterialTheme.typography.headlineMedium
        )

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = "We've sent a verification link to:",
            fontSize = 16.sp,
            textAlign = TextAlign.Center
        )

        Text(
            text = email,
            fontSize = 18.sp,
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.primary
        )

        Spacer(modifier = Modifier.height(24.dp))

        Text(
            text = "Please check your email and click the verification link to complete registration.",
            textAlign = TextAlign.Center
        )

        if (uiState.errorMessage.isNotEmpty()) {
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = uiState.errorMessage,
                color = MaterialTheme.colorScheme.error,
                textAlign = TextAlign.Center
            )
        }

        Spacer(modifier = Modifier.height(24.dp))

        Button(
            onClick = {
                viewModel.createAccount(
                    email = email,
                    password = password,
                    onSuccess = {
                        onSuccess() // Notify parent of success (close the activity)
                    }
                )
            },
            modifier = Modifier.fillMaxWidth(),
            enabled = !uiState.isLoading
        ) {
            Text("Resend Verification Email")
        }

        Spacer(modifier = Modifier.height(16.dp))

        TextButton(
            onClick = onBack,
            enabled = true
        ) {
            Text("Back")
        }

        if (uiState.isLoading) {
            CircularProgressIndicator(
                modifier = Modifier.padding(16.dp)
            )
        }
    }
}
