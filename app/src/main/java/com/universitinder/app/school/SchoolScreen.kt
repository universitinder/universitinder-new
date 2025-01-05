package com.universitinder.app.school

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.ListItem
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.rememberAsyncImagePainter
import com.universitinder.app.components.AffordabilityIndicator
import com.universitinder.app.components.Analytics
import com.universitinder.app.components.EmailText
import com.universitinder.app.components.LinkText
import com.universitinder.app.components.SchoolBasicInfo
import com.universitinder.app.components.SchoolBasicInfoComposable
import com.universitinder.app.components.SchoolPhotos
import com.universitinder.app.components.dialogs.ConfirmDeleteSchoolDialog
import com.universitinder.app.helpers.CurrencyFormatter

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SchoolScreen(schoolViewModel: SchoolViewModel) {
    val uiState by schoolViewModel.uiState.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(text = "School Profile") },
                navigationIcon = {
                    IconButton(onClick = { schoolViewModel.popActivity() }) {
                        Icon(Icons.Filled.ArrowBack, contentDescription = "Go Back")
                    }
                },
                actions = {
                    IconButton(onClick = schoolViewModel::startInformationNavigation) {
                        Icon(Icons.Filled.Edit, contentDescription = "Edit")
                    }
                    IconButton(onClick = schoolViewModel::toggleDeleteDialog, colors = IconButtonDefaults.iconButtonColors(contentColor = Color.Red)) {
                        Icon(Icons.Filled.Delete, contentDescription = "Delete")
                    }
                }
            )
        },
    ) { innerPadding ->
        when (uiState.fetchingLoading) {
            true -> {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator()
                }
            }
            false -> {
                if (uiState.schoolPlusImages != null) {
                    LazyVerticalGrid(
                        modifier = Modifier
                            .padding(innerPadding)
                            .padding(20.dp)
                            .fillMaxWidth(),
                        columns = GridCells.Fixed(3)
                    ) {
                        item(span = { GridItemSpan(3) }) {
                            Column(
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                Box(
                                    modifier = Modifier
                                        .size(200.dp)
                                        .border(
                                            3.dp,
                                            MaterialTheme.colorScheme.secondaryContainer,
                                            CircleShape
                                        )
                                        .clip(CircleShape)
                                        .background(MaterialTheme.colorScheme.primaryContainer),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Image(
                                        modifier = Modifier.fillMaxSize(),
                                        painter = rememberAsyncImagePainter(model = uiState.logo),
                                        contentDescription = "Logo",
                                        contentScale = ContentScale.FillWidth
                                    )
                                }
                                Text(
                                    modifier = Modifier.padding(top = 8.dp),
                                    text = uiState.schoolPlusImages?.school!!.name,
                                    fontSize = 18.sp
                                )
                                Row(
                                    modifier = Modifier.padding(top = 12.dp)
                                ){
                                    Column(
                                        modifier = Modifier.padding(horizontal = 8.dp),
                                        horizontalAlignment = Alignment.CenterHorizontally
                                    ){
                                        Text(text = uiState.schoolPlusImages?.school!!.swipeLeft.toString(), fontWeight = FontWeight.Medium)
                                        Text(text = "Left Swipes", fontSize = 12.sp)
                                    }
                                    Column(
                                        modifier = Modifier.padding(horizontal = 8.dp),
                                        horizontalAlignment = Alignment.CenterHorizontally
                                    ){
                                        Text(text = uiState.schoolPlusImages?.school!!.swipeRight.toString(), fontWeight = FontWeight.Medium)
                                        Text(text = "Right Swipes", fontSize = 12.sp)
                                    }
                                }
                            }
                        }
                        item(span = { GridItemSpan(3) }) {
                            LazyRow(
                                modifier = Modifier.padding(top = 12.dp)
                            ) {
                                item {
                                    TextButton(
                                        onClick = { schoolViewModel.onTabChange(0) },
                                        colors = ButtonDefaults.textButtonColors(
                                            containerColor = if (uiState.activeTab == 0) MaterialTheme.colorScheme.primaryContainer else Color.Transparent
                                        )
                                    ) {
                                        Text(text = "Basic Info")
                                    }
                                }
                                item {
                                    TextButton(
                                        onClick = { schoolViewModel.onTabChange(1) },
                                        colors = ButtonDefaults.textButtonColors(
                                            containerColor = if (uiState.activeTab == 1) MaterialTheme.colorScheme.primaryContainer else Color.Transparent
                                        )
                                    ) {
                                        Text(text = "Analytics")
                                    }
                                }
                                item {
                                    TextButton(
                                        onClick = { schoolViewModel.onTabChange(2) },
                                        colors = ButtonDefaults.textButtonColors(
                                            containerColor = if (uiState.activeTab == 2) MaterialTheme.colorScheme.primaryContainer else Color.Transparent
                                        )
                                    ) {
                                        Text(text = "Photos")
                                    }
                                }
                                item {
                                    TextButton(
                                        onClick = { schoolViewModel.onTabChange(3) },
                                        colors = ButtonDefaults.textButtonColors(
                                            containerColor = if (uiState.activeTab == 3) MaterialTheme.colorScheme.primaryContainer else Color.Transparent
                                        )
                                    ) {
                                        Text(text = "Courses")
                                    }
                                }
                                item {
                                    TextButton(
                                        onClick = { schoolViewModel.onTabChange(4) },
                                        colors = ButtonDefaults.textButtonColors(
                                            containerColor = if (uiState.activeTab == 4) MaterialTheme.colorScheme.primaryContainer else Color.Transparent
                                        )
                                    ) {
                                        Text(text = "Mission")
                                    }
                                }
                                item {
                                    TextButton(
                                        onClick = { schoolViewModel.onTabChange(5) },
                                        colors = ButtonDefaults.textButtonColors(
                                            containerColor = if (uiState.activeTab == 5) MaterialTheme.colorScheme.primaryContainer else Color.Transparent
                                        )
                                    ) {
                                        Text(text = "Vision")
                                    }
                                }
                                item {
                                    TextButton(
                                        onClick = { schoolViewModel.onTabChange(6) },
                                        colors = ButtonDefaults.textButtonColors(
                                            containerColor = if (uiState.activeTab == 6) MaterialTheme.colorScheme.primaryContainer else Color.Transparent
                                        )
                                    ) {
                                        Text(text = "Core Values")
                                    }
                                }
                            }
                        }
                        when (uiState.activeTab) {
                            0 -> {
                                val school = uiState.schoolPlusImages?.school
                                item(span = { GridItemSpan(3) }) { SchoolBasicInfoComposable(label = "Email", component = { EmailText(email = school!!.email) }) }
                                item(span = { GridItemSpan(3) }) { SchoolBasicInfoComposable(label = "Link", component = { LinkText(link = school!!.link) }) }
                                item(span = { GridItemSpan(3) }) { SchoolBasicInfo(label = "Contact Number", value = school!!.contactNumber) }
                                item(span = { GridItemSpan(3) }) { SchoolBasicInfo(label = "Province", value = school!!.province) }
                                item(span = { GridItemSpan(3) }) { SchoolBasicInfo(label = "Municipality/City", value = school!!.municipalityOrCity) }
                                item(span = { GridItemSpan(3) }) { SchoolBasicInfo(label = "Barangay", value = school!!.barangay) }
                                item(span = { GridItemSpan(3) }) { SchoolBasicInfo(label = "Street", value = school!!.street) }
                                item(span = { GridItemSpan(3) }) { SchoolBasicInfo(label = "Tuition", value =  CurrencyFormatter.format(school!!.maximum.toDouble())) }
                                item(span = { GridItemSpan(3) }) { AffordabilityIndicator(affordability = school!!.affordability) }
                            }
                            1 -> {
                                item(span = { GridItemSpan(3) } ){
                                    Analytics(
                                        schoolAnalytics = uiState.schoolAnalytics,
                                        lineChartLoading = uiState.lineChartLoading,
                                        selectedYearLevel = uiState.selectedYear,
                                        onYearLevelSelected = schoolViewModel::onYearLevelSelected
                                    )
                                }
                            }
                            2 -> {
                                item(span = { GridItemSpan(3) }) { Spacer(modifier = Modifier.height(10.dp)) }
                                items(uiState.schoolPlusImages?.images!!){ uri ->
                                    SchoolPhotos(photo = uri)
                                }
                            }
                            3 -> {
                                item(span = { GridItemSpan(3) }) { Spacer(modifier = Modifier.height(10.dp)) }
                                items(uiState.schoolPlusImages?.school!!.courses, span = { GridItemSpan(3) }) { course ->
                                    ListItem(headlineContent = { Text(text = course) })
                                }
                            }
                            4 -> {
                                item(span = { GridItemSpan(3) }){ SchoolBasicInfo(label = "Mission", value = uiState.schoolPlusImages?.school!!.mission) }
                            }
                            5 -> {
                                item(span = { GridItemSpan(3) }) { SchoolBasicInfo(label = "Vision", value = uiState.schoolPlusImages?.school!!.vision) }
                            }
                            6 -> {
                                item(span = { GridItemSpan(3) }) { SchoolBasicInfo(label = "Core Values", value = uiState.schoolPlusImages?.school!!.coreValues) }
                            }
                        }
                    }
                    ConfirmDeleteSchoolDialog(
                        show = uiState.showDeleteDialog,
                        onDismissRequest = schoolViewModel::toggleDeleteDialog,
                        deleteSchool = schoolViewModel::deleteSchool,
                        loading = uiState.deleteLoading
                    )
                }
            }
        }
    }
}
