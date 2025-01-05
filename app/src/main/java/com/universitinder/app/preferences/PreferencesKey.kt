package com.universitinder.app.preferences

import androidx.datastore.preferences.core.stringPreferencesKey

object PreferencesKey {
    val USER_EMAIL = stringPreferencesKey("user_email")
    val USER_NAME = stringPreferencesKey("user_name")
    val USER_TYPE = stringPreferencesKey("user_type")
    val USER_CONTACT_NUMBER = stringPreferencesKey("user_contact_number")
    val USER_ADDRESS = stringPreferencesKey("user_address")
    val TEMP_PASSWORD = stringPreferencesKey("temp_password")
}