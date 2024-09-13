package com.universitinder.app.models

import android.net.Uri
import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class School (
    val name: String = "",
    val email: String = "",
    val contactNumber: String = "",
    val address: String = "",
    val link: String = "",
    val province: String = "",
    val municipalityOrCity: String = "",
    val barangay: String = "",
    val street: String = "",
    val minimum: Int = 0,
    val maximum: Int = 0,
    val affordability: Int = 0,
    val mission: String = "",
    val vision: String = "",
    val coreValues: String = "",
    val courses: List<String> = emptyList(),
    val rightSwipe: Int = 0,
    val leftSwipe: Int = 0,
) : Parcelable {
    fun asMap() : Map<String, Any> {
        return hashMapOf(
            "name" to name,
            "address" to address,
            "minimum" to minimum,
            "maximum" to maximum,
            "affordability" to affordability,
            "mission" to mission,
            "vision" to vision,
            "coreValues" to coreValues,
            "courses" to courses
        )
    }
}

@Parcelize
data class SchoolPlusImages(
    val school: School? = null,
    val images: List<Uri> = emptyList()
) : Parcelable