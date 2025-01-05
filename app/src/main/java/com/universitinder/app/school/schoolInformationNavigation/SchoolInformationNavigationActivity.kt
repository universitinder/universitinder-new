package com.universitinder.app.school.schoolInformationNavigation

import android.os.Build
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import com.universitinder.app.models.School
import com.universitinder.app.ui.theme.UniversitinderTheme
import com.universitinder.app.helpers.ActivityStarterHelper

class SchoolInformationNavigationActivity : AppCompatActivity() {
    @Suppress("DEPRECATION")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val school = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            intent.getParcelableExtra("SCHOOL", School::class.java)
        } else {
            intent.getParcelableExtra("SCHOOL")
        }

        if (school == null) finish()

        val viewModel = SchoolInformationNavigationViewModel(
            school = school!!,
            activityStarterHelper = ActivityStarterHelper(this),
            popActivity = this::finish
        )

        setContent {
            UniversitinderTheme {
                SchoolInformationNavigationScreen(viewModel = viewModel)
            }
        }
    }
}