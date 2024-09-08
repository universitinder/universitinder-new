package com.universitinder.app.school.schoolFAQs.createFAQ

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import com.universitinder.app.controllers.FaqController
import com.universitinder.app.ui.theme.UniversitinderTheme

class CreateFAQActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val faqController = FaqController()
        val createFAQViewModel = CreateFAQViewModel(faqController = faqController, popActivity = this::finish)

        setContent {
            UniversitinderTheme {
                CreateFAQScreen(createFAQViewModel = createFAQViewModel)
            }
        }
    }
}