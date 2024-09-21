package com.universitinder.app.models

enum class UserType {
    INSTITUTION,
    STUDENT,
    UNKNOWN
}

class User (
    val email: String,
    val name: String,
    val type: UserType,
    val address: String,
    val contactNumber: String,
    val matched: List<String> = emptyList(),
)


object UserState {
    var currentUser : User? = null

    fun setUser(user: User?) {
        currentUser = user
    }
}