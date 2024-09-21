package com.universitinder.app.school

import android.net.Uri
import com.universitinder.app.models.SchoolPlusImages

data class SchoolUiState (
    val fetchingLoading : Boolean = false,
    val schoolPlusImages: SchoolPlusImages? = null,
    val logo : Uri? = null,
    val activeTab: Int = 0
)
