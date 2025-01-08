package com.universitinder.app.registration

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.lifecycle.viewmodel.compose.viewModel
import com.universitinder.app.login.LoginActivity

class OtpActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val email = intent.getStringExtra("email") ?: ""
        val password = intent.getStringExtra("password") ?: ""

        setContent {
            MaterialTheme {
                Surface {
                    val viewModel: OtpViewModel = viewModel(
                        factory = OtpViewModelFactory(this)
                    )

                    // Define onSuccess lambda
                    val onSuccess: () -> Unit = {
                        // Close the current activity after success
                        setResult(RESULT_OK)
                        val intent = Intent(this@OtpActivity, LoginActivity::class.java)
                        startActivity(intent)
                        finish()
                    }

                    OtpScreen(
                        email = email,
                        password = password,
                        onSuccess = onSuccess,  // Pass only onSuccess
                        onBack = {
                            setResult(RESULT_CANCELED)
                            finish()
                        },
                        viewModel = viewModel
                    )

                    Handler(Looper.getMainLooper()).postDelayed({
                        viewModel.updateLoadingState(false)

                        val intent = Intent(this@OtpActivity, LoginActivity::class.java)
                        startActivity(intent)
                        finish()
                    }, 5000)
                }
            }
        }
    }
}
