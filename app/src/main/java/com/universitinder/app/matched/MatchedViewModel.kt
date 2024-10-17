package com.universitinder.app.matched

import android.content.Intent
import androidx.lifecycle.ViewModel
import com.universitinder.app.faq.FAQActivity
import com.universitinder.app.helpers.ActivityStarterHelper
import com.universitinder.app.models.SchoolPlusImages
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class MatchedViewModel(
    val school: SchoolPlusImages,
    private val activityStarterHelper: ActivityStarterHelper,
    val popActivity: () -> Unit
): ViewModel(){
    private val _uiState = MutableStateFlow(MatchedUiState())
    val uiState : StateFlow<MatchedUiState> = _uiState.asStateFlow()

    init {
        if (school.images.isNotEmpty()) {
            val logo = school.images.first { it.lastPathSegment!!.split("/")[2].contains("logo") }
            _uiState.value = _uiState.value.copy(logo = logo)
        }
        _uiState.value = _uiState.value.copy(schoolPlusImages = school)
    }

    fun onTabChange(newVal: Int) { _uiState.value = _uiState.value.copy(activeTab = newVal) }

    fun startFAQActivity() {
        val intent = Intent(activityStarterHelper.getContext(), FAQActivity::class.java)
        intent.putExtra("schoolID", school.id)
        activityStarterHelper.startActivity(intent)
    }
}