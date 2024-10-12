package com.universitinder.app.models

enum class EducationLevel {
    VOCATIONAL,
    BACHELORS,
    MASTERS,
    DOCTORATE
}

class Course (
    val name : String = "",
    val duration : Int = 0,
    val level : EducationLevel = EducationLevel.BACHELORS
)

class CourseBatchHelper (
    val schoolID: String = "",
    val courses: List<Course> = emptyList(),
    val twoYearCourses: List<Course> = emptyList(),
)