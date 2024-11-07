package com.universitinder.app.school.analytics

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.universitinder.app.controllers.SchoolController
import com.universitinder.app.md5
import com.universitinder.app.models.School
import com.universitinder.app.models.SchoolAnalyticsYears
import com.universitinder.app.models.StudentByYear
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class SchoolAnalyticsViewModel(
    private val school: School,
    private val schoolController: SchoolController,
    val popActivity: () -> Unit
) : ViewModel(){
    private val _uiState = MutableStateFlow(SchoolAnalyticsUiState())
    val uiState : StateFlow<SchoolAnalyticsUiState> = _uiState.asStateFlow()

    init {
        getAnalytics()
    }

    fun onFirstYearStudentsChange(newVal: String) {
        _uiState.value = _uiState.value.copy(firstYear = _uiState.value.firstYear.copy(students = if (newVal != "") newVal.toInt() else 0))
    }
    fun onFirstYearFacultyChange(newVal: String) {
        _uiState.value = _uiState.value.copy(firstYear = _uiState.value.firstYear.copy(faculty = if (newVal != "") newVal.toInt() else 0))
    }
    fun onFirstYearApplicantsChange(newVal: String) {
        _uiState.value = _uiState.value.copy(firstYear = _uiState.value.firstYear.copy(applicants = if (newVal != "") newVal.toInt() else 0))
    }
    fun onFirstYearAdmittedChange(newVal: String) {
        _uiState.value = _uiState.value.copy(firstYear = _uiState.value.firstYear.copy(admitted = if (newVal != "") newVal.toInt() else 0))
    }
    fun onFirstYearGraduatesChange(newVal: String) {
        _uiState.value = _uiState.value.copy(firstYear = _uiState.value.firstYear.copy(graduates = if (newVal != "") newVal.toInt() else 0))
    }

    fun onSecondYearStudentsChange(newVal: String) {
        _uiState.value = _uiState.value.copy(secondYear = _uiState.value.secondYear.copy(students = if (newVal != "") newVal.toInt() else 0))
    }
    fun onSecondYearFacultyChange(newVal: String) {
        _uiState.value = _uiState.value.copy(secondYear = _uiState.value.secondYear.copy(faculty = if (newVal != "") newVal.toInt() else 0))
    }
    fun onSecondYearApplicantsChange(newVal: String) {
        _uiState.value = _uiState.value.copy(secondYear = _uiState.value.secondYear.copy(applicants = if (newVal != "") newVal.toInt() else 0))
    }
    fun onSecondYearAdmittedChange(newVal: String) {
        _uiState.value = _uiState.value.copy(secondYear = _uiState.value.secondYear.copy(admitted = if (newVal != "") newVal.toInt() else 0))
    }
    fun onSecondYearGraduatesChange(newVal: String) {
        _uiState.value = _uiState.value.copy(secondYear = _uiState.value.secondYear.copy(graduates = if (newVal != "") newVal.toInt() else 0))
    }

    fun onThirdYearStudentsChange(newVal: String) {
        _uiState.value = _uiState.value.copy(thirdYear = _uiState.value.thirdYear.copy(students = if (newVal != "") newVal.toInt() else 0))
    }
    fun onThirdYearFacultyChange(newVal: String) {
        _uiState.value = _uiState.value.copy(thirdYear = _uiState.value.thirdYear.copy(faculty = if (newVal != "") newVal.toInt() else 0))
    }
    fun onThirdYearApplicantsChange(newVal: String) {
        _uiState.value = _uiState.value.copy(thirdYear = _uiState.value.thirdYear.copy(applicants = if (newVal != "") newVal.toInt() else 0))
    }
    fun onThirdYearAdmittedChange(newVal: String) {
        _uiState.value = _uiState.value.copy(thirdYear = _uiState.value.thirdYear.copy(admitted = if (newVal != "") newVal.toInt() else 0))
    }
    fun onThirdYearGraduatesChange(newVal: String) {
        _uiState.value = _uiState.value.copy(thirdYear = _uiState.value.thirdYear.copy(graduates = if (newVal != "") newVal.toInt() else 0))
    }

    fun onFourthYearStudentsChange(newVal: String) {
        _uiState.value = _uiState.value.copy(fourthYear = _uiState.value.fourthYear.copy(students = if (newVal != "") newVal.toInt() else 0))
    }
    fun onFourthYearFacultyChange(newVal: String) {
        _uiState.value = _uiState.value.copy(fourthYear = _uiState.value.fourthYear.copy(faculty = if (newVal != "") newVal.toInt() else 0))
    }
    fun onFourthYearApplicantsChange(newVal: String) {
        _uiState.value = _uiState.value.copy(fourthYear = _uiState.value.fourthYear.copy(applicants = if (newVal != "") newVal.toInt() else 0))
    }
    fun onFourthYearAdmittedChange(newVal: String) {
        _uiState.value = _uiState.value.copy(fourthYear = _uiState.value.fourthYear.copy(admitted = if (newVal != "") newVal.toInt() else 0))
    }
    fun onFourthYearGraduatesChange(newVal: String) {
        _uiState.value = _uiState.value.copy(fourthYear = _uiState.value.fourthYear.copy(graduates = if (newVal != "") newVal.toInt() else 0))
    }

    fun onFifthYearStudentsChange(newVal: String) {
        _uiState.value = _uiState.value.copy(fifthYear = _uiState.value.fifthYear.copy(students = if (newVal != "") newVal.toInt() else 0))
    }
    fun onFifthYearFacultyChange(newVal: String) {
        _uiState.value = _uiState.value.copy(fifthYear = _uiState.value.fifthYear.copy(faculty = if (newVal != "") newVal.toInt() else 0))
    }
    fun onFifthYearApplicantsChange(newVal: String) {
        _uiState.value = _uiState.value.copy(fifthYear = _uiState.value.fifthYear.copy(applicants = if (newVal != "") newVal.toInt() else 0))
    }
    fun onFifthYearAdmittedChange(newVal: String) {
        _uiState.value = _uiState.value.copy(fifthYear = _uiState.value.fifthYear.copy(admitted = if (newVal != "") newVal.toInt() else 0))
    }
    fun onFifthYearGraduatesChange(newVal: String) {
        _uiState.value = _uiState.value.copy(fifthYear = _uiState.value.fifthYear.copy(graduates = if (newVal != "") newVal.toInt() else 0))
    }

    fun addYear(year: SchoolAnalyticsYears) {
        when (year) {
            SchoolAnalyticsYears.FIRST -> {
                _uiState.value = _uiState.value.copy(firstYear = _uiState.value.firstYear.copy(
                    studentByYear = _uiState.value.firstYear.studentByYear.plus(StudentByYear("", 0))
                ))
            }
            SchoolAnalyticsYears.SECOND -> {
                _uiState.value = _uiState.value.copy(secondYear = _uiState.value.secondYear.copy(
                    studentByYear = _uiState.value.secondYear.studentByYear.plus(StudentByYear("", 0))
                ))
            }
            SchoolAnalyticsYears.THIRD -> {
                _uiState.value = _uiState.value.copy(thirdYear = _uiState.value.thirdYear.copy(
                    studentByYear = _uiState.value.thirdYear.studentByYear.plus(StudentByYear("", 0))
                ))
            }
            SchoolAnalyticsYears.FOURTH -> {
                _uiState.value = _uiState.value.copy(fourthYear = _uiState.value.fourthYear.copy(
                    studentByYear = _uiState.value.fourthYear.studentByYear.plus(StudentByYear("", 0))
                ))
            }
            SchoolAnalyticsYears.FIFTH -> {
                _uiState.value = _uiState.value.copy(fifthYear = _uiState.value.fifthYear.copy(
                    studentByYear = _uiState.value.fifthYear.studentByYear.plus(StudentByYear("", 0))
                ))
            }
            else -> {}
        }
    }

    fun removeYear(year: SchoolAnalyticsYears, index: Int) {
        when (year) {
            SchoolAnalyticsYears.FIRST -> {
                _uiState.value = _uiState.value.copy(firstYear = _uiState.value.firstYear.copy(
                    studentByYear = _uiState.value.firstYear.studentByYear.filterIndexed { i, _ -> i != index }
                ))
            }

            SchoolAnalyticsYears.SECOND -> {
                _uiState.value = _uiState.value.copy(secondYear = _uiState.value.secondYear.copy(
                    studentByYear = _uiState.value.secondYear.studentByYear.filterIndexed { i, _ -> i != index }
                ))
            }

            SchoolAnalyticsYears.THIRD -> {
                _uiState.value = _uiState.value.copy(thirdYear = _uiState.value.thirdYear.copy(
                    studentByYear = _uiState.value.thirdYear.studentByYear.filterIndexed { i, _ -> i != index }
                ))
            }

            SchoolAnalyticsYears.FOURTH -> {
                _uiState.value = _uiState.value.copy(fourthYear = _uiState.value.fourthYear.copy(
                    studentByYear = _uiState.value.fourthYear.studentByYear.filterIndexed { i, _ -> i != index }
                ))
            }

            SchoolAnalyticsYears.FIFTH -> {
                _uiState.value = _uiState.value.copy(fifthYear = _uiState.value.fifthYear.copy(
                    studentByYear = _uiState.value.fifthYear.studentByYear.filterIndexed { i, _ -> i != index }
                ))
            }
            else -> {}
        }
    }

    fun updateNumberStudentByYear(year: SchoolAnalyticsYears, index: Int, newVal: String) {
        when (year) {
            SchoolAnalyticsYears.FIRST -> {
                _uiState.value = _uiState.value.copy(firstYear = _uiState.value.firstYear.copy(
                    studentByYear = _uiState.value.firstYear.studentByYear.mapIndexed { i, studentByYear ->
                        if (index == i) studentByYear.copy(students = newVal.toIntOrNull() ?: 0)
                        else studentByYear
                    }
                ))
            }
            SchoolAnalyticsYears.SECOND -> {
                _uiState.value = _uiState.value.copy(secondYear = _uiState.value.secondYear.copy(
                    studentByYear = _uiState.value.secondYear.studentByYear.mapIndexed { i, studentByYear ->
                        if (index == i) studentByYear.copy(students = newVal.toIntOrNull() ?: 0)
                        else studentByYear
                    }
                ))
            }
            SchoolAnalyticsYears.THIRD -> {
                _uiState.value = _uiState.value.copy(thirdYear = _uiState.value.thirdYear.copy(
                    studentByYear = _uiState.value.thirdYear.studentByYear.mapIndexed { i, studentByYear ->
                        if (index == i) studentByYear.copy(students = newVal.toIntOrNull() ?: 0)
                        else studentByYear
                    }
                ))
            }
            SchoolAnalyticsYears.FOURTH -> {
                _uiState.value = _uiState.value.copy(fourthYear = _uiState.value.fourthYear.copy(
                    studentByYear = _uiState.value.fourthYear.studentByYear.mapIndexed { i, studentByYear ->
                        if (index == i) studentByYear.copy(students = newVal.toIntOrNull() ?: 0)
                        else studentByYear
                    }
                ))
            }
            SchoolAnalyticsYears.FIFTH -> {
                _uiState.value = _uiState.value.copy(fifthYear = _uiState.value.fifthYear.copy(
                    studentByYear = _uiState.value.fifthYear.studentByYear.mapIndexed { i, studentByYear ->
                        if (index == i) studentByYear.copy(students = newVal.toIntOrNull() ?: 0)
                        else studentByYear
                    }
                ))
            }
            else -> {}
        }
    }

    fun updateYearStudentByYear(year: SchoolAnalyticsYears, index: Int, newVal: String) {
        when (year) {
            SchoolAnalyticsYears.FIRST -> {
                _uiState.value = _uiState.value.copy(firstYear = _uiState.value.firstYear.copy(
                    studentByYear = _uiState.value.firstYear.studentByYear.mapIndexed { i, studentByYear ->
                        if (index == i) studentByYear.copy(year = newVal)
                        else studentByYear
                    }
                ))
            }

            SchoolAnalyticsYears.SECOND -> {
                _uiState.value = _uiState.value.copy(secondYear = _uiState.value.secondYear.copy(
                    studentByYear = _uiState.value.secondYear.studentByYear.mapIndexed { i, studentByYear ->
                        if (index == i) studentByYear.copy(year = newVal)
                        else studentByYear
                    }
                ))
            }

            SchoolAnalyticsYears.THIRD -> {
                _uiState.value = _uiState.value.copy(thirdYear = _uiState.value.thirdYear.copy(
                    studentByYear = _uiState.value.thirdYear.studentByYear.mapIndexed { i, studentByYear ->
                        if (index == i) studentByYear.copy(year = newVal)
                        else studentByYear
                    }
                ))
            }

            SchoolAnalyticsYears.FOURTH -> {
                _uiState.value = _uiState.value.copy(fourthYear = _uiState.value.fourthYear.copy(
                    studentByYear = _uiState.value.fourthYear.studentByYear.mapIndexed { i, studentByYear ->
                        if (index == i) studentByYear.copy(year = newVal)
                        else studentByYear
                    }
                ))
            }

            SchoolAnalyticsYears.FIFTH -> {
                _uiState.value = _uiState.value.copy(fifthYear = _uiState.value.fifthYear.copy(
                    studentByYear = _uiState.value.fifthYear.studentByYear.mapIndexed { i, studentByYear ->
                        if (index == i) studentByYear.copy(year = newVal)
                        else studentByYear
                    }
                ))
            }
            else -> {}
        }
    }

    private fun getAnalytics() {
        viewModelScope.launch {
            val analytics = schoolController.getSchoolAnalytics(school.documentID)
            if (analytics.isNotEmpty()) {
                analytics.forEach {
                    if (it.year == SchoolAnalyticsYears.FIRST) _uiState.value = _uiState.value.copy(firstYear = it)
                    if (it.year == SchoolAnalyticsYears.SECOND) _uiState.value = _uiState.value.copy(secondYear = it)
                    if (it.year == SchoolAnalyticsYears.THIRD) _uiState.value = _uiState.value.copy(thirdYear = it)
                    if (it.year == SchoolAnalyticsYears.FOURTH) _uiState.value = _uiState.value.copy(fourthYear = it)
                    if (it.year == SchoolAnalyticsYears.FIFTH) _uiState.value = _uiState.value.copy(fifthYear = it)
                }
            }
        }
    }

    fun save() {
        _uiState.value = _uiState.value.copy(
            saveLoading = true,
            firstYear = _uiState.value.firstYear.copy(
                documentID = (school.documentID + "FIRST").md5(),
                admissionRate = if (_uiState.value.firstYear.applicants != 0) ((_uiState.value.firstYear.admitted / _uiState.value.firstYear.applicants) * 100).toFloat() else 0.0f,
                graduationRate = if (_uiState.value.firstYear.students != 0) ((_uiState.value.firstYear.graduates / _uiState.value.firstYear.students) * 100).toFloat() else 0.0f,
            ),
            secondYear = _uiState.value.secondYear.copy(
                documentID = (school.documentID + "SECOND").md5(),
                admissionRate = if (_uiState.value.secondYear.applicants != 0) ((_uiState.value.secondYear.admitted / _uiState.value.secondYear.applicants) * 100).toFloat() else 0.0f,
                graduationRate = if (_uiState.value.secondYear.students != 0) ((_uiState.value.secondYear.graduates / _uiState.value.secondYear.students) * 100).toFloat() else 0.0f,
            ),
            thirdYear = _uiState.value.thirdYear.copy(
                documentID = (school.documentID + "THIRD").md5(),
                admissionRate = if (_uiState.value.thirdYear.applicants != 0) ((_uiState.value.thirdYear.admitted / _uiState.value.thirdYear.applicants) * 100).toFloat() else 0.0f,
                graduationRate = if (_uiState.value.thirdYear.students != 0) ((_uiState.value.thirdYear.graduates / _uiState.value.thirdYear.students) * 100).toFloat() else 0.0f,
            ),
            fourthYear = _uiState.value.fourthYear.copy(
                documentID = (school.documentID + "FOURTH").md5(),
                admissionRate = if (_uiState.value.fourthYear.applicants != 0) ((_uiState.value.fourthYear.admitted / _uiState.value.fourthYear.applicants) * 100).toFloat() else 0.0f,
                graduationRate = if (_uiState.value.fourthYear.students != 0) ((_uiState.value.fourthYear.graduates / _uiState.value.fourthYear.students) * 100).toFloat() else 0.0f,
            ),
            fifthYear = _uiState.value.fifthYear.copy(
                documentID = (school.documentID + "FIFTH").md5(),
                admissionRate = if (_uiState.value.fifthYear.applicants != 0) ((_uiState.value.fifthYear.admitted / _uiState.value.fifthYear.applicants) * 100).toFloat() else 0.0f,
                graduationRate = if (_uiState.value.fifthYear.students != 0) ((_uiState.value.fifthYear.graduates / _uiState.value.fifthYear.students) * 100).toFloat() else 0.0f,
            ),
        )
        viewModelScope.launch {
            schoolController.setSchoolAnalytics(documentID = school.documentID, listOf(
                _uiState.value.firstYear,
                _uiState.value.secondYear,
                _uiState.value.thirdYear,
                _uiState.value.fourthYear,
                _uiState.value.fifthYear
            )
            )
            withContext(Dispatchers.Main) {
                _uiState.value = _uiState.value.copy(saveLoading = false)
            }
        }
    }
}
