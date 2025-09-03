package com.example.fibra_labeling.fibraprint.ui.screen.etiqueta.impresion

import android.annotation.SuppressLint
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.text.KeyboardOptions
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
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.example.fibra_labeling.R
import com.example.fibra_labeling.core.navigation.SettingsDestination
import com.example.fibra_labeling.ui.BarcodeViewModel
import com.example.fibra_labeling.shared.ui.screen.component.BarcodeInputField
import com.example.fibra_labeling.shared.ui.screen.component.CustomEmptyMessage
import com.example.fibra_labeling.fibraprint.ui.screen.etiqueta.component.LoadingDialog
import com.example.fibra_labeling.fibraprint.ui.screen.etiqueta.component.MessageDialog
import com.example.fibra_labeling.fibraprint.ui.screen.etiqueta.component.SapFioriDetailCard
import com.example.fibra_labeling.ui.theme.FioriBackground
import org.koin.androidx.compose.koinViewModel

@SuppressLint("ContextCastToActivity")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PrintScreen(
    viewModel: PrintViewModel = koinViewModel(),
    onBack: () -> Unit,
    onNavigateToScan: () -> Unit,
    onNavigateToRegister: () -> Unit,
    onNavigateToSettings: ((SettingsDestination) -> Unit)? = null,
    navController: NavController
){
    val focusRequester = remember { FocusRequester() } // Necesitas importar FocusRequester
    val pesajeResult by viewModel.pesajeResult.collectAsState() // Necesitas agregar esto en tu ViewModel
    val loading by viewModel.loading.collectAsState()  // Necesitas agregar esto en tu ViewModel (ver abajo)
    val isPrint by viewModel.isPrint.collectAsState()

    val isPrintLoading by viewModel.isPrintLoading.collectAsState()
    val printResult by viewModel.printResult.collectAsState()

    var showDialog by remember { mutableStateOf(false) }

    var messagePrint by remember { mutableStateOf("") }

    val activity = LocalContext.current as ComponentActivity
    val snackbarHostState = remember { SnackbarHostState() }
    val barcodeViewModel: BarcodeViewModel = koinViewModel(
        viewModelStoreOwner = activity
    )
    val lastBarcode by viewModel.lastScannedBarcode.collectAsState()

    val currentBackStackEntry = navController.currentBackStackEntryAsState().value

    val keyboardController = LocalSoftwareKeyboardController.current

    LaunchedEffect(Unit) {
        barcodeViewModel.barcode.collect { scannedCode ->
            if (scannedCode!=null) {
                viewModel.actualizarCodeBar(scannedCode)
                viewModel.obtenerPesaje(scannedCode)
                //mostrarScanner = false
            }
        }
    }

    LoadingDialog(show = isPrintLoading)
    LaunchedEffect(Unit) {
        viewModel.printMessage.collect { error ->
            when(error){
                "successPrint"->snackbarHostState.showSnackbar("Etiqueta Impresa con exito")
                else->snackbarHostState.showSnackbar(error)
            }
        }
    }


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

    LaunchedEffect(printResult) {
        if (printResult.isSuccess && printResult.getOrNull() != null) {
            val data = printResult.getOrNull()

            if (data?.success == true && data.data != null) {
                messagePrint = data.message ?: "Impresión exitosa!"
                showDialog = true
            } else {
                messagePrint = data?.message ?: "Error desconocido en la impresión."
                showDialog = false
            }
        }else if(printResult.isFailure){
            messagePrint = printResult.exceptionOrNull()?.localizedMessage ?: "Error desconocido en la impresión."
            showDialog = true
        }else{
            showDialog=false
        }
    }


    MessageDialog(
        show = showDialog,
        message = messagePrint,
        onDismiss = {
            Log.d("PRINT",showDialog.toString())
            showDialog=false
        }
    )


    LaunchedEffect(Unit) {
        viewModel.eventoNavegacion.collect { destino ->
            when (destino) {
                "printSetting" -> onNavigateToSettings?.invoke(SettingsDestination.Printer)
            }
        }
    }


    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                navigationIcon = {
                    IconButton(
                        onClick = {
                            onBack()
                        }
                    ) {
                        Icon(
                            painter = painterResource(R.drawable.ic_arrow_left),
                            contentDescription = "Back",
                            tint = MaterialTheme.colorScheme.primary
                        )
                    }
                },
                title = {
                    Text("Imprimir Etiqueta",
                        color = Color.Black,
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold
                    )
                },
                actions = {
                    Row {
                        IconButton(
                            onClick = {
                                onNavigateToRegister()
                            }
                        ) {
                            Icon(
                                painter = painterResource(R.drawable.ic_edit),
                                contentDescription = "add",
                                tint = Color.Black
                            )
                        }
                        if (isPrint){
                            IconButton(
                                onClick = {
                                    if(lastBarcode!=null){
                                        viewModel.printPesaje(
                                            lastBarcode.toString().trim()
                                        )
                                        viewModel.actualizarCodeBar("")
                                    }
                                }

                            ) {
                                Icon(
                                    painter = painterResource(R.drawable.ic_print),
                                    contentDescription = "print",
                                    tint = Color.Black
                                )
                            }
                        }

                    }
                }

            )
        },

