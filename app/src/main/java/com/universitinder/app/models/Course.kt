package com.universitinder.app.models

enum class EducationLevel {
    VOCATIONAL,
    BACHELORS,
    MASTERS,
    DOCTORATE
}

class Degree (
    val name : String,
    val duration : Int,
    val level : EducationLevel
)