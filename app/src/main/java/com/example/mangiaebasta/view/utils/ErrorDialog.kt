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
            onDismiss() // Chiamato quando l'utente chiude il dialogo
        },
        confirmButton = {
            Button(onClick = onDismiss) { // Bottone "Chiudi"
                Text("Chiudi")
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
