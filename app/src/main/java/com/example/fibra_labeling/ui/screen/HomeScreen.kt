package com.example.fibra_labeling.ui.screen

import BarcodeScannerScreen
import android.Manifest
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.focusable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.example.fibra_labeling.R
import org.koin.androidx.compose.koinViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(viewModel: HomeViewModel= koinViewModel()){
    var codValue by remember { mutableStateOf("") }
    val focusRequester = remember { FocusRequester() }

    val pesajeResult by viewModel.pesajeResult.collectAsState()
    val loading by viewModel.loading.collectAsState()  // Necesitas agregar esto en tu ViewModel (ver abajo)

    var mostrarDialogo by remember { mutableStateOf(false) }
    val context = LocalContext.current
    var mostrarScanner by remember { mutableStateOf(false) }




    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            TopAppBar(title = { Text("Labeling") })
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
        ) {
            
            if (mostrarScanner) {
                BarcodeScannerScreen (
                    onBarcodeScanned = { scannedCode ->
                        codValue = scannedCode
                        mostrarScanner = false

                    },
                    onBack = { mostrarScanner = false }
                )
            } else {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(16.dp)
                ) {
                    OutlinedTextField(
                        value = codValue,
                        onValueChange = { codValue = it },
                        label = { Text("Código de barras") },
                        maxLines = 1,
                        modifier = Modifier
                            .fillMaxWidth()
                            .focusRequester(focusRequester),
                        leadingIcon = {
                            Icon(
                                painter = painterResource(R.drawable.barcode_scan),
                                contentDescription = "Barcode Icon"
                            )
                        },
                        trailingIcon = {
                            IconButton(
                                onClick = { mostrarScanner = true }
                            ) {
                                Icon(
                                    painter = painterResource(R.drawable.camera),
                                    contentDescription = "Escanear"
                                )
                            }
                        },
                        enabled = true
                    )
                    Spacer(modifier = Modifier.height(24.dp))

                    Card (
                        colors = CardDefaults.cardColors(MaterialTheme.colorScheme.onPrimary),
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 12.dp)

                    ) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Text("Código: ", style = MaterialTheme.typography.titleMedium)
                            Text("Nombre:")
                            Text("Proveedor: ")
                            Text("Lote: ")
                            Text("Almacén: ")
                            Text("Peso: ")
                            Text("Motivo: ")
                            Text("Ubicación: ")
                            Text("Piso: ")
                            Text("Metro lineal: ")
                            Text("Equivalente: ")
                            Text("Fecha de creación: ")
                            Text("Usuario de creación: ")
                            Text("Fecha de actualización: ")
                            Text("Usuario de actualización: ")
                            Text("Estado: ")

                        }
                    }
                }



                // Estado de Loading
                /*
                if (loading) {
                    Row (
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.Center
                    ) {
                        CircularProgressIndicator()
                    }
                } else {
                    when {
                        pesajeResult.isFailure -> {
                            val errorMsg = pesajeResult.exceptionOrNull()?.localizedMessage ?: "Error desconocido"
                            Text(
                                text = "Error: $errorMsg",
                                color = MaterialTheme.colorScheme.error,
                                modifier = Modifier.padding(16.dp)
                            )
                        }
                        pesajeResult.isSuccess && pesajeResult.getOrNull() != null -> {
                            val data = pesajeResult.getOrNull()
                            if (data != null && data.codeBar.isNotBlank()) {
                                Card (
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(vertical = 12.dp)
                                ) {
                                    Column(modifier = Modifier.padding(16.dp)) {
                                        Text("Código: ${data.codeBar}", style = MaterialTheme.typography.titleMedium)
                                        Text("Nombre: ${data.name}")
                                        Text("Proveedor: ${data.proveedor}")
                                        Text("Lote: ${data.lote}")
                                        Text("Almacén: ${data.almacen}")
                                        Text("Peso: ${data.peso}")
                                        Text("Motivo: ${data.motivo}")
                                        Text("Ubicación: ${data.ubicacion}")
                                        Text("Piso: ${data.piso}")
                                        Text("Metro lineal: ${data.metroLineal}")
                                        Text("Equivalente: ${data.equivalente}")
                                        Text("Fecha de creación: ${data.createDate}")
                                        Text("Usuario de creación: ${data.userCreate}")
                                        Text("Fecha de actualización: ${data.updateDate}")
                                        Text("Usuario de actualización: ${data.userUpdate}")
                                        Text("Estado: ${data.status}")

                                    }
                                }
                            } else {
                                Text(
                                    text = "No se encontraron datos.",
                                    modifier = Modifier.padding(16.dp)
                                )
                            }
                        }
                        else -> {
                            Text(
                                text = "Ingresa un código de barras y presiona buscar.",
                                modifier = Modifier.padding(16.dp)
                            )
                        }
                    }
                }

                 */
            }
            // Focus automático
            LaunchedEffect(Unit) { focusRequester.requestFocus() }
        }
    }
}