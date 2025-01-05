package com.universitinder.app.school.schoolFAQs

import android.content.Intent
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.firestore.DocumentSnapshot
import com.universitinder.app.controllers.FaqController
import com.universitinder.app.models.School
import com.universitinder.app.models.UserState
import com.universitinder.app.school.schoolFAQs.createFAQ.CreateFAQActivity
import com.universitinder.app.school.schoolFAQs.editFAQ.EditFAQActivity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import com.universitinder.app.helpers.ActivityStarterHelper

class SchoolFAQsViewModel(
    private val school: School,
    private val faqController: FaqController,
    private val activityStarterHelper: ActivityStarterHelper,
    val popActivity: () -> Unit
): ViewModel() {
    private val _uiState = MutableStateFlow(SchoolFAQsUiState())
    private val currentUser = UserState.currentUser
    val uiState : StateFlow<SchoolFAQsUiState> = _uiState.asStateFlow()

    init {
        refresh()
    }

    fun refresh() {
        if (currentUser != null) {
            viewModelScope.launch(Dispatchers.IO) {
                withContext(Dispatchers.Main) { _uiState.value = _uiState.value.copy(fetchingLoading = true) }
                val faqs = faqController.getFAQs(school.documentID)
                onFaqsChange(faqs)
                withContext(Dispatchers.Main) { _uiState.value = _uiState.value.copy(fetchingLoading = false) }
            }
        }
    }

    private fun onFaqsChange(faqs: List<DocumentSnapshot>) { _uiState.value = _uiState.value.copy(faqs = faqs) }

    fun startAddFaqActivity() {
        val intent = Intent(activityStarterHelper.getContext(), CreateFAQActivity::class.java)
        intent.putExtra("SCHOOL", school)
        activityStarterHelper.startActivity(intent)
    }

    fun startEditFaqActivity(documentID: String) {
        val intent = Intent(activityStarterHelper.getContext(), EditFAQActivity::class.java)
        intent.putExtra("documentID", documentID)
        intent.putExtra("SCHOOL", school)
        activityStarterHelper.startActivity(intent)
    }
}