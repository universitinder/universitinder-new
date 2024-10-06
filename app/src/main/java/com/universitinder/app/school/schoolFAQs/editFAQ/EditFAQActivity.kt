package com.universitinder.app.school.schoolFAQs.editFAQ

import android.os.Build
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import com.universitinder.app.controllers.FaqController
import com.universitinder.app.models.School
import com.universitinder.app.ui.theme.UniversitinderTheme

class EditFAQActivity : AppCompatActivity() {
    @Suppress("DEPRECATION")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val documentID = intent.getStringExtra("documentID") ?: ""
        if (documentID == "") finish()

        val school = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            intent.getParcelableExtra("SCHOOL", School::class.java)
        } else {
            intent.getParcelableExtra("SCHOOL")
        }

        if (school == null) finish()

        val faqController = FaqController()
        val editFAQViewModel = EditFAQViewModel(school = school!!, documentID = documentID, faqController = faqController, popActivity = this::finish)

        setContent {
            UniversitinderTheme {
                EditFAQScreen(editFAQViewModel = editFAQViewModel)
            }
        }
    }
}