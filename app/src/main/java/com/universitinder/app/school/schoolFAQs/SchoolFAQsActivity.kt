package com.universitinder.app.school.schoolFAQs

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import com.universitinder.app.controllers.FaqController
import com.universitinder.app.helpers.ActivityStarterHelper
import com.universitinder.app.ui.theme.UniversitinderTheme

class SchoolFAQsActivity : AppCompatActivity() {
    private lateinit var schoolFAQsViewModel: SchoolFAQsViewModel

    override fun onRestart() {
        super.onRestart()
        schoolFAQsViewModel.refresh()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val faqController = FaqController()
        val activityStarterHelper = ActivityStarterHelper(this)
        schoolFAQsViewModel = SchoolFAQsViewModel(faqController = faqController, activityStarterHelper = activityStarterHelper, popActivity = this::finish)

        setContent {
            UniversitinderTheme {
                SchoolFAQsScreen(schoolFAQsViewModel = schoolFAQsViewModel)
            }
        }
    }
}