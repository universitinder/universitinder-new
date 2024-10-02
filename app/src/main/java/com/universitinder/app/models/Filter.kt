package com.universitinder.app.models

data class Filter(
    val provinces: String = "",
    val cities: String = "",
    val minimum: Int = 0,
    val maximum: Int = 0,
    val affordability: Int = 0,
    val courses: String = "",
    val private: Boolean = false,
    val public: Boolean = false,
    val has2YearCourse: Boolean = false,
    val has3YearCourse: Boolean = false,
    val has4YearCourse: Boolean = false,
    val has5YearCourse: Boolean = false,
    val courseDuration: String = ""
)
