package com.universitinder.app.school

import android.content.Intent
import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.universitinder.app.controllers.SchoolController
import com.universitinder.app.models.School
import com.universitinder.app.models.SchoolAnalytics
import com.universitinder.app.models.SchoolAnalyticsYears
import com.universitinder.app.models.StudentByYear
import com.universitinder.app.models.UserState
import com.universitinder.app.models.UserType
import com.universitinder.app.helpers.ActivityStarterHelper
import com.universitinder.app.school.schoolInformationNavigation.SchoolInformationNavigationActivity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlin.math.round

class SchoolViewModel(
    private val school: School,
    private val schoolController: SchoolController,
    private val activityStarterHelper: ActivityStarterHelper,
    val popActivity: () -> Unit,
): ViewModel(){
    private val currentUser = UserState.currentUser
    private val _uiState = MutableStateFlow(SchoolUiState())
    val uiState : StateFlow<SchoolUiState> = _uiState.asStateFlow()

    init {
        if (currentUser != null && currentUser.type == UserType.INSTITUTION) {
            refresh()
        }
    }

    fun toggleDeleteDialog() { _uiState.value = _uiState.value.copy(showDeleteDialog = !_uiState.value.showDeleteDialog) }

    fun refresh() {
        if (currentUser != null) {
            viewModelScope.launch(Dispatchers.IO) {
                withContext(Dispatchers.Main) { _uiState.value = _uiState.value.copy(fetchingLoading = true) }
                val schoolPlusImages = schoolController.getSchoolPlusImageByDocumentID(school.documentID)
                val schoolAnalyticsList = schoolController.getSchoolAnalytics(school.documentID)
                if (schoolPlusImages == null) {
                    withContext(Dispatchers.Main) { _uiState.value = _uiState.value.copy(fetchingLoading = false) }
                    return@launch
                } else {
                    var logo : Uri? = null
                    if (schoolPlusImages.images.isNotEmpty() && schoolPlusImages.images.any { it.lastPathSegment!!.split("/")[2].contains("logo") }) {
                        logo = schoolPlusImages.images.first { it.lastPathSegment!!.split("/")[2].contains("logo") }
                    }
                    withContext(Dispatchers.Main) {
                        _uiState.value = _uiState.value.copy(
                            fetchingLoading = false,
                            schoolPlusImages = schoolPlusImages,
                            schoolAnalyticsList = schoolAnalyticsList,
                            logo = logo
                        )
                    }
                    onYearLevelSelected(SchoolAnalyticsYears.ALL.toString())
                }
            }
        }
    }

    fun onYearLevelSelected(newVal: String) {
        if (_uiState.value.schoolAnalyticsList.isEmpty()) {
            _uiState.value = _uiState.value.copy(
                selectedYear = newVal,
                schoolAnalytics = SchoolAnalytics()
            )
        } else {
            val selectedYear = SchoolAnalyticsYears.valueOf(newVal)
            val selectedAnalytics = _uiState.value.schoolAnalyticsList.firstOrNull { it.year == selectedYear }

            if (selectedAnalytics != null) {
                val admissionRate = if (selectedAnalytics.applicants > 0) {
                    round((selectedAnalytics.admitted.toFloat() / selectedAnalytics.applicants) * 100 * 100) / 100
                } else {
                    0.0f
                }

                val graduationRate = if (selectedAnalytics.students > 0) {
                    round((selectedAnalytics.graduates.toFloat() / selectedAnalytics.students) * 100 * 100) / 100
                } else {
                    0.0f
                }

                _uiState.value = _uiState.value.copy(
                    selectedYear = newVal,
                    schoolAnalytics = selectedAnalytics.copy(
                        admissionRate = admissionRate,
                        graduationRate = graduationRate
                    )
                )
            } else if (selectedYear == SchoolAnalyticsYears.ALL) {
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

                val admissionRate = if (aggregatedAnalytics.applicants > 0) {
                    round((aggregatedAnalytics.admitted.toFloat() / aggregatedAnalytics.applicants) * 100 * 100) / 100
                } else {
                    0.0f
                }

                val graduationRate = if (aggregatedAnalytics.students > 0) {
                    round((aggregatedAnalytics.graduates.toFloat() / aggregatedAnalytics.students) * 100 * 100) / 100
                } else {
                    0.0f
                }

                _uiState.value = _uiState.value.copy(
                    selectedYear = newVal,
                    schoolAnalytics = aggregatedAnalytics.copy(
                        admissionRate = admissionRate,
                        graduationRate = graduationRate,
                        studentByYear = aggregatedStudentByYear
                    )
                )
            }
        }
    }

    fun deleteSchool() {
        viewModelScope.launch(Dispatchers.IO) {
            withContext(Dispatchers.Main) { _uiState.value = _uiState.value.copy(deleteLoading = true) }
            schoolController.deleteSchool(school.documentID)
            withContext(Dispatchers.Main) { _uiState.value = _uiState.value.copy(deleteLoading = false, showDeleteDialog = false) }
            popActivity()
        }
    }

    fun onTabChange(newVal: Int) { _uiState.value = _uiState.value.copy(activeTab = newVal) }

    fun startInformationNavigation() {
        val intent = Intent(activityStarterHelper.getContext(), SchoolInformationNavigationActivity::class.java)
        intent.putExtra("SCHOOL", school)
        activityStarterHelper.startActivity(intent)
    }
}