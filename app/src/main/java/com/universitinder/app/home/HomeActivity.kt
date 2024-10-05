package com.universitinder.app.home

import android.content.Intent
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.datastore.preferences.core.edit
import androidx.lifecycle.lifecycleScope
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth
import com.universitinder.app.accountSetup.AccountSetupActivity
import com.universitinder.app.controllers.CourseController
import com.universitinder.app.controllers.FilterController
import com.universitinder.app.controllers.SchoolController
import com.universitinder.app.controllers.UserController
import com.universitinder.app.filters.FiltersViewModel
import com.universitinder.app.helpers.ActivityStarterHelper
import com.universitinder.app.login.LoginActivity
import com.universitinder.app.matches.MatchesViewModel
import com.universitinder.app.models.UserState
import com.universitinder.app.navigation.NavigationScreen
import com.universitinder.app.navigation.NavigationViewModel
import com.universitinder.app.profile.ProfileViewModel
//import com.universitinder.app.school.SchoolViewModel
import com.universitinder.app.school.list.SchoolListViewModel
//import com.universitinder.app.school.schoolInformationNavigation.SchoolInformationNavigationViewModel
import com.universitinder.app.ui.theme.UniversitinderTheme
import com.universitinder.app.userDataStore
import kotlinx.coroutines.launch

class HomeActivity : AppCompatActivity() {
    private lateinit var auth: FirebaseAuth
    private lateinit var profileViewModel : ProfileViewModel
    private lateinit var navigationViewModel : NavigationViewModel
    private lateinit var homeViewModel: HomeViewModel

    override fun onRestart() {
        super.onRestart()
        lifecycleScope.launch{
            profileViewModel.refreshUser(this@HomeActivity)
            navigationViewModel.refreshUser()
        }
    }

    override fun onStart() {
        super.onStart()

        if (auth.currentUser != null){
            val currentUser = UserState.currentUser
            if (currentUser == null) {
                val intent = Intent(this@HomeActivity, AccountSetupActivity::class.java)
                startActivity(intent)
            }
        } else {
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        auth = Firebase.auth

        val schoolController = SchoolController()
        val filterController = FilterController()
        val courseController = CourseController()
        val userController = UserController()
        val activityStarterHelper = ActivityStarterHelper(this)
        homeViewModel = HomeViewModel(
            application = application,
            schoolController = schoolController,
            filterController = filterController,
            activityStarterHelper = activityStarterHelper,
            userController = userController
        )
//        val schoolInformationNavigationViewModel = SchoolInformationNavigationViewModel(activityStarterHelper = activityStarterHelper, popActivity = this::finish)
//        val schoolViewModel = SchoolViewModel(documentId = "", schoolController = schoolController, activityStarterHelper = activityStarterHelper, popActivity = this::finish)
        val filtersViewModel = FiltersViewModel(filterController = filterController, courseController = courseController, popActivity = this::finish)
        val matchesViewModel = MatchesViewModel(userController = userController, schoolController = schoolController, activityStarterHelper = activityStarterHelper)
        val schoolListViewModel = SchoolListViewModel(schoolController = schoolController, activityStarterHelper = activityStarterHelper)
        profileViewModel = ProfileViewModel(auth = auth, userController = userController, activityStarterHelper = activityStarterHelper, clearUser = this::clearUser)
        navigationViewModel = NavigationViewModel(
//            schoolInformationNavigationViewModel = schoolInformationNavigationViewModel,
//            schoolViewModel = schoolViewModel,
            homeViewModel = homeViewModel,
            profileViewModel = profileViewModel,
//            filtersViewModel = filtersViewModel,
            matchesViewModel = matchesViewModel,
            schoolListViewModel = schoolListViewModel
        )

        setContent {
            UniversitinderTheme {
                NavigationScreen(navigationViewModel = navigationViewModel)
            }
        }
    }

    private suspend fun clearUser() {
        this.userDataStore.edit { preferences ->
            preferences.clear()
        }
    }
}