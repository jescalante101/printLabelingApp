package com.example.fibra_labeling.ui.screen.inventory.component

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable

@Composable
fun ConfirmSyncDialog(
    isVisible: Boolean,
    onConfirm: () -> Unit,
    onDismiss: () -> Unit
) {
    if (isVisible) {
        AlertDialog(
            onDismissRequest = onDismiss,
            title = {
                Text(text = "Confirmación de Sincronización")
            },
            text = {
                Text("¿Está seguro de enviar estos datos a SAP?\n El documento cambiará de estado a cerrado y no podrá ser modificado.")
            },
            confirmButton = {
                Button (
                    onClick = {
                        onConfirm() // Realiza la sincronización
                        onDismiss() // Cierra el diálogo
                    }
                ) {
                    Text("Sí")
                }
            },
            dismissButton = {
                Button(
                    onClick = onDismiss
                ) {
                    Text("Cancelar")
                }
            }
        )
    }
}