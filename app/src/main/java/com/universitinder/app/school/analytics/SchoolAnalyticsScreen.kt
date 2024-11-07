package com.universitinder.app.school.analytics

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.rememberPagerState
import com.universitinder.app.models.ResultMessageType
import com.universitinder.app.models.SchoolAnalytics
import com.universitinder.app.models.SchoolAnalyticsYears
import com.universitinder.app.models.StudentByYear

@OptIn(ExperimentalPagerApi::class, ExperimentalMaterial3Api::class)
@Composable
fun SchoolAnalyticsScreen(viewModel: SchoolAnalyticsViewModel) {
    val uiState by viewModel.uiState.collectAsState()
    val pagerState = rememberPagerState(initialPage = 0)
    var selectedTab by remember { mutableIntStateOf(0) }

    LaunchedEffect(key1 = selectedTab) {
        pagerState.animateScrollToPage(selectedTab)
    }

    LaunchedEffect(key1 = pagerState.currentPage, pagerState.isScrollInProgress) {
        if (!pagerState.isScrollInProgress)
            selectedTab = pagerState.currentPage
    }

    Scaffold(
        bottomBar = {
            Column {
                if (uiState.resultMessage.show)
                    Text(
                        text = uiState.resultMessage.message,
                        color = if (uiState.resultMessage.type == ResultMessageType.FAILED) Color.Red else MaterialTheme.colorScheme.primary,
                        textAlign = TextAlign.Center,
                        fontSize = 12.sp
                    )
                Button(
                    onClick = viewModel::save,
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
        },
        topBar = {
            Column {
                TopAppBar(
                    title = { Text(text = "Analytics") },
                    navigationIcon = {
                        IconButton(onClick = { viewModel.popActivity() }) {
                            Icon(Icons.Filled.ArrowBack, contentDescription = "Go Back")
                        }
                    },
                )
                LazyRow(
                    modifier = Modifier.padding(horizontal = 20.dp)
                ) {
                    item {
                        TextButton(
                            onClick = { selectedTab = 0 },
                            colors = ButtonDefaults.textButtonColors(
                                containerColor = if(selectedTab == 0) MaterialTheme.colorScheme.primaryContainer else Color.Transparent
                            )
                        ) { Text(text = "1st Year")}
                        TextButton(
                            onClick = { selectedTab = 1 },
                            colors = ButtonDefaults.textButtonColors(
                                containerColor = if(selectedTab == 1) MaterialTheme.colorScheme.primaryContainer else Color.Transparent
                            )
                        ) { Text(text = "2nd Year")}
                        TextButton(
                            onClick = { selectedTab = 2 },
                            colors = ButtonDefaults.textButtonColors(
                                containerColor = if(selectedTab == 2) MaterialTheme.colorScheme.primaryContainer else Color.Transparent
                            )
                        ) { Text(text = "3rd Year")}
                        TextButton(
                            onClick = { selectedTab = 3 },
                            colors = ButtonDefaults.textButtonColors(
                                containerColor = if(selectedTab == 3) MaterialTheme.colorScheme.primaryContainer else Color.Transparent
                            )
                        ) { Text(text = "4th Year")}
                        TextButton(
                            onClick = { selectedTab = 4 },
                            colors = ButtonDefaults.textButtonColors(
                                containerColor = if(selectedTab == 4) MaterialTheme.colorScheme.primaryContainer else Color.Transparent
                            )
                        ) { Text(text = "5th Year")}
                    }
                }
            }
        }
    ){ innerPadding ->
        when (uiState.fetchLoading) {
            true -> {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator()
                }
            }
            false -> {
                HorizontalPager(
                    modifier = Modifier.padding(innerPadding),
                    state = pagerState,
                    count = 5
                ) {
                    when (selectedTab) {
                        0 -> {
                            AnalyticsForm(
                                schoolAnalytics = uiState.firstYear,
                                onStudentsChange = viewModel::onFirstYearStudentsChange,
                                onFacultyChange = viewModel::onFirstYearFacultyChange,
                                onApplicantsChange = viewModel::onFirstYearApplicantsChange,
                                onAdmittedChange = viewModel::onFirstYearAdmittedChange,
                                onGraduatesChange = viewModel::onFirstYearGraduatesChange,
                                addYear = viewModel::addYear,
                                removeYear = viewModel::removeYear,
                                updateYearStudentByYear = viewModel::updateYearStudentByYear,
                                updateNumberStudentByYear = viewModel::updateNumberStudentByYear
                            )
                        }
                        1 -> {
                            AnalyticsForm(
                                schoolAnalytics = uiState.secondYear,
                                onStudentsChange = viewModel::onSecondYearStudentsChange,
                                onFacultyChange = viewModel::onSecondYearFacultyChange,
                                onApplicantsChange = viewModel::onSecondYearApplicantsChange,
                                onAdmittedChange = viewModel::onSecondYearAdmittedChange,
                                onGraduatesChange = viewModel::onSecondYearGraduatesChange,
                                addYear = viewModel::addYear,
                                removeYear = viewModel::removeYear,
                                updateYearStudentByYear = viewModel::updateYearStudentByYear,
                                updateNumberStudentByYear = viewModel::updateNumberStudentByYear
                            )
                        }
                        2 -> {
                            AnalyticsForm(
                                schoolAnalytics = uiState.thirdYear,
                                onStudentsChange = viewModel::onThirdYearStudentsChange,
                                onFacultyChange = viewModel::onThirdYearFacultyChange,
                                onApplicantsChange = viewModel::onThirdYearApplicantsChange,
                                onAdmittedChange = viewModel::onThirdYearAdmittedChange,
                                onGraduatesChange = viewModel::onThirdYearGraduatesChange,
                                addYear = viewModel::addYear,
                                removeYear = viewModel::removeYear,
                                updateYearStudentByYear = viewModel::updateYearStudentByYear,
                                updateNumberStudentByYear = viewModel::updateNumberStudentByYear
                            )
                        }
                        3 -> {
                            AnalyticsForm(
                                schoolAnalytics = uiState.fourthYear,
                                onStudentsChange = viewModel::onFourthYearStudentsChange,
                                onFacultyChange = viewModel::onFourthYearFacultyChange,
                                onApplicantsChange = viewModel::onFourthYearApplicantsChange,
                                onAdmittedChange = viewModel::onFourthYearAdmittedChange,
                                onGraduatesChange = viewModel::onFourthYearGraduatesChange,
                                addYear = viewModel::addYear,
                                removeYear = viewModel::removeYear,
                                updateYearStudentByYear = viewModel::updateYearStudentByYear,
                                updateNumberStudentByYear = viewModel::updateNumberStudentByYear
                            )
                        }
                        4 -> {
                            AnalyticsForm(
                                schoolAnalytics = uiState.fifthYear,
                                onStudentsChange = viewModel::onFifthYearStudentsChange,
                                onFacultyChange = viewModel::onFifthYearFacultyChange,
                                onApplicantsChange = viewModel::onFifthYearApplicantsChange,
                                onAdmittedChange = viewModel::onFifthYearAdmittedChange,
                                onGraduatesChange = viewModel::onFifthYearGraduatesChange,
                                addYear = viewModel::addYear,
                                removeYear = viewModel::removeYear,
                                updateYearStudentByYear = viewModel::updateYearStudentByYear,
                                updateNumberStudentByYear = viewModel::updateNumberStudentByYear
                            )
                        }
                    }
                }
            }
        }
    }
}


/**
data class SchoolAnalytics(
val documentID: String = "",
val year: SchoolAnalyticsYears = SchoolAnalyticsYears.FIRST,
val students: Int = 0,
val faculty: Int = 0,
val applicants: Int = 0,
val admitted: Int = 0,
val admissionRate: Float = 0.0f,
val graduates: Int = 0,
val graduationRate: Float = 0.0f,
val studentByYear: List<StudentByYear> = listOf()
)
 */
@Composable
fun AnalyticsForm(
    schoolAnalytics: SchoolAnalytics,
    onStudentsChange: (newVal: String) -> Unit,
    onFacultyChange: (newVal: String) -> Unit,
    onApplicantsChange: (newVal: String) -> Unit,
    onAdmittedChange: (newVal: String) -> Unit,
    onGraduatesChange: (newVal: String) -> Unit,
    addYear: (year: SchoolAnalyticsYears) -> Unit,
    removeYear: (year: SchoolAnalyticsYears, index: Int) -> Unit,
    updateNumberStudentByYear: (year: SchoolAnalyticsYears, index: Int, newVal: String) -> Unit,
    updateYearStudentByYear: (year: SchoolAnalyticsYears, index: Int, newVal: String) -> Unit,
) {
    LazyColumn(
        modifier = Modifier.padding(20.dp)
    ) {
        item {
            OutlinedTextField(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 10.dp),
                label = { Text(text = "Number of Students") },
                keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number),
                value = schoolAnalytics.students.toString(),
                onValueChange = onStudentsChange,
            )
        }
        item {
            OutlinedTextField(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 10.dp),
                label = { Text(text = "Number of Faculty Members") },
                keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number),
                value = schoolAnalytics.faculty.toString(),
                onValueChange = onFacultyChange,
            )
        }
        item {
            OutlinedTextField(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 10.dp),
                label = { Text(text = "Number of Applicants") },
                keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number),
                value = schoolAnalytics.applicants.toString(),
                onValueChange = onApplicantsChange,
            )
        }
        item {
            OutlinedTextField(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 10.dp),
                label = { Text(text = "Number of Admitted") },
                keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number),
                value = schoolAnalytics.admitted.toString(),
                onValueChange = onAdmittedChange,
            )
        }
        item {
            OutlinedTextField(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 10.dp),
                label = { Text(text = "Number of Graduates") },
                keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number),
                value = schoolAnalytics.graduates.toString(),
                onValueChange = onGraduatesChange,
            )
        }
        item {
            StudentByYearForm(
                year = schoolAnalytics.year,
                studentByYearList = schoolAnalytics.studentByYear,
                addYear = addYear,
                removeYear = removeYear,
                updateNumberStudentByYear = updateNumberStudentByYear,
                updateYearStudentByYear = updateYearStudentByYear
            )
        }
    }
}


