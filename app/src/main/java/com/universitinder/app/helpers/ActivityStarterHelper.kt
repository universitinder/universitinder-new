package com.universitinder.app.helpers

import android.content.Context
import android.content.Intent
import androidx.activity.ComponentActivity
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts

class ActivityStarterHelper(private val activity: ComponentActivity) {
    private var resultCallback: ((Int) -> Unit)? = null
    
    private val activityResultLauncher: ActivityResultLauncher<Intent> = activity.registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        resultCallback?.invoke(result.resultCode)
    }

    fun getContext(): Context = activity

    fun startActivity(intent: Intent) {
        activity.startActivity(intent)
    }

    fun launchActivityForResult(intent: Intent, callback: (Int) -> Unit) {
        resultCallback = callback
        activityResultLauncher.launch(intent)
    }
}