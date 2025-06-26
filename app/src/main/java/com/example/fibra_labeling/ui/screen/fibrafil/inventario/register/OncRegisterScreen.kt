package com.example.fibra_labeling.ui.screen.fibrafil.inventario.register

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
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.fibra_labeling.ui.screen.fibrafil.inventario.component.FioriDropdownUser
import com.example.fibra_labeling.ui.screen.fibrafil.inventario.register.form.OncForm
import com.example.fibra_labeling.ui.theme.Fibra_labelingTheme
import kotlinx.coroutines.launch
import org.koin.androidx.compose.koinViewModel


@SuppressLint("ConfigurationScreenWidthHeight")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun OncRegisterScreen(
    showSheet: Boolean,
    onDismiss: () -> Unit,
    onSave: (OncForm) -> Unit,
    viewModel: OincRegisterViewModel = koinViewModel()
){
    val formState by viewModel.formState
    val errorMsg by viewModel.errorMsg

    val configuration = LocalConfiguration.current
    val isScreenSmall = configuration.screenHeightDp < 600

    val scope = rememberCoroutineScope()
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)

    val usuarios by viewModel.usuarios.collectAsState()

    if (showSheet) {

        ModalBottomSheet(
            onDismissRequest = {
                viewModel.reset()
                onDismiss()
            },

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

                FioriDropdownUser(
                    label = "Usuario",
                    options = usuarios,
                    selected = formState.usuario,
                    onSelectedChange = {
                        viewModel.onUsuarioChange(it)
                    },
                    onFilterChange = { filtro ->
                        viewModel.buscarUsuarios(filtro)
                    }
                )

                Spacer(Modifier.height(10.dp))
                OutlinedTextField(
                    value = formState.referencia,
                    onValueChange = { viewModel.onReferenciaChange(it) },
                    label = { Text("Referencia") },
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(Modifier.height(10.dp))
                OutlinedTextField(
                    value = formState.remarks,
                    onValueChange = { viewModel.onRemarksChange(it) },
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
                        scope.launch {
                            sheetState.hide()
                            viewModel.reset()
                            onDismiss()
                        }
                    }) { Text("Cancelar") }
                    Spacer(Modifier.width(10.dp))
                    Button(onClick = {
                        if (viewModel.validate()) {
                            viewModel.insertOinc()
                            onSave(formState)
                            scope.launch {
                                sheetState.hide()
                                viewModel.reset()
                                onDismiss()
                            }

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