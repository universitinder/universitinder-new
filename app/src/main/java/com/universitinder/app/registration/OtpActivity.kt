package com.universitinder.app.registration

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.lifecycle.viewmodel.compose.viewModel

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

                    OtpScreen(
                        email = email,
                        password = password,
                        onSuccess = {
                            setResult(RESULT_OK)
                            finish()
                        },
                        onBack = {
                            setResult(RESULT_CANCELED)
                            finish()
                        },
                        viewModel = viewModel
                    )
                }
            }
        }
    }

}
