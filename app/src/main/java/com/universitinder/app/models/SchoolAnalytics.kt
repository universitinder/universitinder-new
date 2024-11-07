package com.universitinder.app.models

enum class SchoolAnalyticsYears {
    FIRST,
    SECOND,
    THIRD,
    FOURTH,
    FIFTH,
    ALL
}

/*
* •Year: All
•Admission Rate
•Graduation Rate
•Student enrollment by year
* */

data class StudentByYear(
    val year: String = "",
    val students: Int = 0,
)

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
