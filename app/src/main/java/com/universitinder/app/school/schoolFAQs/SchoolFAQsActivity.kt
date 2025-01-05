package com.universitinder.app.school.schoolFAQs

import android.os.Build
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import com.universitinder.app.controllers.FaqController
import com.universitinder.app.models.School
import com.universitinder.app.ui.theme.UniversitinderTheme
import com.universitinder.app.helpers.ActivityStarterHelper

class SchoolFAQsActivity : AppCompatActivity() {
    private lateinit var schoolFAQsViewModel: SchoolFAQsViewModel

    override fun onRestart() {
        super.onRestart()
        schoolFAQsViewModel.refresh()
    }

    @Suppress("DEPRECATION")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val school = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            intent.getParcelableExtra("SCHOOL", School::class.java)
        } else {
            intent.getParcelableExtra("SCHOOL")
        }

        if (school == null) finish()

        val faqController = FaqController()
        val activityStarterHelper = ActivityStarterHelper(this)
        schoolFAQsViewModel = SchoolFAQsViewModel(school = school!!, faqController = faqController, activityStarterHelper = activityStarterHelper, popActivity = this::finish)

        setContent {
            UniversitinderTheme {
                SchoolFAQsScreen(schoolFAQsViewModel = schoolFAQsViewModel)
            }
        }
    }
}