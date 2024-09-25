package com.universitinder.app.models

data class Filter(
    val provinces: String = "",
    val cities: String = "",
    val minimum: Int = 0,
    val maximum: Int = 0,
    val affordability: Int = 0,
    val courses: String = "",
    val privatePublic: String = "",
    val courseDuration: String = ""
)
