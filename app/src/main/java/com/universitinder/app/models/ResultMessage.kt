package com.universitinder.app.models

enum class ResultMessageType {
    SUCCESS,
    FAILED
}

data class ResultMessage (
    val show: Boolean = false,
    val type: ResultMessageType = ResultMessageType.SUCCESS,
    val message: String = ""
)
