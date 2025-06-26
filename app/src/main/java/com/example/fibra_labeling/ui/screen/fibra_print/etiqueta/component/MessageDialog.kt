package com.example.fibra_labeling.ui.screen.fibra_print.etiqueta.component

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable

@Composable
fun MessageDialog(
    show: Boolean,
    message: String,
    onDismiss: () -> Unit
) {
    if (show) {
        AlertDialog(

            onDismissRequest = onDismiss,
            title = { Text("Mensaje") },
            text = { Text(message) },
            confirmButton = {
                Button (
                    onClick = {
                        onDismiss()
                    }
                ) {
                    Text("Cerrar")
                }
            }
        )
    }
}