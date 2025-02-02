package com.example.mangiaebasta.view.utils

import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import com.example.mangiaebasta.model.dataClasses.Error

@Composable
fun ErrorDialog(
    error: Error,
    onDismiss: () -> Unit
) {
    androidx.compose.material3.AlertDialog(
        onDismissRequest = {
            onDismiss()
        },
        confirmButton = {
            Button(onClick = onDismiss) {
                Text("Close")
            }
        },
        title = {
            Text(text = error.title)
        },
        text = {
            Text(text = error.message)
        }
    )
}
