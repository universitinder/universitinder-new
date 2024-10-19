package com.universitinder.app.components

import android.content.Intent
import android.net.Uri
import android.widget.Toast
import androidx.compose.foundation.text.ClickableText
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.withStyle

@Composable
fun EmailText(email: String) {
    val context = LocalContext.current

    val annotatedText = buildAnnotatedString {
        pushStringAnnotation(tag = "EMAIL", annotation = email)
        withStyle(style = SpanStyle(color = MaterialTheme.colorScheme.primary, textDecoration = TextDecoration.Underline)) {
            append(email)
        }
        pop()
    }

    when (email) {
        "" -> { Text(text = "No Email") }
        else -> {
            ClickableText(
                text = annotatedText,
                onClick = { offset ->
                    annotatedText.getStringAnnotations(tag = "EMAIL", start = offset, end = offset)
                        .firstOrNull()?.let { annotation ->
                            val emailIntent = Intent(Intent.ACTION_SENDTO).apply {
                                data = Uri.parse(annotation.item) // "mailto:" format
                            }

                            // Check if there is an email app available
                            if (emailIntent.resolveActivity(context.packageManager) != null) {
                                context.startActivity(emailIntent)
                            } else {
                                // No email app found, show a fallback message
                                Toast.makeText(context, "No email client installed", Toast.LENGTH_SHORT).show()
                            }
                        }
                }
            )
        }
    }
}