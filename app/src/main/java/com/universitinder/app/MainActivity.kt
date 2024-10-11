package com.universitinder.app

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.lifecycleScope
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth
import com.universitinder.app.home.HomeActivity
import com.universitinder.app.login.LoginActivity
import com.universitinder.app.models.User
import com.universitinder.app.models.UserState
import com.universitinder.app.models.UserType
import com.universitinder.app.preferences.PreferencesKey
import com.universitinder.app.ui.theme.UniversitinderTheme
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.launch
import java.security.MessageDigest

val Context.userDataStore by preferencesDataStore(name = "user_preferences")

@OptIn(ExperimentalStdlibApi::class)
fun String.md5(): String {
    val md = MessageDigest.getInstance("MD5")
    val digest = md.digest(this.toByteArray())
    return digest.toHexString()
}

class MainActivity : AppCompatActivity() {
    private lateinit var auth: FirebaseAuth

    override fun onStart() {
        super.onStart()
        if (auth.currentUser == null) {
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
            return
        }
        val intent = Intent(this, HomeActivity::class.java)
        startActivity(intent)
    }

    private suspend fun getUser() {
        val map : Preferences? = this.userDataStore.data.firstOrNull()
        if (map != null) {
            val user = User(
                email = map[PreferencesKey.USER_EMAIL] ?: "",
                name = map[PreferencesKey.USER_NAME] ?: "",
                contactNumber = map[PreferencesKey.USER_CONTACT_NUMBER] ?: "",
                address = map[PreferencesKey.USER_ADDRESS] ?: "",
                type = if (map[PreferencesKey.USER_TYPE] != null) UserType.valueOf(map[PreferencesKey.USER_TYPE].toString()) else UserType.UNKNOWN,
            )
            UserState.setUser(user)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        auth = Firebase.auth

        lifecycleScope.launch { getUser() }

        setContent {
            UniversitinderTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    Column (
                        modifier = Modifier.fillMaxSize(),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        CircularProgressIndicator()
                    }
                }
            }
        }
    }
}