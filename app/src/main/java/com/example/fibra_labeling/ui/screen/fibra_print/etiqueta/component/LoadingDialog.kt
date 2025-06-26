package com.example.fibra_labeling.ui.screen.fibra_print.etiqueta.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun LoadingDialog(
    show: Boolean,
    message: String = "Eviando..."
) {
    if (show) {
        AlertDialog(
            onDismissRequest = { /* Ignorar o poner lógica para cerrar si quieres */ },
            confirmButton = {},
            dismissButton = {},
            title = null,
            text = {
                Column (
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    CircularProgressIndicator()
                    Spacer(modifier = Modifier.height(16.dp))
                    Text(text = message)
                }
            }
        )
    }
}