package com.universitinder.app.matched

import android.net.Uri
import com.universitinder.app.models.SchoolPlusImages

data class MatchedUiState (
    val schoolPlusImages: SchoolPlusImages = SchoolPlusImages(),
    val logo : Uri? = null,
    val activeTab: Int = 0,
)