package com.example.fibra_labeling.fibrafil.ui.screen.etiqueta.component

import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.text.input.KeyboardType

@Composable
fun CopiesInputDialog(
    show: Boolean,
    onDismiss: () -> Unit,
    onConfirm: (Int) -> Unit
) {
    if (show) {
        var copiesText by remember { mutableStateOf("1") }
        val isValid = copiesText.toIntOrNull()?.let { it > 0 } == true

        AlertDialog(
            onDismissRequest = onDismiss,
            title = { Text("Ingresar número de copias") },
            text = {
                OutlinedTextField(
                    value = copiesText,
                    onValueChange = { value ->
                        // Solo acepta números positivos
                        if (value.all { it.isDigit() } && value.length <= 3) {
                            copiesText = value
                        } else if (value.isEmpty()) {
                            copiesText = ""
                        }
                    },
                    label = { Text("Nro de copias") },
                    singleLine = true,
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                )
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        val copias = copiesText.toIntOrNull() ?: 1
                        onConfirm(copias)
                    },
                    enabled = isValid
                ) {
                    Text("Aceptar")
                }
            },
            dismissButton = {
                TextButton(onClick = onDismiss) {
                    Text("Cancelar")
                }
            }
        )
    }
}
