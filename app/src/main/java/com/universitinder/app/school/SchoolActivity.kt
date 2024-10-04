package com.universitinder.app.school

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import com.universitinder.app.controllers.SchoolController
import com.universitinder.app.helpers.ActivityStarterHelper
import com.universitinder.app.ui.theme.UniversitinderTheme

class SchoolActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val documentId = intent.getStringExtra("DOCUMENT_ID") ?: ""

        if (documentId == "") finish()

        val schoolController = SchoolController()
        val activityStarterHelper = ActivityStarterHelper(this)
        val schoolViewModel = SchoolViewModel(
            documentId = documentId,
            schoolController = schoolController,
            activityStarterHelper = activityStarterHelper,
            popActivity = this::finish
        )

        setContent {
            UniversitinderTheme {
                SchoolScreen(schoolViewModel = schoolViewModel)
            }
        }
    }
}