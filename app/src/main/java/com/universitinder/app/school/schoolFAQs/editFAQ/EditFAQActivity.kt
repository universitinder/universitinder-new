package com.universitinder.app.school.schoolFAQs.editFAQ

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import com.universitinder.app.controllers.FaqController
import com.universitinder.app.ui.theme.UniversitinderTheme

class EditFAQActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val documentID = intent.getStringExtra("documentID") ?: ""
        if (documentID == "") finish()

        val faqController = FaqController()
        val editFAQViewModel = EditFAQViewModel(documentID = documentID, faqController = faqController, popActivity = this::finish)

        setContent {
            UniversitinderTheme {
                EditFAQScreen(editFAQViewModel = editFAQViewModel)
            }
        }
    }
}