package com.universitinder.app.faq

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import com.universitinder.app.ui.theme.UniversitinderTheme

class FAQActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val faqViewModel = FAQViewModel(popActivity = this::finish)

        setContent {
            UniversitinderTheme {
                FAQScreen(faqViewModel = faqViewModel)
            }
        }
    }
}