package com.universitinder.app.profile

import android.content.Context
import android.content.Intent
import androidx.datastore.preferences.core.Preferences
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import com.universitinder.app.MainActivity
import com.universitinder.app.changePassword.ChangePasswordActivity
import com.universitinder.app.controllers.UserController
import com.universitinder.app.login.LoginActivity
import com.universitinder.app.models.User
import com.universitinder.app.models.UserState
import com.universitinder.app.models.UserType
import com.universitinder.app.preferences.PreferencesKey
import com.universitinder.app.profile.editAccount.EditAccountActivity
import com.universitinder.app.userDataStore
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import com.universitinder.app.helpers.ActivityStarterHelper

class ProfileViewModel(
    private val auth: FirebaseAuth,
    private val userController: UserController,
    private val activityStarterHelper: ActivityStarterHelper,
    private val clearUser: suspend () -> Unit
) : ViewModel() {
    private val _uiState = MutableStateFlow(ProfileUiState())
    val uiState : StateFlow<ProfileUiState> = _uiState.asStateFlow()

    init {
        val currentUser = UserState.currentUser
        if (currentUser != null) { _uiState.value = _uiState.value.copy(user = currentUser) }
    }

    fun onDismissDeleteDialog() { _uiState.value = _uiState.value.copy(showDeleteDialog = false, deleteDialogLoading = false) }
    fun showDeleteDialog() { _uiState.value = _uiState.value.copy(showDeleteDialog = true) }

    suspend fun refreshUser(context: Context) {
        viewModelScope.launch(Dispatchers.IO) {
            withContext(Dispatchers.Main) { _uiState.value = _uiState.value.copy(loading = true) }
            async {
                val map : Preferences? = context.userDataStore.data.firstOrNull()
                if (map != null) {
                    val user = User(
                        email = map[PreferencesKey.USER_EMAIL] ?: "",
                        name = map[PreferencesKey.USER_NAME] ?: "",
                        contactNumber = map[PreferencesKey.USER_CONTACT_NUMBER] ?: "",
                        address = map[PreferencesKey.USER_ADDRESS] ?: "",
                        type = if (map[PreferencesKey.USER_TYPE] != null) UserType.valueOf(map[PreferencesKey.USER_TYPE].toString()) else UserType.UNKNOWN,
                    )
                    UserState.setUser(user)
                    setUser(user)
                }
            }.await()
            withContext(Dispatchers.Main) { _uiState.value = _uiState.value.copy(loading = false) }
        }
    }

    private fun setUser(newUser: User) { _uiState.value = _uiState.value.copy(user = newUser) }

    fun logout() {
        auth.signOut()
        viewModelScope.launch {
            clearUser()
        }
        val intent = Intent(activityStarterHelper.getContext(), MainActivity::class.java)
        activityStarterHelper.startActivity(intent)
    }

    fun startEditAccountActivity() {
        val intent = Intent(activityStarterHelper.getContext(), EditAccountActivity::class.java)
        activityStarterHelper.startActivity(intent)
    }

    fun startChangePasswordActivity() {
        val intent = Intent(activityStarterHelper.getContext(), ChangePasswordActivity::class.java)
        activityStarterHelper.startActivity(intent)
    }

    fun deleteAccount() {
        viewModelScope.launch(Dispatchers.IO) {
            withContext(Dispatchers.Main) { _uiState.value = _uiState.value.copy(deleteDialogLoading = true) }
            userController.deleteUser()
            auth.signOut()
            val intent = Intent(activityStarterHelper.getContext(), LoginActivity::class.java)
            activityStarterHelper.startActivity(intent)
            withContext(Dispatchers.Main) { _uiState.value = _uiState.value.copy(deleteDialogLoading = false) }
        }
    }
}