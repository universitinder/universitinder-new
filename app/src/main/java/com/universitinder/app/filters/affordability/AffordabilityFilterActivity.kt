package com.universitinder.app.filters.affordability

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import com.universitinder.app.controllers.FilterController
import com.universitinder.app.ui.theme.UniversitinderTheme

class AffordabilityFilterActivity: AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val filterController = FilterController()
        val affordabilityFilterViewModel = AffordabilityFilterViewModel(filterController = filterController, popActivity = this::finish)

        setContent {
            UniversitinderTheme {
                AffordabilityFilterScreen(affordabilityFilterViewModel = affordabilityFilterViewModel)
            }
        }
    }
}