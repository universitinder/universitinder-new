package com.universitinder.app.home

//import android.util.Log
import android.content.Intent
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.universitinder.app.controllers.FilterController
import com.universitinder.app.controllers.SchoolController
import com.universitinder.app.controllers.UserController
import com.universitinder.app.filters.FiltersActivity
import com.universitinder.app.helpers.ActivityStarterHelper
import com.universitinder.app.matched.MatchedActivity
import com.universitinder.app.models.SchoolPlusImages
import com.universitinder.app.models.UserState
import com.universitinder.app.models.UserType
import com.universitinder.app.school.profile.SchoolProfileActivity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class HomeViewModel(
    private val schoolController: SchoolController,
    private val userController: UserController,
    private val filterController: FilterController,
    private val activityStarterHelper: ActivityStarterHelper
): ViewModel() {
    private val currentUser = UserState.currentUser
    private val _uiState = MutableStateFlow(HomeUiState())
    val uiState : StateFlow<HomeUiState> = _uiState.asStateFlow()

    init {
        if (currentUser != null && currentUser.type == UserType.STUDENT) {
            refresh()
        }
    }

    fun refresh() {
        if (currentUser != null) {
            viewModelScope.launch(Dispatchers.IO) {
                withContext(Dispatchers.Main) { _uiState.value = _uiState.value.copy(fetchingLoading = true) }
                val filter = filterController.getFilter(currentUser.email)
                if (filter != null) {
                    val schools = schoolController.getFilteredSchoolTwo(filter)
                    withContext(Dispatchers.Main) {
                        _uiState.value = _uiState.value.copy(
                            currentIndex = 0,
                            fetchingLoading = false,
                            schools = schools.shuffled()
                        )
                    }
                }
            }
        }
    }

    fun onSwipeRight(school: SchoolPlusImages) {
        _uiState.value = _uiState.value.copy(currentIndex = _uiState.value.currentIndex+1)
        val intent = Intent(activityStarterHelper.getContext(), MatchedActivity::class.java)
        intent.putExtra("school", school)
        activityStarterHelper.startActivity(intent)
        viewModelScope.launch(Dispatchers.IO) {
            schoolController.addSchoolSwipeRightCount(school.id)
            if (currentUser != null && school.school != null) userController.addMatchedSchool(currentUser, school.school.name)
        }
    }

    fun onSwipeLeft(id: String) {
        _uiState.value = _uiState.value.copy(currentIndex = _uiState.value.currentIndex+1)
        viewModelScope.launch(Dispatchers.IO) {
            schoolController.addSchoolSwipeLeftCount(id)
        }
    }

    fun startSchoolProfileActivity(school: SchoolPlusImages) {
        val intent = Intent(activityStarterHelper.getContext(), SchoolProfileActivity::class.java)
        intent.putExtra("school", school)
        activityStarterHelper.startActivity(intent)
    }

    fun startFilterActivity() {
        val intent = Intent(activityStarterHelper.getContext(), FiltersActivity::class.java)
        activityStarterHelper.startActivity(intent)
    }
}