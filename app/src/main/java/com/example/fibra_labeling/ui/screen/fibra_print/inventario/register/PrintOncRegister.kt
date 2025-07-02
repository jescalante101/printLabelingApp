package com.example.fibra_labeling.ui.screen.fibra_print.inventario.register

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.intl.Locale
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.toUpperCase
import androidx.compose.ui.unit.dp
import com.example.fibra_labeling.ui.screen.component.CustomFioriTextField
import com.example.fibra_labeling.ui.screen.fibra_print.etiqueta.component.PrintFioriDropdownUser
import com.example.fibra_labeling.ui.screen.fibra_print.inventario.register.form.PrintOincFormState
import com.example.fibra_labeling.ui.screen.fibra_print.inventario.register.form.TipoRegistro
import com.example.fibra_labeling.ui.theme.FioriBackground
import kotlinx.coroutines.launch
import okio.utf8Size
import org.koin.androidx.compose.koinViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PrintOncRegister(
    showSheet: Boolean,
    onDismiss: () -> Unit,
    onSave: (PrintOincFormState) -> Unit,
    viewModel: PrintRegisterOIncViewModel= koinViewModel()
){

    val configuration = LocalConfiguration.current
    val isScreenSmall = configuration.screenHeightDp < 600

    val scope = rememberCoroutineScope()
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)

    var selectedOption by remember { mutableStateOf("Usuario") }

    val allUsers by viewModel.allUsers.collectAsState()
    val formState = viewModel.formState



    if (showSheet) {

        ModalBottomSheet(
            containerColor = FioriBackground,
            onDismissRequest = {
//                viewModel.reset()
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
                    text = "Nuevo Inventario",
                    style = MaterialTheme.typography.titleLarge,
                    modifier = Modifier.padding(bottom = 18.dp)
                        .fillMaxWidth(),
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Center
                )
                Row(
                    Modifier
                        .fillMaxWidth()
                        .padding(bottom = 10.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = androidx.compose.ui.Alignment.CenterVertically,

                ) {


                    CustomFioriTextField(
                        value = viewModel.fecha!!,
                        onValueChange = {
//                            viewModel.onReferenciaChange(it)
                        },
                        label = "Fecha Inicio",
                        modifier = Modifier.weight(1f),
                        enabled = false
                    )

                    Spacer(Modifier.width(10.dp))
                    CustomFioriTextField(
                        value = viewModel.hora!!,
                        onValueChange = {
//                            viewModel.onReferenciaChange(it)
                        },
                        label = "Hora",
                        modifier = Modifier.weight(1f),
                        enabled = false
                    )
                }
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 10.dp, top = 8.dp),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    FilterChip(
                        selected = formState.selectedOption == TipoRegistro.USUARIO,
                        onClick = { viewModel.onSelectedOptionChange(TipoRegistro.USUARIO) },
                        label = {
                            Text(
                                "Usuario",
                                style = MaterialTheme.typography.bodyLarge,
                                modifier = Modifier.padding(vertical = 4.dp),
                                fontWeight = FontWeight.Bold
                            )
                        },
                        shape = MaterialTheme.shapes.medium,
                        colors = FilterChipDefaults.filterChipColors(
                            selectedContainerColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.3f),
                            selectedLabelColor = MaterialTheme.colorScheme.onPrimaryContainer,
                            containerColor = MaterialTheme.colorScheme.surface,
                            labelColor = MaterialTheme.colorScheme.onSurface
                        ),
                        modifier = Modifier
                            .height(48.dp)
                    )
                    FilterChip(
                        selected = formState.selectedOption == TipoRegistro.EMPLEADO,
                        onClick = { viewModel.onSelectedOptionChange(TipoRegistro.EMPLEADO) },
                        label = {
                            Text(
                                "Empleado",
                                style = MaterialTheme.typography.bodyLarge,
                                modifier = Modifier.padding(vertical = 4.dp),
                                fontWeight = FontWeight.Bold
                            )
                        },
                        shape = MaterialTheme.shapes.medium,
                        colors = FilterChipDefaults.filterChipColors(
                            selectedContainerColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.3f),
                            selectedLabelColor = MaterialTheme.colorScheme.onPrimaryContainer,
                            containerColor = MaterialTheme.colorScheme.surface,
                            labelColor = MaterialTheme.colorScheme.onSurface
                        ),
                        modifier = Modifier
                            .height(48.dp)
                    )
                }


                // Mostrar dinámicamente según selección
                if (formState.selectedOption== TipoRegistro.USUARIO) {
                    PrintFioriDropdownUser(
                        label = "Usuario",
                        options = allUsers,
                        selected = formState.selectedUser,
                        onSelectedChange = {
                            viewModel.onSelectedUserChange(it)
                        },
                        onFilterChange = {
                            viewModel.onSearchChange(it)
                        },
                        modifier = Modifier.fillMaxWidth().heightIn(max=250.dp)
                    )
                } else {
                    // Aquí puedes reemplazar por tu dropdown de empleados si tienes otro
                    CustomFioriTextField(
                        value = formState.selectedEmpleado,
                        onValueChange = {
                            viewModel.onSelectedEmpleadoChange(it)
                        },
                        label = "Empleado",
                        modifier = Modifier.fillMaxWidth()
                    )
                }

                Spacer(Modifier.height(10.dp))
                CustomFioriTextField(
                    value = formState.referencia,
                    onValueChange = {
                        viewModel.onReferenciaChange(it)
                    },
                    label = "Zona",
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(Modifier.height(10.dp))
                CustomFioriTextField(
                 value = formState.observaciones,
                    onValueChange = {
                        viewModel.onObservacionesChange(it.toUpperCase(Locale.current))
                    },
                    label = "Mueble",
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(Modifier.height(18.dp))
                viewModel.formState.errorMessage?.let {
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