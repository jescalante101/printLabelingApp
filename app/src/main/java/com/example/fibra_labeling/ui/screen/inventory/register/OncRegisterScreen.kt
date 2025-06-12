package com.example.fibra_labeling.ui.screen.inventory.register

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.fibra_labeling.ui.screen.component.DatePickerField
import com.example.fibra_labeling.ui.screen.inventory.register.form.OncForm
import com.example.fibra_labeling.ui.theme.Fibra_labelingTheme
import kotlinx.coroutines.launch


@SuppressLint("ConfigurationScreenWidthHeight")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun OncRegisterScreen(
    showSheet: Boolean,
    onDismiss: () -> Unit,
    onSave: (OncForm) -> Unit
){

    val configuration = LocalConfiguration.current
    val isScreenSmall = configuration.screenHeightDp < 600

    val scope = rememberCoroutineScope()
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)

    var formState by remember { mutableStateOf(OncForm()) }
    var errorMsg by remember { mutableStateOf<String?>(null) }

    if (showSheet) {
        ModalBottomSheet(
            onDismissRequest = onDismiss,
            sheetState = sheetState,
            modifier = if (isScreenSmall) Modifier.fillMaxSize() else Modifier
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .verticalScroll(rememberScrollState())
                    .imePadding()
                    .padding(horizontal = 24.dp, vertical = 8.dp)
            ) {
                Text(
                    text = "Registrar Conteo",
                    style = MaterialTheme.typography.titleMedium,
                    modifier = Modifier.padding(bottom = 18.dp)
                )

                OutlinedTextField(
                    value = formState.usuario,
                    onValueChange = { formState = formState.copy(usuario = it) },
                    label = { Text("Usuario") },
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(Modifier.height(10.dp))
                DatePickerField(
                    label = "Fecha de conteo",
                    value = formState.countDate,
                    onValueChange = { formState = formState.copy(countDate = it) }
                )
                Spacer(Modifier.height(10.dp))
                OutlinedTextField(
                    value = formState.referencia,
                    onValueChange = { formState = formState.copy(referencia = it) },
                    label = { Text("Referencia") },
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(Modifier.height(10.dp))
                OutlinedTextField(
                    value = formState.remarks,
                    onValueChange = { formState = formState.copy(remarks = it) },
                    label = { Text("Observaciones") },
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(Modifier.height(18.dp))
                errorMsg?.let {
                    Text(
                        text = it,
                        color = MaterialTheme.colorScheme.error,
                        modifier = Modifier.padding(bottom = 10.dp)
                    )
                }
                Row(
                    Modifier
                        .fillMaxWidth()
                        .padding(bottom = 8.dp),
                    horizontalArrangement = Arrangement.End
                ) {
                    TextButton(onClick = {
                        scope.launch { sheetState.hide(); onDismiss() }
                    }) { Text("Cancelar") }
                    Spacer(Modifier.width(10.dp))
                    Button(onClick = {
                        // Validación simple
                        if (formState.usuario.isBlank() || formState.countDate.isBlank()) {
                            errorMsg = "Usuario y fecha de conteo son obligatorios."
                        } else {
                            errorMsg = null
                            onSave(formState)
                            scope.launch { sheetState.hide(); onDismiss() }
                        }
                    }) {
                        Text("Guardar")
                    }
                }
            }
        }
    }


}

@Preview(showBackground = true)
@Composable
fun PreviewOncRegisterScreen() {
    Fibra_labelingTheme {
        OncRegisterScreen(
            showSheet = true,
            onDismiss = {},
            onSave = {}
        )
    }
}