@Composable
fun StudentByYearForm(
    year: SchoolAnalyticsYears,
    studentByYearList: List<StudentByYear>,
    addYear: (year: SchoolAnalyticsYears) -> Unit,
    removeYear: (year: SchoolAnalyticsYears, index: Int) -> Unit,
    updateNumberStudentByYear: (year: SchoolAnalyticsYears, index: Int, newVal: String) -> Unit,
    updateYearStudentByYear: (year: SchoolAnalyticsYears, index: Int, newVal: String) -> Unit,
) {
    Column(
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally
    ){
        studentByYearList.forEachIndexed { index, studentByYear ->
            Row(
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.fillMaxWidth(0.92f)
                ){
                    OutlinedTextField(
                        modifier = Modifier
                            .fillMaxWidth(0.5f)
                            .padding(vertical = 10.dp),
                        label = { Text(text = "Year") },
                        keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number),
                        value = studentByYear.year,
                        onValueChange = { updateYearStudentByYear(year, index, it) },
                    )
                    Spacer(modifier = Modifier.width(10.dp))
                    OutlinedTextField(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 10.dp),
                        label = { Text(text = "Number of Students") },
                        keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number),
                        value = studentByYear.students.toString(),
                        onValueChange = { updateNumberStudentByYear(year, index, it) },
                    )
                }
                IconButton(onClick = { removeYear(year, index) }) {
                    Icon(Icons.Default.Close, contentDescription = "Delete")
                }
            }
        }
        Button(onClick = { addYear(year) }) {
            Text(text = "Add Year")
        }
    }
}