//        floatingActionButton = {
//            Column(
//                modifier = Modifier.padding(bottom = 16.dp),
//                verticalArrangement = Arrangement.Center,
//                horizontalAlignment = Alignment.End
//            ) {
//                if (isPrint) {
//                    FloatingActionButton(
//                        onClick = {
//                            if(lastBarcode!=null){
//                                viewModel.printPesaje(
//                                    lastBarcode.toString().trim()
//                                )
//                                viewModel.actualizarCodeBar("")
//                            }
//
//                        },
//                        //containerColor = Color(0xFF2C3E50),
//                        //containerColor = MaterialTheme.colorScheme.secondaryContainer
//                    ) {
//                        Icon(
//                            painter = painterResource(R.drawable.ic_print),
//                            contentDescription = "Menu",
//                            tint = Color.Black
//                        )
//                    }
//                }
//                Spacer(modifier = Modifier.height(16.dp))
//                ExtendedFloatingActionButton(
//                    onClick = {
//                        onNavigateToRegister()
//                    },
//                    containerColor = Color(0xFF2C3E50).copy(0.9f),
//                   // containerColor = MaterialTheme.colorScheme.primary
//
//                ){
//                    Row(
//                        modifier = Modifier
//                            .padding(horizontal = 16.dp)
//                            .background(Color.Transparent),
//                        horizontalArrangement = Arrangement.Center,
//                        verticalAlignment = Alignment.CenterVertically
//                    ) {
//                        Text(
//                            text = "Nuevo Etiqueta",
//                            color =  MaterialTheme.colorScheme.surface,
//                            style = MaterialTheme.typography.titleSmall
//                        )
//                        Spacer(modifier = Modifier.width(8.dp))
//                        Icon(
//                            painter = painterResource(R.drawable.ic_new),
//                            contentDescription = "Menu",
//                            tint = MaterialTheme.colorScheme.surface
//                        )
//                    }
//                }
//
//
//            }
//
//        },
//
        containerColor = FioriBackground,
        snackbarHost = { SnackbarHost(snackbarHostState) }
    ) {padding->
        LazyColumn (
            modifier = Modifier
                .fillMaxSize().padding(padding).padding(top = 16.dp)
        ) {


            item {
                BarcodeInputField(
                    barcodeValue =lastBarcode.toString(),
                    onValueChange = {

                        viewModel.actualizarCodeBar(it)
                        if (it.length>=17){
                            viewModel.obtenerPesaje(it)
                            viewModel.actualizarCodeBar("")
                            focusRequester.requestFocus()
                        }
//                        if(it.endsWith("\n")){
//
//                        }

                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp)
                        .onFocusChanged{focusState->
                            if(focusState.isFocused){
                                keyboardController?.hide()
                            }
                        },
                    onScanClick = {
                        onNavigateToScan()
                    },
                    scanComplete = {
                        if(lastBarcode!=null){
                            viewModel.actualizarCodeBar("")
                        }
                    },
                    editable = true,
                    readOnly = true,
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Text,
                        imeAction = ImeAction.Done
                    ),
                   focusRequester = focusRequester
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
                            var msj="No se ha podido obtener los datos para este codigo $lastBarcode"
                            if(errorMsg.toString().contains("Connection timed out")){
                                msj="La conexión falló. Es posible que el servidor esté experimentando dificultades o que tu conexión a internet no sea estable. Revisa tu conexión y vuelve a intentarlo."
                            }
                            CustomEmptyMessage(
                                title = "No se ha podido obtener los datos!",
                                message = msj
                            )
                        }
                        pesajeResult.isSuccess && pesajeResult.getOrNull() != null -> {
                            val data = pesajeResult.getOrNull()
                            if (data != null && data.codeBar.isNotBlank()) {
                                Column (
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(vertical = 12.dp, horizontal = 4.dp)
                                ) {
                                    AnimatedVisibility(
                                        visible = data.codeBar.isNotBlank()

                                    ) {
                                        SapFioriDetailCard(
                                            item = data,
                                            modifier = Modifier.padding(horizontal = 4.dp)
                                        )
                                    }
                                }
                            } else {
                                CustomEmptyMessage(
                                    title = "Aviso!",
                                    message = "¡Para obtener la información del producto o paquete, escanea el código de barras! Puedes usar la cámara de tu dispositivo o el lector integrado."
                                )
                            }
                        }
                        else -> {
                            CustomEmptyMessage(
                                title = "Aviso!",
                                message = "Ingresa un código de barras y presiona buscar."
                            )
                        }
                    }
                }
            }


        }

        //LaunchedEffect(Unit) { focusRequester.requestFocus() }

    }

//
//    Box{
//
//
//
//       Box(
//           modifier = Modifier.padding(top= 32.dp)
//       ){
//           CustomAppBar(
//               title = { Text("Generar Etiqueta", color = Color.Black, style = MaterialTheme.typography.titleMedium ) },
//               leadingIcon = {
//                   IconButton(
//                       onClick = {
//                           onBack()
//                       }
//                   ) {
//                       Icon(
//                           painter = painterResource(R.drawable.ic_arrow_left),
//                           contentDescription = "Back",
//                           tint = Color.Black
//                       )
//                   }
//               },
//               trailingIcon ={
//                   IconButton(
//                       onClick = {
//
//                       }
//                   ){
//                       Icon(
//                          imageVector = Icons.Default.AccountCircle,
//                           contentDescription = "user",
//                           tint = Color.Black
//                       )
//                   }
//               }
//           )
//       }
//    }

}