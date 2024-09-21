package com.universitinder.app.components.dialogs

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.universitinder.app.models.ResultMessage
import com.universitinder.app.models.ResultMessageType

@Composable
fun ConfirmDialog(
    title: String,
    text: String,
    show: Boolean,
    onDismissRequest: () -> Unit,
    resultMessage: ResultMessage,
    onCancel: () -> Unit,
    loading: Boolean,
    actionText: String,
    onConfirm: () -> Unit
) {
    if (show) {
        Dialog(onDismissRequest = onDismissRequest) {
            Surface(
                modifier = Modifier
                    .fillMaxWidth()
                    .wrapContentHeight(),
                shape = MaterialTheme.shapes.medium
            ) {
                Column(
                    modifier = Modifier
                        .padding(16.dp)
                        .fillMaxWidth()
                ) {
                    Text(text = title, style = MaterialTheme.typography.titleLarge)
                    Text(
                        text = text,
                        style = MaterialTheme.typography.bodyMedium,
                        modifier = Modifier.padding(vertical = 24.dp)
                    )
                    if (resultMessage.show)
                        Text(
                            modifier = Modifier.padding(top = 10.dp),
                            text = resultMessage.message,
                            color = if (resultMessage.type == ResultMessageType.FAILED) Color.Red else MaterialTheme.colorScheme.primary,
                            textAlign = TextAlign.Center,
                            fontSize = 12.sp
                        )
                    Row {
                        TextButton(
                            modifier = Modifier
                                .fillMaxWidth(0.5f)
                                .padding(end = 5.dp),
                            onClick = onCancel
                        ) {
                            Text(text = "Cancel")
                        }
                        Button(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(start = 5.dp),
                            onClick = onConfirm
                        ) {
                            if (loading) {
                                CircularProgressIndicator(modifier = Modifier.size(20.dp))
                            } else {
                                Text(text = actionText)
                            }
                        }
                    }
                }
            }
        }
    }
}