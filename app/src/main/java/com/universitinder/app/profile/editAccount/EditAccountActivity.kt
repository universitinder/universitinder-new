package com.universitinder.app.profile.editAccount

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import com.universitinder.app.ui.theme.UniversitinderTheme
import com.universitinder.app.helpers.ActivityStarterHelper

class EditAccountActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val activityStarterHelper = ActivityStarterHelper(this)
        val editAccountViewModel = EditAccountViewModel(activityStarterHelper = activityStarterHelper, popActivity = this::popActivity)

        setContent {
            UniversitinderTheme {
                EditAccountScreen(editAccountViewModel = editAccountViewModel)
            }
        }
    }

    private fun popActivity() {
        finish()
    }
}