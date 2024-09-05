package com.universitinder.app.models

class School (
    val name: String,
    val email: String,
    val contactNumber: String,
    val address: String,
    val minimum: Int,
    val maximum: Int,
    val affordability: Int,
    val mission: String,
    val vision: String,
    val coreValues: String,
    val courses: String,
) {
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
