package com.example.fibra_labeling.shared.ui.screen.setting.servidor

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material3.Card
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.fibra_labeling.R
import com.example.fibra_labeling.shared.ui.screen.component.CustomEmptyMessage
import com.example.fibra_labeling.shared.ui.screen.component.CustomSwipeableItem
import com.example.fibra_labeling.shared.ui.screen.setting.servidor.component.ServerSettingFormSheet
import com.example.fibra_labeling.ui.theme.FioriBackground
import com.example.fibra_labeling.ui.theme.LightColors
import org.koin.androidx.compose.koinViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ServerSettingScreen(
    viewModel: ServerSettingViewModel = koinViewModel(), // Koin
    onBack: () -> Unit
) {

    val configs by viewModel.configs.collectAsState()
    val empresaSeleccionada by viewModel.empresaSeleccionada.collectAsState()

    //snackbar
    val snackbarHostState = remember { SnackbarHostState() }

    //form
    val formState by viewModel.formState
    var showForm by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        viewModel.message.collect { message ->
            when(message) {
                "error_form"->{
                    // show snackBar
                    snackbarHostState.showSnackbar("Error en el formulario de configuración")
                }
                "error_url"->{
                    snackbarHostState.showSnackbar("Error en la URL ingresada, debe ser válida")
                }
                "success_register"->{
                    snackbarHostState.showSnackbar("Se registró correctamente")                }
                else -> {
                    snackbarHostState.showSnackbar(message)
                }
            }
        }
    }

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) },
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        "Conexiones de $empresaSeleccionada",
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold
                    )
                },
                navigationIcon = {
                    IconButton(onClick = {
                        onBack()
                    }) {
                        Icon(
                           painter = painterResource(R.drawable.ic_arrow_left),
                            contentDescription = "Volver",
                            tint = LightColors.primary
                        )
                    }
                },
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = {
                    showForm=true
                },
                shape = RoundedCornerShape(16.dp),
                containerColor = LightColors.primary
            ) {
                Icon(
                    painter = painterResource(R.drawable.ic_plus),
                    contentDescription = "Nueva conexión",
                    tint = LightColors.onPrimary
                )
            }
        },
        containerColor = FioriBackground
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .padding(horizontal = 20.dp, vertical = 10.dp)
                .fillMaxSize()
        ) {
            if (configs.isEmpty()) {
                CustomEmptyMessage(
                    message = "No hay configuraciones de conexión disponibles para $empresaSeleccionada"
                )
            } else {
                LazyColumn (
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f, fill = false)
                ) {
                    items(configs.size) { index ->
                        val setting = configs[index]
                        val selected = setting.isSelect

                        CustomSwipeableItem(
                            item = setting,
                            itemId = setting.id,
                            canSwipe = {true},
                            onDelete = {
                                viewModel.onEliminar(setting)
                            },
                        ) {
                            Card(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 4.dp)
                                    .selectable(
                                        selected = selected,
                                        onClick = {
                                            viewModel.onSeleccionar(setting)
                                        }
                                    ),
                                shape = MaterialTheme.shapes.medium,

                                ) {
                                Row (
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(vertical = 7.dp)
                                        .padding(horizontal = 14.dp, vertical = 16.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Column(
                                        modifier = Modifier.weight(1f)
                                    ) {
                                        Text(
                                            setting.nombre,
                                            style = MaterialTheme.typography.titleMedium.copy(
                                                color = if (selected) LightColors.primary else LightColors.onSurface,
                                                fontWeight = if (selected) FontWeight.Bold else null
                                            )
                                        )
                                        Spacer(Modifier.height(3.dp))
                                        Text(
                                            setting.urlBase,
                                            style = MaterialTheme.typography.bodySmall,
                                        )
                                    }
                                    if (selected) {
                                        Icon(
                                            imageVector = Icons.Filled.CheckCircle,
                                            contentDescription = "Seleccionada",
                                            tint = LightColors.primary,
                                            modifier = Modifier.padding(start = 6.dp)
                                        )
                                    }
                                }
                            }
                        }

                    }
                }
            }
            Spacer(Modifier.height(22.dp))
        }
    }

    if (showForm) {
        ModalBottomSheet (
            onDismissRequest = {
                showForm = false
                viewModel.onCancelar()
            }
        ) {
            ServerSettingFormSheet(
                formState = formState,
                onNombreChange = viewModel::onNombreChange,
                onUrlChange = viewModel::onUrlChange,
                onGuardar = {
                    viewModel.onGuardar()
                    showForm = false
                },
                onCancelar = {
                    showForm = false
                    viewModel.onCancelar()
                }
            )
        }
    }
}
