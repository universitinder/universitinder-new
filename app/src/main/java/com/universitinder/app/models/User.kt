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
) {
    fun asMap() : Map<String, Any> {
        return hashMapOf(
            "email" to email,
            "name" to name,
            "type" to type.toString(),
            "address" to address,
            "contactNumber" to contactNumber
        )
    }
}


object UserState {
    var currentUser : User? = null

    fun setUser(user: User?) {
        currentUser = user
    }
}