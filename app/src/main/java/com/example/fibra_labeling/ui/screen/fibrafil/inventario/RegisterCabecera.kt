package com.example.fibra_labeling.ui.screen.fibrafil.inventario

import FioriCardConteoCompact
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
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
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.fibra_labeling.R
import com.example.fibra_labeling.data.local.entity.fibrafil.FibOincEntity
import com.example.fibra_labeling.ui.screen.fibra_print.etiqueta.register.component.SearchBar
import com.example.fibra_labeling.ui.screen.fibrafil.inventario.component.ConfirmSyncDialog
import com.example.fibra_labeling.ui.screen.fibrafil.inventario.register.OncRegisterScreen
import com.example.fibra_labeling.ui.theme.FioriBackground
import org.koin.androidx.compose.koinViewModel


@OptIn(ExperimentalMaterial3Api::class)
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
    val loading by viewModel.loading.collectAsState()
    val syncMessage by viewModel.syncMessage.collectAsState()
    val searchQuery by viewModel.searchQuery.collectAsState()

    val snackbarHostState = remember { SnackbarHostState() }

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
            showDialog = false
        }
    }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("Toma de Inventario", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = { onNavigateBack() }) {
                        Icon(
                            painter = painterResource(R.drawable.ic_arrow_left),
                            contentDescription = "Back",
                            tint = MaterialTheme.colorScheme.primary
                        )
                    }
                },
                actions = {
                    IconButton(onClick = { showSheet = true }) {
                        Icon(
                            painter = painterResource(R.drawable.ic_new),
                            contentDescription = "Nuevo",
                            tint = MaterialTheme.colorScheme.primary
                        )
                    }
                }
            )
        },
        snackbarHost = { SnackbarHost(snackbarHostState) },
        containerColor = FioriBackground
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(horizontal = 16.dp)
        ) {
            Spacer(modifier = Modifier.height(16.dp))
            SearchBar(
                searchText = searchQuery,
                onSearchTextChange = { text ->
                    viewModel.updateSearchQuery(text)
                },
                modifier = Modifier.padding(bottom = 16.dp),
                onSearch = { query -> viewModel.updateSearchQuery(query) }
            )

            LazyColumn(verticalArrangement = Arrangement.spacedBy(8.dp)) {
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

    if (loading) {
        AlertDialog(
            onDismissRequest = {},
            title = { Text("Sincronización en curso") },
            text = {
                Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.fillMaxWidth()) {
                    CircularProgressIndicator()
                    Spacer(modifier = Modifier.height(16.dp))
                    Text("Estamos enviando los datos a SAP...")
                }
            },
            confirmButton = {}
        )
    }

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
