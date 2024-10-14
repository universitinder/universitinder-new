package com.universitinder.app.forgotPassword

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.universitinder.app.components.CurvedClipShape
import com.universitinder.app.models.ResultMessageType

@Composable
fun ForgotPasswordScreen(forgotPasswordViewModel: ForgotPasswordViewModel) {
    var bitmapState by remember { mutableStateOf<Bitmap?>(null) }
    val context = LocalContext.current
    val uiState by forgotPasswordViewModel.uiState.collectAsState()

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
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Top
            ){
                Column(
                    modifier = Modifier.fillMaxWidth().padding(top = 56.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ){
                    if (bitmapState != null) {
                        Image(
                            bitmap = bitmapState!!.asImageBitmap(),
                            contentScale = ContentScale.FillWidth,
                            contentDescription = "Logo",
                            modifier = Modifier
                                .size(120.dp)
                                .clip(CircleShape)
                        )
                    }
                    Text(
                        text = "Universitinder",
                        fontSize = 36.sp,
                        fontWeight = FontWeight.SemiBold
                    )
                    Text(
                        text = "Forgot Password",
                        fontSize = 24.sp,
                        fontWeight = FontWeight.SemiBold,
                        modifier = Modifier.padding(top = 12.dp)
                    )
                    Text(
                        text = "Please enter your email to continue",
                        fontSize = 18.sp,
                    )
                    Column(
                        modifier = Modifier.padding(top = 48.dp)
                    ){
                        OutlinedTextField(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(top = 40.dp),
                            label = { Text(text = "Email") },
                            value = uiState.email,
                            onValueChange = forgotPasswordViewModel::onEmailChange,
                            singleLine = true,
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
                            leadingIcon = { Icon(Icons.Filled.Email, contentDescription = "Email Icon") }
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
        }
    }
}