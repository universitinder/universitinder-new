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

    fun userIsEmpty() : Boolean {
        if (currentUser == null) return true
        return currentUser!!.type == UserType.UNKNOWN && (currentUser!!.email.isEmpty() || currentUser!!.email.isBlank()) &&
                (currentUser!!.address.isBlank() || currentUser!!.address.isEmpty()) && (currentUser!!.contactNumber.isEmpty() || currentUser!!.contactNumber.isBlank() &&
                (currentUser!!.name.isEmpty() || currentUser!!.name.isBlank())
            )
    }
}