package com.universitinder.app.components

import androidx.compose.animation.core.EaseInOutCubic
import androidx.compose.animation.core.tween
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.universitinder.app.models.SchoolAnalytics
import com.universitinder.app.models.SchoolAnalyticsYears
import com.universitinder.app.models.StudentByYear
import ir.ehsannarmani.compose_charts.LineChart
import ir.ehsannarmani.compose_charts.models.AnimationMode
import ir.ehsannarmani.compose_charts.models.DotProperties
import ir.ehsannarmani.compose_charts.models.DrawStyle
import ir.ehsannarmani.compose_charts.models.LabelProperties
import ir.ehsannarmani.compose_charts.models.Line

@Composable
fun Analytics(lineChartLoading: Boolean, schoolAnalytics: SchoolAnalytics, selectedYearLevel: String, onYearLevelSelected: (yearLevel: String) -> Unit) {
    var expandYearLevelMenu by remember {
        mutableStateOf(false)
    }

    Column{
        Box(modifier = Modifier
            .padding(vertical = 10.dp)
            .fillMaxWidth()) {
            Column {
                Text(text = "Year Level")
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .border(
                            width = 0.5.dp,
                            color = Color.Black,
                            shape = CircleShape.copy(
                                CornerSize(5.dp)
                            )
                        )
                        .clickable { expandYearLevelMenu = !expandYearLevelMenu },
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        modifier = Modifier.padding(vertical = 16.dp, horizontal = 18.dp),
                        text = selectedYearLevel
                    )
                    Icon(Icons.Filled.ArrowDropDown, contentDescription = "Dropdown Arrow")
                }
            }
        }
        PopUpDropDown(
            label = "Year Level",
            items = SchoolAnalyticsYears.entries.map { it.toString() },
            show = expandYearLevelMenu,
            onDismissRequest = {expandYearLevelMenu = false},
            onItemSelected = onYearLevelSelected
        )
        Row {
            AnalyticsFigureWithTitle(title = "Students", figure = schoolAnalytics.students.toFloat(), modifier = Modifier.fillMaxWidth(0.5f))
            Spacer(modifier = Modifier.width(8.dp))
            AnalyticsFigureWithTitle(title = "Faculty", figure = schoolAnalytics.faculty.toFloat(), modifier = Modifier.fillMaxWidth())
        }
        Row {
            AnalyticsFigureWithTitle(title = "Applicants", figure = schoolAnalytics.applicants.toFloat(), modifier = Modifier.fillMaxWidth(0.5f))
            Spacer(modifier = Modifier.width(8.dp))
            AnalyticsFigureWithTitle(title = "Admitted", figure = schoolAnalytics.admitted.toFloat(), modifier = Modifier.fillMaxWidth())
        }
        Row {
            AnalyticsDonutPercentage(title = "Admission Rate", figure = schoolAnalytics.admissionRate, modifier = Modifier.fillMaxWidth())
        }
        Row {
            AnalyticsFigureWithTitle(title = "Graduates", figure = schoolAnalytics.graduates.toFloat(), modifier = Modifier.fillMaxWidth())
        }
        Row {
            AnalyticsDonutPercentage(title = "Graduation Rate", figure = schoolAnalytics.graduationRate, modifier = Modifier.fillMaxWidth())
        }
        Row {
            AnalyticsStudentsPerYearLineChart(
                studentPerYearList = schoolAnalytics.studentByYear,
                loading = lineChartLoading,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(300.dp))
        }
    }
}

@Composable
fun AnalyticsFigureWithTitle(title: String, figure: Float, modifier: Modifier, inPercent: Boolean = false) {
    Card(
        modifier = modifier.padding(vertical = 4.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ){
        Column(
            modifier = Modifier.padding(20.dp)
        ) {
            Text(text = title)
            Text(
                text = if (inPercent) "$figure%" else figure.toString(),
                style = TextStyle(fontSize = 24.sp, fontWeight = FontWeight.SemiBold)
            )
        }
    }
}

@Composable
fun AnalyticsDonutPercentage(title: String, figure: Float, modifier: Modifier) {
    Card(
        modifier = modifier.padding(vertical = 4.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        Column(
            modifier = Modifier.padding(20.dp)
        ){
            Text(text = title)
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                StyledCircularProgressIndicator(progress = figure, strokeWidth = 20.dp)
                Text(text = "$figure%", style = TextStyle(fontWeight = FontWeight.SemiBold, fontSize = 32.sp))
            }
        }
    }
}

@Composable
fun StyledCircularProgressIndicator(
    progress: Float,               // progress between 0f and 1f
    indicatorColor: Color = MaterialTheme.colorScheme.primary,  // Customizable indicator color
    trackColor: Color = Color.LightGray,      // Track color
    strokeWidth: Dp = 8.dp               // Thickness of the progress indicator
) {
    Box(contentAlignment = Alignment.Center) {
        // Gray background track
        CircularProgressIndicator(
            progress = 1f,  // Full circle for the track
            color = trackColor,
            strokeWidth = strokeWidth,
            modifier = Modifier.size(162.dp)
        )
        // Foreground progress indicator
        CircularProgressIndicator(
            progress = progress,
            color = indicatorColor,
            strokeWidth = strokeWidth,
            modifier = Modifier.size(162.dp)
        )
    }
}

@Composable
fun AnalyticsStudentsPerYearLineChart(loading: Boolean, studentPerYearList: List<StudentByYear>, modifier: Modifier) {
    val sorted = studentPerYearList.sortedBy { it.year }
    val labelValues = sorted.map { it.year }
    val dataValues = sorted.map { it.students }

    Card(
        modifier = modifier.padding(vertical = 4.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ){
        Column(
            modifier = Modifier.padding(20.dp)
        ) {
            Text(text = "Students Per Year")
            when (dataValues.isEmpty()) {
                true -> {
                    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        Text(text = "No Data to Show")
                    }
                }
                false -> {
                    when (loading) {
                        true -> {
                            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                                CircularProgressIndicator()
                            }
                        }
                        false -> {
                            Spacer(modifier = Modifier.height(10.dp))
                            LineChart(
                                modifier = Modifier
                                    .fillMaxSize()
                                    .padding(horizontal = 22.dp,),
                                labelProperties = LabelProperties(
                                    enabled = labelValues.isNotEmpty(),
                                    labels = labelValues
                                ),
                                data =
                                    listOf(
                                        Line(
                                            label = "Students",
                                            values = dataValues.map { it.toDouble() },
                                            color = SolidColor(MaterialTheme.colorScheme.primary),
                                            firstGradientFillColor = MaterialTheme.colorScheme.primary.copy(alpha = .5f),
                                            secondGradientFillColor = Color.Transparent,
                                            dotProperties = DotProperties(
                                                enabled = true,
                                                color = SolidColor(Color.White),
                                                strokeWidth = 4.dp,
                                                radius = 7.dp,
                                                strokeColor = SolidColor(MaterialTheme.colorScheme.primary),
                                            ),
                                            strokeAnimationSpec = tween(2000, easing = EaseInOutCubic),
                                            gradientAnimationDelay = 1000,
                                            drawStyle = DrawStyle.Stroke(width = 2.dp),
                                        )
                                    ),
                                animationMode = AnimationMode.Together(delayBuilder = {
                                    it * 500L
                                }),
                            )
                        }
                    }
                }
            }
        }
    }
}