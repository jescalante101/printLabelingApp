package com.example.fibra_labeling.ui.screen.inventory

import FioriCardConteoCompact
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
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
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.fibra_labeling.R
import com.example.fibra_labeling.data.local.entity.fibrafil.FibOincEntity
import com.example.fibra_labeling.ui.component.CustomAppBar
import com.example.fibra_labeling.ui.screen.component.CustomSearch
import com.example.fibra_labeling.ui.screen.inventory.component.ConfirmSyncDialog
import com.example.fibra_labeling.ui.screen.inventory.register.OncRegisterScreen
import org.koin.androidx.compose.koinViewModel


@Composable
fun RegisterCabecera(
    onNavigateBack: () -> Unit,
    onNavigateToFilEtiqueta: (isPrint: Boolean) -> Unit,
    viewModel: RegisterCabeceraViewModel = koinViewModel(),
    onNavigateToDetails: (docEntry: Int) -> Unit
) {
    var showSheet by remember { mutableStateOf(false) }
    var itemToSync by remember { mutableStateOf<FibOincEntity?>(null) }
    var showDialog by remember { mutableStateOf(false) }

    val oincs by viewModel.oincs.collectAsState()
    val loading by viewModel.loading.collectAsState()  // Estado de carga (sincronización en curso)
    val syncMessage by viewModel.syncMessage.collectAsState()
    val searchQuery by viewModel.searchQuery.collectAsState()

    val snackbarHostState = remember { SnackbarHostState() }

    val scope = rememberCoroutineScope()

    // Confirmación de sincronización (cuando el usuario acepta)
    fun onConfirmSync(dto: FibOincEntity) {
        viewModel.syncEtiquetaEncabezado(dto.docEntry)
    }

    fun onDismissDialog() {
        showDialog = false
    }

    LaunchedEffect(syncMessage) {
        syncMessage?.let {
            snackbarHostState.showSnackbar(it)
        }
    }
    LaunchedEffect(loading) {
        if (!loading) {
            showDialog = false // Cierra el diálogo de carga una vez que termina la sincronización
        }
    }

    Box {
        Scaffold(

            snackbarHost = { SnackbarHost(snackbarHostState) },
            floatingActionButton = {
                // Floating button para crear un nuevo registro
                ExtendedFloatingActionButton(
                    onClick = {
                        showSheet = true
                    },
                    containerColor = Color(0xFF2C3E50),
                ) {
                    Text("Nuevo", color = Color.White)
                }
            }

        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(it)
                    .padding(top = 60.dp)
            ) {
                CustomSearch(
                    value = searchQuery,

                    onChangeValue = {
                        text ->
                        viewModel.updateSearchQuery(text)
                    },

                    placeholder = "Buscar",
                    focusRequester = FocusRequester.Default,
                    leadingIcon = {
                        Icon(
                            painter = painterResource(R.drawable.barcode_scan),
                            contentDescription = "Barcode Icon",
                        )
                    },
                    trailingIcon = {
                        IconButton(
                            onClick = {}
                        ) {
                            Icon(
                                painter = painterResource(R.drawable.ic_search),
                                contentDescription = "Search",
                            )
                        }
                    },
                    isReadOnly = false,
                    enabled = true
                )
                Spacer(modifier = Modifier.height(8.dp))

                LazyColumn {
                    item { Spacer(modifier = Modifier.height(16.dp)) }
                    item { Spacer(modifier = Modifier.height(8.dp)) }
                    items(oincs.size) { index ->
                        val inc = oincs[index]
                        FioriCardConteoCompact(
                            dto = inc.cabecera,
                            isSyncing = loading,
                            onClick = {
                                viewModel.saveUserLogin(
                                    userLogin = inc.cabecera.u_UserNameCount ?: "",
                                    code = inc.cabecera.u_Ref ?: "",
                                    docEntry = inc.cabecera.docEntry.toString()
                                )
                                onNavigateToFilEtiqueta(false)
                            },

                            onDetailsClick = {
                                onNavigateToDetails(inc.cabecera.docEntry.toInt())
                            },

                            onSyncClick = {
                                showDialog = true
                                itemToSync = inc.cabecera
                            },

                            detailsEnabled = inc.detalles.isNotEmpty()
                        )
                    }
                }
            }

        }

        // Mostrar el modal de confirmación cuando el usuario quiere sincronizar
        if (showDialog) {
            ConfirmSyncDialog(
                isVisible = showDialog,
                onConfirm = {
                    itemToSync?.let {
                        onConfirmSync(it)
                    }
                },
                onDismiss = { onDismissDialog() }
            )
        }

        // **Bloqueo de pantalla durante la sincronización**:
        if (loading) {

            AlertDialog(
                onDismissRequest = {},
                title = { Text("Sincronización en curso") },
                text = {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        CircularProgressIndicator(color = Color.White)
                        Spacer(modifier = Modifier.height(16.dp))
                        Text("Estamos enviando los datos a SAP...")
                    }
                },
                confirmButton = {}
            )
        }

        // AppBar
        Box(modifier = Modifier.padding(top = 32.dp)) {
            CustomAppBar(
                title = { Text("Toma de Inventario", color = Color.Black, style = MaterialTheme.typography.titleMedium) },
                leadingIcon = {
                    IconButton(onClick = { onNavigateBack() }) {
                        Icon(
                            painter = painterResource(R.drawable.ic_arrow_left),
                            contentDescription = "Back",
                            tint = Color.Black
                        )
                    }
                },
                trailingIcon = {
                    IconButton(onClick = {}) {
                        Icon(
                            imageVector = Icons.Default.AccountCircle,
                            contentDescription = "user",
                            tint = Color.Black
                        )
                    }
                }
            )
        }
    }



    // Bottom sheet para el registro
    OncRegisterScreen(
        showSheet = showSheet,
        onDismiss = { showSheet = false },
        onSave = {}
    )
}


@Preview
@Composable
fun PreviewRegisterCabecera() {
    RegisterCabecera(
        onNavigateBack = {},
        onNavigateToFilEtiqueta = {},
        onNavigateToDetails = {}

    )
}