package com.universitinder.app.components

import android.net.Uri
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import coil.compose.AsyncImage
import coil.compose.AsyncImagePainter

@Composable
fun SchoolPhotos(photo: Uri) {
    var showDialog by remember { mutableStateOf(false) }
    var loading by remember { mutableStateOf(false) }
    var dialogLoading by remember { mutableStateOf(false) }

    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        AsyncImage(
            modifier = Modifier
                .fillMaxWidth()
                .clickable { showDialog = true },
            model = photo,
            contentDescription = "Images",
            onState = {
                loading = when(it) {
                    is AsyncImagePainter.State.Loading -> {
                        true
                    }

                    else -> {
                        false
                    }
                }
            }
        )
        if (loading) {
            CircularProgressIndicator()
        }
    }

    if (showDialog) {
        Dialog(
            onDismissRequest = { showDialog = false },
        ) {
            Box(
                modifier = Modifier.padding(10.dp),
                contentAlignment = Alignment.Center
            ) {
                AsyncImage(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { showDialog = true },
                    model = photo,
                    contentDescription = "Images",
                    onState = {
                        dialogLoading = when(it) {
                            is AsyncImagePainter.State.Loading -> {
                                true
                            }

                            else -> {
                                false
                            }
                        }
                    }
                )
                if (dialogLoading) {
                    CircularProgressIndicator(color = Color.White)
                }
            }
        }
    }
}