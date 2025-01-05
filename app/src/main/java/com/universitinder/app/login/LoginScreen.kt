package com.universitinder.app.login

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.universitinder.app.components.CurvedClipShape
import com.universitinder.app.models.ResultMessageType

@Composable
fun LoginScreen(loginViewModel: LoginViewModel) {
    var bitmapState by remember { mutableStateOf<Bitmap?>(null) }
    val context = LocalContext.current
    val uiState by loginViewModel.uiState.collectAsState()

    LaunchedEffect(Unit) {
        val inputStream = context.assets.open("logo.jpg")
        bitmapState = BitmapFactory.decodeStream(inputStream)
    }

    Scaffold { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
            contentAlignment = Alignment.TopCenter,
        ) {
            CurvedClipShape()
            Column(
                modifier = Modifier
                    .padding(innerPadding)
                    .padding(20.dp)
                    .fillMaxSize(),
                verticalArrangement = Arrangement.SpaceAround
            ) {
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    if (bitmapState != null) {
                        Image(
                            bitmap = bitmapState!!.asImageBitmap(),
                            contentScale = ContentScale.FillWidth,
                            contentDescription = "Logo",
                            modifier = Modifier.size(120.dp).clip(CircleShape)
                        )
                    }
                    Text(
                        text = "Universitinder",
                        fontSize = 36.sp,
                        fontWeight = FontWeight.SemiBold
                    )
                    Text(
                        text = "Please login to continue",
                        fontSize = 18.sp,
                    )
                }
                Column {
                    OutlinedTextField(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 8.dp),
                        value = uiState.email,
                        onValueChange = loginViewModel::onEmailChange,
                        singleLine = true,
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
                        label = { Text(text = "Email") },
                        leadingIcon = { Icon(Icons.Filled.Email, contentDescription = "Email Icon") },
                    )
                    OutlinedTextField(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 8.dp),
                        value = uiState.password,
                        onValueChange = loginViewModel::onPasswordChange,
                        singleLine = true,
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                        visualTransformation = if (!uiState.showPassword) PasswordVisualTransformation() else VisualTransformation.None,
                        label = { Text(text = "Password") },
                        leadingIcon = { Icon(Icons.Filled.Lock, contentDescription = "Password Icon") },
                    )
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Row(
                            modifier = Modifier.clickable { loginViewModel.onShowPasswordChange(!uiState.showPassword) },
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Checkbox(checked = uiState.showPassword, onCheckedChange = loginViewModel::onShowPasswordChange)
                            Text(text = "Show Password", fontSize = 16.sp)
                        }
                        Text(
                            modifier = Modifier.clickable { loginViewModel.startForgotPasswordActivity() },
                            text = "Forgot Password",
                            fontSize = 16.sp,
                            color = MaterialTheme.colorScheme.primary
                        )
                    }
                    if (uiState.resultMessage.show)
                        Text(
                            text = uiState.resultMessage.message,
                            color = if (uiState.resultMessage.type == ResultMessageType.FAILED) Color.Red else MaterialTheme.colorScheme.primary,
                            textAlign = TextAlign.Center,
                            fontSize = 12.sp
                        )
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
                ) {
                    Text(text = "Don't have an account?")
                    Text(
                        modifier = Modifier.clickable { loginViewModel.startRegistrationActivity() },
                        text = "Register Here",
                        color = MaterialTheme.colorScheme.primary
                    )
                }
            }
        }
    }
}