package com.universitinder.app.school.location

import com.google.android.gms.maps.model.LatLng
import com.universitinder.app.models.ResultMessage

data class SchoolLocationUiState(
    val location: LatLng? = null,
    val savingLoading: Boolean = false,
    val resultMessage : ResultMessage = ResultMessage()
)
