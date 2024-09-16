package com.universitinder.app.faq

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import com.universitinder.app.controllers.FaqController
import com.universitinder.app.ui.theme.UniversitinderTheme

class FAQActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val schoolId = intent.getStringExtra("schoolID") ?: ""

        if (schoolId == "") finish()

        val faqController = FaqController()
        val faqViewModel = FAQViewModel(schoolId = schoolId, faqController = faqController, popActivity = this::finish)

        setContent {
            UniversitinderTheme {
                FAQScreen(faqViewModel = faqViewModel)
            }
        }
    }
}