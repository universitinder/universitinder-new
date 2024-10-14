package com.universitinder.app.school.list

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.ListItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
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
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.universitinder.app.models.School

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SchoolListScreen(schoolListViewModel: SchoolListViewModel) {
    val uiState by schoolListViewModel.uiState.collectAsState()
    var bitmapState by remember { mutableStateOf<Bitmap?>(null) }
    val context = LocalContext.current

    LaunchedEffect(Unit) {
        bitmapState = BitmapFactory.decodeStream(context.assets.open("logo.jpg"))
    }

    Scaffold (
        topBar = {
            TopAppBar(
                title = {
                    Row (verticalAlignment = Alignment.CenterVertically){
                        if (bitmapState != null) {
                            Image(
                                bitmap = bitmapState!!.asImageBitmap(),
                                contentDescription = "Logo",
                                modifier = Modifier.size(50.dp).clip(CircleShape),
                                contentScale = ContentScale.FillWidth
                            )
                        }
                        Text(text = "Schools", modifier = Modifier.padding(start = 12.dp))
                    }
                }
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = { schoolListViewModel.startRegisterSchool() },
                shape = CircleShape
            ) {
                Icon(Icons.Filled.Add, contentDescription = "Add School")
            }
        }
    ){ innerPadding ->
        when (uiState.fetchingLoading) {
            true -> {
                Box(modifier = Modifier
                    .padding(innerPadding)
                    .fillMaxSize(), contentAlignment = Alignment.Center ) {
                    CircularProgressIndicator()
                }
            }
            else -> {
                when (uiState.schools.size) {
                    0 -> {
                        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                            Text(text = "No Available Schools")
                        }
                    }
                    else -> {
                        LazyColumn(
                            modifier = Modifier.padding(innerPadding)
                        ){
                            itemsIndexed(
                                uiState.schools,
                                key = { _, it ->
                                    it.id
                                }
                            ) { index, it ->
                                val schoolObject = it.toObject(School::class.java)
                                if (schoolObject != null) {
                                    ListItem(
                                        modifier = Modifier.clickable { schoolListViewModel.startSchoolActivity(schoolObject) },
                                        leadingContent = { Text(text = "${index+1}") },
                                        headlineContent = { Text(text = schoolObject.name) },
                                        supportingContent = { Text(text = schoolObject.email) }
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}