package com.universitinder.app.components.dialogs

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog

@Composable
fun ConfirmDeleteSchoolDialog(
    show: Boolean,
    onDismissRequest: () -> Unit,
    deleteSchool: () -> Unit,
    loading: Boolean
) {
    if (show) {
        Dialog(onDismissRequest = onDismissRequest) {
            Card{
                Text(text = "Delete Confirmation", fontSize = 24.sp, modifier = Modifier.padding(20.dp))
                when(loading) {
                    true -> {
                        Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
                            CircularProgressIndicator()
                        }
                    }
                    false -> {
                        Text(text = "Are you sure you want to delete this school? Deleting this school is irreversible", modifier = Modifier.padding(vertical = 10.dp, horizontal = 20.dp))
                    }
                }
                Row(
                    modifier = Modifier
                        .padding(20.dp)
                        .fillMaxWidth()
                ) {
                    TextButton(
                        onClick = onDismissRequest,
                        modifier = Modifier.fillMaxWidth(0.5f)
                    ) {
                        Text(text = "Cancel")
                    }
                    Button(
                        onClick = deleteSchool,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(text = "Delete")
                    }
                }
            }
        }
    }
}
