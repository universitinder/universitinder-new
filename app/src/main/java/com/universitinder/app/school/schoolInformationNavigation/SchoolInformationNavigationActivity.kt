package com.universitinder.app.school.schoolInformationNavigation

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import com.universitinder.app.helpers.ActivityStarterHelper
import com.universitinder.app.ui.theme.UniversitinderTheme

class SchoolInformationNavigationActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val viewModel = SchoolInformationNavigationViewModel(activityStarterHelper = ActivityStarterHelper(this), popActivity = this::finish)

        setContent {
            UniversitinderTheme {
                SchoolInformationNavigationScreen(viewModel = viewModel)
            }
        }
    }
}