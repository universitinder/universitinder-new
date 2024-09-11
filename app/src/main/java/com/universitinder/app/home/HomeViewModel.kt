package com.universitinder.app.home

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.universitinder.app.controllers.FilterController
import com.universitinder.app.controllers.SchoolController
import com.universitinder.app.models.UserState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class HomeViewModel(
    private val schoolController: SchoolController,
    private val filterController: FilterController
): ViewModel() {
    private val currentUser = UserState.currentUser
    private val _uiState = MutableStateFlow(HomeUiState())
    val uiState : StateFlow<HomeUiState> = _uiState.asStateFlow()

    init {
        refresh()
    }

//    private suspend fun getFilters() {
//        if (currentUser != null) {
//            viewModelScope.launch(Dispatchers.IO) {
//                val filter = filterController.getFilter(currentUser.email)
//                Log.w("HOME VIEW MODEL", filter.toString())
//                if (filter != null) _uiState.value = _uiState.value.copy(filter = filter)
//            }
//        }
//    }

    fun refresh() {
        if (currentUser != null) {
            viewModelScope.launch(Dispatchers.IO) {
                withContext(Dispatchers.Main) { _uiState.value = _uiState.value.copy(fetchingLoading = true) }
                val filter = filterController.getFilter(currentUser.email)
                Log.w("HOME VIEW MODEL", filter.toString())
                if (filter != null) {
                    val schools = schoolController.getFilteredSchool(filter)
//                    Log.w("HOME VIEW MODEL", schools.toString())
                    withContext(Dispatchers.Main) {
                        _uiState.value = _uiState.value.copy(
                            fetchingLoading = false,
                            schools = schools
                        )
                    }
                }
            }
        }
    }
}