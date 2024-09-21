package com.universitinder.app.profile

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.universitinder.app.components.CircularAvatar
import com.universitinder.app.components.dialogs.ConfirmDeleteAccountDialog

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileScreen(profileViewModel: ProfileViewModel) {
    val uiState by profileViewModel.uiState.collectAsState()
    val currentUser = uiState.user

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        Text(text = "My Profile")
                    }
                }
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
            verticalArrangement = Arrangement.SpaceAround,
            horizontalAlignment = Alignment.CenterHorizontally
        ){
            if (uiState.loading) {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator()
                }
            } else {
                Column (
                    modifier = Modifier
                        .padding(top = 20.dp)
                        .fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally
                ){
                    CircularAvatar(name = currentUser.name, size = 100.dp, textStyle = MaterialTheme.typography.headlineMedium)
                    Text(
                        modifier = Modifier.padding(top = 20.dp),
                        text = currentUser.name,
                        style = MaterialTheme.typography.headlineSmall,
                        fontWeight = FontWeight.SemiBold
                    )
                    Text(text = currentUser.email, style = MaterialTheme.typography.bodyLarge)
                    Column (
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 12.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(text = "Address:", style = MaterialTheme.typography.bodySmall)
                        Text(text = currentUser.address, style = MaterialTheme.typography.bodyMedium)
                    }
                    Column (
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 12.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(text = "Contact Number:", style = MaterialTheme.typography.bodySmall)
                        Text(text = currentUser.contactNumber, style = MaterialTheme.typography.bodyMedium)
                    }
                    Row(
                        modifier = Modifier.padding(top = 8.dp)
                    ){
                        TextButton(
                            modifier = Modifier.padding(horizontal = 8.dp),
                            onClick = profileViewModel::startEditAccountActivity
                        ) {
                            Text(text = "Edit")
                        }
                        TextButton(
                            modifier = Modifier.padding(horizontal = 8.dp),
                            onClick = profileViewModel::startChangePasswordActivity
                        ) {
                            Text(text = "Change Password")
                        }
                    }
                    TextButton(
                        modifier = Modifier.padding(),
                        onClick = profileViewModel::showDeleteDialog,
                        colors = ButtonDefaults.textButtonColors(
                            contentColor = Color.Red
                        )
                    ) {
                        Text(text = "Delete Account")
                    }
                }
            }
            FilledTonalButton(
                modifier = Modifier.padding(top = 40.dp),
                onClick = profileViewModel::logout
            ) {
                Text(text = "Sign Out")
            }
        }

        ConfirmDeleteAccountDialog(
            show = uiState.showDeleteDialog,
            onDismissRequest = profileViewModel::onDismissDeleteDialog,
            deleteAccount = profileViewModel::deleteAccount,
            loading = uiState.deleteDialogLoading
        )
    }
}