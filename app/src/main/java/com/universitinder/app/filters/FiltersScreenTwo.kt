package com.universitinder.app.filters

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.rememberPagerState
import com.universitinder.app.filters.affordability.AffordabilityFilterScreen
import com.universitinder.app.filters.city.CityFilterScreen
import com.universitinder.app.filters.courses.CoursesFilterScreen
import com.universitinder.app.filters.duration.DurationFilterScreen
import com.universitinder.app.filters.privatePublic.PrivatePublicFilterScreen
import com.universitinder.app.filters.province.ProvinceFilterScreen
import com.universitinder.app.models.COURSE_DURATION
import com.universitinder.app.models.ResultMessageType
import kotlin.text.toIntOrNull

@OptIn(ExperimentalMaterial3Api::class, ExperimentalPagerApi::class)
@Composable
fun FiltersScreenTwo(filtersViewModel: FiltersViewModel) {
    val uiState by filtersViewModel.uiState.collectAsState()
    val pagerState = rememberPagerState(initialPage = 0)
    var selectedTab by remember { mutableIntStateOf(1) }

    LaunchedEffect(key1 = selectedTab) {
        pagerState.animateScrollToPage(selectedTab)
    }

    LaunchedEffect(key1 = pagerState.currentPage, pagerState.isScrollInProgress) {
        if (!pagerState.isScrollInProgress)
            selectedTab = pagerState.currentPage
    }

    Scaffold (
        topBar = {
            Column {
                TopAppBar(
                    title = { Text(text = "Filters") },
                    navigationIcon = {
                        IconButton(onClick = { filtersViewModel.popActivity() }) {
                            Icon(Icons.Filled.ArrowBack, contentDescription = "Go Back")
                        }
                    },
                    actions = {
                        TextButton(onClick = { filtersViewModel.clear() }) {
                            Text(text = "Clear")
                        }
                    }
                )
                LazyRow {
                    item {
                        TextButton(
                            onClick = { selectedTab = 1 },
                            colors = ButtonDefaults.textButtonColors(
                                containerColor = if(selectedTab == 1) MaterialTheme.colorScheme.primaryContainer else Color.Transparent
                            )
                        ) { Text(text = "City/Municipality")}
                        TextButton(
                            onClick = { selectedTab = 2 },
                            colors = ButtonDefaults.textButtonColors(
                                containerColor = if(selectedTab == 2) MaterialTheme.colorScheme.primaryContainer else Color.Transparent
                            )
                        ) { Text(text = "Private/Public")}
                        TextButton(
                            onClick = { selectedTab = 3 },
                            colors = ButtonDefaults.textButtonColors(
                                containerColor = if(selectedTab == 3) MaterialTheme.colorScheme.primaryContainer else Color.Transparent
                            )
                        ) { Text(text = "Affordability")}
                        TextButton(
                            onClick = { selectedTab = 4 },
                            colors = ButtonDefaults.textButtonColors(
                                containerColor = if(selectedTab == 4) MaterialTheme.colorScheme.primaryContainer else Color.Transparent
                            )
                        ) { Text(text = "Courses")}
                        TextButton(
                            onClick = { selectedTab = 5 },
                            colors = ButtonDefaults.textButtonColors(
                                containerColor = if(selectedTab == 5) MaterialTheme.colorScheme.primaryContainer else Color.Transparent
                            )
                        ) { Text(text = "Course Duration")}
                    }
                }
            }
        },
        bottomBar = {
            Column {
                if (uiState.resultMessage.show)
                    Text(text = uiState.resultMessage.message, color = if (uiState.resultMessage.type == ResultMessageType.FAILED) Color.Red else MaterialTheme.colorScheme.primary, textAlign = TextAlign.Center, fontSize = 12.sp)
                Button(
                    onClick = filtersViewModel::save,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp)
                ) {
                    if (uiState.saveLoading) {
                        CircularProgressIndicator(color = Color.White)
                    } else {
                        Text(text = "Save")
                    }
                }
            }
        }
    ){ innerPadding ->
        HorizontalPager(
            modifier = Modifier.padding(innerPadding),
            state = pagerState,
            count = 6
        ) {
            when (selectedTab) {
                0 -> {
                    ProvinceFilterScreen(
                        provinces = uiState.provinces,
                        checkedProvinces = uiState.checkedProvinces,
                        provinceOnCheckChange = filtersViewModel::onProvinceCheckChange
                    )
                }
                1 -> {
                    CityFilterScreen(
                        cities = uiState.cities,
                        checkedCities = uiState.checkedCities,
                        onCheckChange = filtersViewModel::onCityCheckChange
                    )
                }
                2 -> {
                    PrivatePublicFilterScreen(
                        values = uiState.privatePublic,
                        checkedValues = uiState.checkedPrivatePublic,
                        onCheckChange = filtersViewModel::onPrivatePublicCheckChange
                    )
                }
                3 -> {
                    AffordabilityFilterScreen(
                        minimum = uiState.minimum,
                        onMinimumChange = filtersViewModel::onMinimumChange,
                        maximum = uiState.maximum,
                        onMaximumChange = filtersViewModel::onMaximumChange,
                        affordability = uiState.affordability
                    )
                }
                4 -> {
                    CoursesFilterScreen(
                        courses = uiState.courses,
                        checkedCourses = uiState.checkedCourses,
                        onCourseCheckChange = filtersViewModel::onCoursesCheckChange
                    )
                }
                5 -> {
                    DurationFilterScreen(
                        durations = COURSE_DURATION,
                        checkedDurations = uiState.checkedDurations,
                        onCheckChange = filtersViewModel::onDurationCheckChange
                    )
                }
            }
        }
    }
}