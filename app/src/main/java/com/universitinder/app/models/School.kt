package com.universitinder.app.models

import android.net.Uri
import android.os.Parcelable
import kotlinx.parcelize.Parcelize

//enum class PrivatePublic {
//    PRIVATE,
//    PUBLIC
//}

@Parcelize
data class School (
    val documentID: String = "",
    val name: String = "",
    val email: String = "",
    val contactNumber: String = "",
    val link: String = "",
    val province: String = "",
    val municipalityOrCity: String = "",
    val barangay: String = "",
    val street: String = "",
//    val isPrivate: Boolean = false,
    val private: Boolean = false,
//    val isPublic: Boolean = false,
    val public: Boolean = false,
    val has2YearCourse: Boolean = false,
    val has3YearCourse: Boolean = false,
    val has4YearCourse: Boolean = false,
    val has5YearCourse: Boolean = false,
    val minimum: Int = 0,
    val maximum: Int = 0,
    val affordability: Int = 0,
    val mission: String = "",
    val vision: String = "",
    val coreValues: String = "",
    val courses: List<String> = emptyList(),
    val coursesDuration: List<String> = emptyList(),
    val swipeRight: Int = 0,
    val swipeLeft: Int = 0,
    val coordinates: LocationPoint = LocationPoint(0.0, 0.0),
) : Parcelable

@Parcelize
data class SchoolPlusImages(
    val id: String = "",
    val school: School? = null,
    val images: List<Uri> = emptyList()
) : Parcelable

data class CourseDurations(
    val has2YearCourse: Boolean,
    val has3YearCourse: Boolean,
    val has4YearCourse: Boolean,
    val has5YearCourse: Boolean,
)

val COURSE_DURATION = listOf(
    "2 YEARS",
    "3 YEARS",
    "4 YEARS",
    "5 YEARS",
)

val COURSE_DURATION_STRING_TO_INT_MAP = hashMapOf(
    "2 YEARS" to 2,
    "3 YEARS" to 3,
    "4 YEARS" to 4,
    "5 YEARS" to 5
)

val COURSE_DURATION_INT_TO_STRING_MAP = hashMapOf(
    2 to "2 YEARS",
    3 to "3 YEARS",
    4 to "4 YEARS",
    5 to "5 YEARS"
)