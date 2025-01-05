package com.universitinder.app.matched

import android.content.Intent
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.universitinder.app.controllers.SchoolController
import com.universitinder.app.faq.FAQActivity
import com.universitinder.app.models.SchoolAnalytics
import com.universitinder.app.models.SchoolAnalyticsYears
import com.universitinder.app.models.SchoolPlusImages
import com.universitinder.app.models.StudentByYear
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import com.universitinder.app.helpers.ActivityStarterHelper

class MatchedViewModel(
    val school: SchoolPlusImages,
    private val activityStarterHelper: ActivityStarterHelper,
    private val schoolController : SchoolController = SchoolController(),
    val popActivity: () -> Unit
): ViewModel(){
    private val _uiState = MutableStateFlow(MatchedUiState())
    val uiState : StateFlow<MatchedUiState> = _uiState.asStateFlow()

    init {
        if (school.images.isNotEmpty()) {
            val logo = school.images.first { it.lastPathSegment!!.split("/")[2].contains("logo") }
            _uiState.value = _uiState.value.copy(logo = logo)
        }
        viewModelScope.launch {
            val schoolAnalyticsList = schoolController.getSchoolAnalytics(school.school!!.documentID)
            withContext(Dispatchers.Main) {
                _uiState.value = _uiState.value.copy(
                    schoolPlusImages = school,
                    schoolAnalyticsList = schoolAnalyticsList,
                )
            }
            onYearLevelSelected(SchoolAnalyticsYears.ALL.toString())
        }
    }

    fun onYearLevelSelected(newVal: String) {
        if (_uiState.value.schoolAnalyticsList.isEmpty()) {
            _uiState.value = _uiState.value.copy(
                selectedYear = newVal,
                schoolAnalytics = SchoolAnalytics()
            )
        } else {
            when(SchoolAnalyticsYears.valueOf(newVal)) {
                SchoolAnalyticsYears.FIRST -> {
                    _uiState.value = _uiState.value.copy(
                        selectedYear = newVal,
                        schoolAnalytics = _uiState.value.schoolAnalyticsList.first { it.year == SchoolAnalyticsYears.FIRST }
                    )
                }
                SchoolAnalyticsYears.SECOND -> {
                    _uiState.value = _uiState.value.copy(
                        selectedYear = newVal,
                        schoolAnalytics = _uiState.value.schoolAnalyticsList.first { it.year == SchoolAnalyticsYears.SECOND }
                    )
                }
                SchoolAnalyticsYears.THIRD -> {
                    _uiState.value = _uiState.value.copy(
                        selectedYear = newVal,
                        schoolAnalytics = _uiState.value.schoolAnalyticsList.first { it.year == SchoolAnalyticsYears.THIRD }
                    )
                }
                SchoolAnalyticsYears.FOURTH -> {
                    _uiState.value = _uiState.value.copy(
                        selectedYear = newVal,
                        schoolAnalytics = _uiState.value.schoolAnalyticsList.first { it.year == SchoolAnalyticsYears.FOURTH }
                    )
                }
                SchoolAnalyticsYears.FIFTH -> {
                    _uiState.value = _uiState.value.copy(
                        selectedYear = newVal,
                        schoolAnalytics = _uiState.value.schoolAnalyticsList.first { it.year == SchoolAnalyticsYears.FIFTH }
                    )
                }
                SchoolAnalyticsYears.ALL -> {
                    val aggregatedAnalytics = _uiState.value.schoolAnalyticsList.reduce { acc, analytics ->
                        SchoolAnalytics(
                            year = SchoolAnalyticsYears.ALL,
                            students = acc.students + analytics.students,
                            faculty = acc.faculty + analytics.faculty,
                            admitted = acc.admitted + analytics.admitted,
                            applicants = acc.applicants + analytics.applicants,
                            graduates = acc.graduates + analytics.graduates
                        )
                    }

                    val aggregatedStudentByYear = _uiState.value.schoolAnalyticsList
                        .flatMap { it.studentByYear }
                        .groupBy { it.year }
                        .map { (year, studentList) ->
                            StudentByYear(
                                year = year,
                                students = studentList.sumOf { it.students }
                            )
                        }

                    _uiState.value = _uiState.value.copy(
                        selectedYear = newVal,
                        schoolAnalytics = aggregatedAnalytics.copy(
                            admissionRate = _uiState.value.schoolAnalyticsList.first { it.year == SchoolAnalyticsYears.FIRST }.admissionRate,
                            graduationRate = _uiState.value.schoolAnalyticsList.first { it.year == SchoolAnalyticsYears.FOURTH }.graduationRate,
                            studentByYear = aggregatedStudentByYear
                        )
                    )
                }
            }
        }
    }

    fun onTabChange(newVal: Int) { _uiState.value = _uiState.value.copy(activeTab = newVal) }

    fun startFAQActivity() {
        val intent = Intent(activityStarterHelper.getContext(), FAQActivity::class.java)
        intent.putExtra("schoolID", school.id)
        activityStarterHelper.startActivity(intent)
    }
}