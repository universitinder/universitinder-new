package com.universitinder.app.school.schoolFAQs.createFAQ

import android.os.Build
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import com.universitinder.app.controllers.FaqController
import com.universitinder.app.models.School
import com.universitinder.app.ui.theme.UniversitinderTheme

class CreateFAQActivity : AppCompatActivity() {
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
        val createFAQViewModel = CreateFAQViewModel(school = school!!, faqController = faqController, popActivity = this::finish)

        setContent {
            UniversitinderTheme {
                CreateFAQScreen(createFAQViewModel = createFAQViewModel)
            }
        }
    }
}