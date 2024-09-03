package com.universitinder.app.helpers

import android.content.Context
import android.content.Intent

class ActivityStarterHelper(private val context: Context) {
    fun startActivity(intent: Intent) {
        context.startActivity(intent)
    }

    fun getContext() : Context {
        return context
    }
}