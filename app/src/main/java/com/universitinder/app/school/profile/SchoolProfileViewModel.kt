package com.universitinder.app.school.profile

import androidx.lifecycle.ViewModel
import com.universitinder.app.models.SchoolPlusImages

class SchoolProfileViewModel (
    val school: SchoolPlusImages,
    val popActivity: () -> Unit,
): ViewModel()