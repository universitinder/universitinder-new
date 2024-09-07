package com.universitinder.app.school.schoolImages

import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import coil.compose.rememberAsyncImagePainter
import com.universitinder.app.models.ResultMessageType

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SchoolImagesScreen(schoolImagesViewModel: SchoolImagesViewModel) {
    val context = LocalContext.current
    val uiState by schoolImagesViewModel.uiState.collectAsState()

    val pickLogo = rememberLauncherForActivityResult(contract = ActivityResultContracts.GetContent()) { uri ->
        if (uri != null) schoolImagesViewModel.onLogoPicked(uri)
    }

    val pickImages = rememberLauncherForActivityResult(contract = ActivityResultContracts.GetMultipleContents()) { uris ->
        schoolImagesViewModel.onImagesPicked(uris)
    }

    Scaffold (
        topBar = {
            TopAppBar(
                navigationIcon = {
                    IconButton(onClick = { schoolImagesViewModel.popActivity() }) {
                        Icon(Icons.Filled.ArrowBack, contentDescription = "Go Back")
                    }
                },
                title = { Text(text = "Images") }
            )
        }
    ){ innerPadding ->
        when (uiState.fetchingLoading) {
            true -> {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator()
                }
            }
            false -> {
                LazyVerticalGrid (
                    modifier = Modifier
                        .padding(innerPadding)
                        .padding(20.dp)
                        .fillMaxSize(),
                    columns = GridCells.Fixed(3)
                ){
                    item (span = { GridItemSpan(3) }){
                        Box(
                            modifier = Modifier
                                .widthIn(max = 200.dp)
                                .aspectRatio(ratio = (10 / 10).toFloat())
                                .background(MaterialTheme.colorScheme.primaryContainer),
                            contentAlignment = Alignment.Center
                        ) {
                            if (uiState.logo != null)
                                Image(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .aspectRatio(ratio = (10 / 10).toFloat()),
                                    painter = rememberAsyncImagePainter(uiState.logo),
                                    contentDescription = "Logo"
                                )
                            else
                                Text(text = "No Logo")
                        }
                    }
                    item (span = { GridItemSpan(3) }) {
                        if (uiState.logoResultMessage.show)
                            Text(text = uiState.logoResultMessage.message, color = if (uiState.logoResultMessage.type == ResultMessageType.FAILED) Color.Red else MaterialTheme.colorScheme.primary, textAlign = TextAlign.Center, fontSize = 12.sp)
                    }
                    item (span = { GridItemSpan(3) }){
                        FilledTonalButton(
                            modifier = Modifier.padding(top = 10.dp, bottom = 10.dp),
                            onClick = { pickLogo.launch("image/*") }
                        ) {
                            if (uiState.logoLoading)
                                CircularProgressIndicator()
                            else
                                Text(
                                    text = if (uiState.logo != null) "Change Logo" else "Select Logo"
                                )
                        }
                    }
                    items(uiState.images) { uri ->
                        AsyncImage(
                            modifier = Modifier
                                .fillMaxWidth()
                                .wrapContentHeight(),
                            model = uri,
                            contentDescription = "Images"
                        )
                    }
                    item (span = { GridItemSpan(3) }) {
                        if (uiState.imagesResultMessage.show)
                            Text(text = uiState.imagesResultMessage.message, color = if (uiState.imagesResultMessage.type == ResultMessageType.FAILED) Color.Red else MaterialTheme.colorScheme.primary, textAlign = TextAlign.Center, fontSize = 12.sp)
                    }
                    item(span = { GridItemSpan(3) }) {
                        FilledTonalButton(
                            modifier = Modifier.padding(top = 10.dp),
                            onClick = { pickImages.launch("image/*") }
                        ) {
                            if (uiState.imagesLoading)
                                CircularProgressIndicator()
                            else
                                Text(text = if (uiState.images.isEmpty()) "Select Images" else "Change Images")
                        }
                    }
                    item(span = { GridItemSpan(3) }){
                        Button(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(top = 30.dp),
                            onClick = { schoolImagesViewModel.save(context) }
                        ) {
                            Text(text = "Save")
                        }
                    }
                }
            }
        }
    }
}