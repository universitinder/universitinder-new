import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.google.firebase.auth.FirebaseAuth
import com.universitinder.app.controllers.SchoolController
import com.universitinder.app.controllers.UserController
import com.universitinder.app.helpers.ActivityStarterHelper
import com.universitinder.app.login.LoginViewModel

class LoginViewModelFactory(
    private val auth: FirebaseAuth,
    private val userController: UserController,
    private val schoolController: SchoolController,
    private val activityStarterHelper: ActivityStarterHelper
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(LoginViewModel::class.java)) {
            return LoginViewModel(auth, userController, schoolController, activityStarterHelper) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}