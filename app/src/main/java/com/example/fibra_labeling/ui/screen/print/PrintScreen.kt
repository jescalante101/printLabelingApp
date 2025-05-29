package com.example.fibra_labeling.ui.screen.print

import android.annotation.SuppressLint
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.example.fibra_labeling.R
import com.example.fibra_labeling.ui.BarcodeViewModel
import com.example.fibra_labeling.ui.screen.print.component.SapFioriDetailCard
import org.koin.androidx.compose.koinViewModel

@SuppressLint("ContextCastToActivity")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PrintScreen(
    viewModel: PrintViewModel= koinViewModel(),
    onBack: () -> Unit,
    onNavigateToScan: () -> Unit,
    navController: NavController
){
    val focusRequester = remember { FocusRequester() } // Necesitas importar FocusRequester
    val pesajeResult by viewModel.pesajeResult.collectAsState() // Necesitas agregar esto en tu ViewModel
    val loading by viewModel.loading.collectAsState()  // Necesitas agregar esto en tu ViewModel (ver abajo)
    val activity = LocalContext.current as ComponentActivity


    val barcodeViewModel: BarcodeViewModel = koinViewModel(
        viewModelStoreOwner = activity
    )

    LaunchedEffect(Unit) {
        barcodeViewModel.barcode.collect { scannedCode ->
            if (scannedCode!=null) {
                viewModel.actualizarCodeBar(scannedCode)
                viewModel.obtenerPesaje(scannedCode)
                //mostrarScanner = false
            }
        }
    }




    val lastBarcode by viewModel.lastScannedBarcode.collectAsState()
    val currentBackStackEntry = navController.currentBackStackEntryAsState().value

    LaunchedEffect(currentBackStackEntry) {
        val result = currentBackStackEntry
            ?.savedStateHandle
            ?.get<String>("barcode_scan_result")
        if (result != null) {
            // Actualiza el ViewModel o llama a tu función:
            viewModel.actualizarCodeBar(result)
            viewModel.obtenerPesaje(result)
            // Limpia el valor para que no se vuelva a disparar accidentalmente
            currentBackStackEntry.savedStateHandle["barcode_scan_result"] = ""
        }
    }



    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            TopAppBar(
                title = { Text("Labeling") },
                navigationIcon = {
                    IconButton(onClick = { onBack() }) {
                        Icon(
                            painter = painterResource(R.drawable.ic_arrow_left),
                            contentDescription = "Back"
                        )
                    }
                }
            )
        },
        floatingActionButton = {

            FloatingActionButton(
                onClick = {

                },
                shape = CircleShape,
                contentColor = Color.White
            ) {
                Icon(
                    painter = painterResource(R.drawable.ic_print),
                    contentDescription = "Print"
                )
            }
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
        ) {

            LazyColumn (
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp)
            ) {
                item {
                    OutlinedTextField(
                        shape = MaterialTheme.shapes.medium,
                        value = lastBarcode.toString(),
                        onValueChange = {
                            viewModel.actualizarCodeBar(it)
                        },
                        label = {
                            Text(
                                "Código de barras",
                                color = MaterialTheme.colorScheme.onSurface
                            )
                                },
                        maxLines = 1,
                        modifier = Modifier
                            .fillMaxWidth()
                            .focusRequester(focusRequester),
                        leadingIcon = {
                            Icon(
                                painter = painterResource(R.drawable.barcode_scan),
                                contentDescription = "Barcode Icon",
                                tint = Color.White
                            )
                        },
                        trailingIcon = {
                            IconButton(
                                onClick = {
                                    onNavigateToScan()
                                }
                            ) {
                                Icon(
                                    painter = painterResource(R.drawable.camera),
                                    contentDescription = "Escanear",
                                    tint = Color.White
                                )
                            }
                        },
                        readOnly = true,
                        enabled = false,
                        textStyle = TextStyle.Companion.Default.copy(
                            color = MaterialTheme.colorScheme.onSurface,
                            fontSize = MaterialTheme.typography.bodyMedium.fontSize,
                            fontFamily = MaterialTheme.typography.bodyMedium.fontFamily

                        ),
                        colors = TextFieldDefaults.colors(Color.White)
                    )
                }

                item {
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
                                Log.d("COD NO VALID",errorMsg)
                                Text(

                                    text = "No se ha podido obtener los datos para este codigo ${lastBarcode.toString()}",
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
                                        SapFioriDetailCard(
                                            item = data,
                                        )
                                    }
                                } else {

                                    Text(
                                        text = "¡Para obtener la información del producto o paquete, escanea el código de barras! Puedes usar la cámara de tu dispositivo o el lector integrado.",
                                        color = MaterialTheme.colorScheme.error,
                                        modifier = Modifier.padding(16.dp),
                                        style = MaterialTheme.typography.titleLarge
                                    )

//                                    data?.let {
//                                        SapFioriDetailCard(
//                                            item = it,
//                                        )
//                                    }
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
                }

            }




                // Estado de Loading





            // Focus automático
            LaunchedEffect(Unit) { focusRequester.requestFocus() }
        }
    }
}