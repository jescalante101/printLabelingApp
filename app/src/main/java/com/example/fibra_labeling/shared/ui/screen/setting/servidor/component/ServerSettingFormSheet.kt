package com.example.fibra_labeling.shared.ui.screen.setting.servidor.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.fibra_labeling.shared.ui.screen.setting.servidor.form.ServerSettingFormState
import com.example.fibra_labeling.ui.theme.LightColors


@Composable
fun ServerSettingFormSheet(
    formState: ServerSettingFormState,
    onNombreChange: (String) -> Unit,
    onUrlChange: (String) -> Unit,
    onGuardar: () -> Unit,
    onCancelar: () -> Unit
) {
    Column(
        Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .background(
                color = LightColors.primaryContainer.copy(alpha = 0.07f),
                shape = RoundedCornerShape(16.dp)
            )
            .padding(20.dp)
    ) {
        OutlinedTextField(
            value = formState.nombre,
            onValueChange = onNombreChange,
            label = { Text("Nombre de la conexión") },
            singleLine = true,
            isError = formState.nombreError != null,
            supportingText = {
                formState.nombreError?.let { Text(it, color = MaterialTheme.colorScheme.error) }
            },
            shape = RoundedCornerShape(12.dp),
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(Modifier.height(12.dp))
        OutlinedTextField(
            value = formState.url,
            onValueChange = onUrlChange,
            label = { Text("URL base") },
            singleLine = true,
            isError = formState.urlError != null,
            supportingText = {
                formState.urlError?.let { Text(it, color = MaterialTheme.colorScheme.error) }
            },
            shape = RoundedCornerShape(12.dp),
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(Modifier.height(18.dp))
        Row {
            Button(
                onClick = onGuardar,
                modifier = Modifier.weight(1f),
                shape = RoundedCornerShape(12.dp),
                enabled = formState.isValid
            ) {
                Text("Guardar")
            }
            Spacer(Modifier.width(12.dp))
            OutlinedButton(
                onClick = onCancelar,
                modifier = Modifier.weight(1f),
                shape = RoundedCornerShape(12.dp)
            ) {
                Text("Cancelar")
            }
        }
    }
}
