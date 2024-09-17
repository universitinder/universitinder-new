package com.universitinder.app.matched

import android.content.Intent
import androidx.lifecycle.ViewModel
import com.universitinder.app.faq.FAQActivity
import com.universitinder.app.helpers.ActivityStarterHelper
import com.universitinder.app.models.SchoolPlusImages

class MatchedViewModel(
    val school: SchoolPlusImages,
    private val activityStarterHelper: ActivityStarterHelper,
    val popActivity: () -> Unit
): ViewModel(){

    fun startFAQActivity() {
        val intent = Intent(activityStarterHelper.getContext(), FAQActivity::class.java)
        intent.putExtra("schoolID", school.id)
        activityStarterHelper.startActivity(intent)
    }
}