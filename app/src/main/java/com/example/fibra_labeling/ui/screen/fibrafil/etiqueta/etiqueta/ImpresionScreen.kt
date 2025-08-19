package com.example.fibra_labeling.ui.screen.fibrafil.etiqueta.etiqueta

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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.example.fibra_labeling.R
import com.example.fibra_labeling.ui.BarcodeViewModel
import com.example.fibra_labeling.ui.navigation.Screen
import com.example.fibra_labeling.ui.screen.component.BarcodeInputField
import com.example.fibra_labeling.ui.screen.component.CustomEmptyMessage
import com.example.fibra_labeling.ui.screen.fibra_print.etiqueta.component.LoadingDialog
import com.example.fibra_labeling.ui.screen.fibra_print.etiqueta.component.MessageDialog
import com.example.fibra_labeling.ui.screen.fibrafil.etiqueta.component.CopiesAndTemplateDialog
import com.example.fibra_labeling.ui.screen.fibrafil.etiqueta.component.ProductoDetalleCard
import com.example.fibra_labeling.ui.theme.FioriBackground
import org.koin.androidx.compose.koinViewModel

@OptIn(ExperimentalMaterial3Api::class)
@SuppressLint("ContextCastToActivity")
@Composable
fun ImpresionScreen(
    onBack: () -> Unit,
    onNavigateToRegister: () -> Unit,
    onNavigateToScan: ()-> Unit,
    navController: NavController,
    viewModel: ImpresionModelView= koinViewModel()
){

    val currentBackStackEntry = navController.currentBackStackEntryAsState().value
    val isPrintLoading by viewModel.isPrintLoading.collectAsState()
    val etiquetaResult by viewModel.productDetailResult.collectAsState()
    val printResult by viewModel.printResult.collectAsState()
    val loading by viewModel.loading.collectAsState()
    var showDialog by remember { mutableStateOf(false) }
    val isPrint by viewModel.isPrint.collectAsState()
    var messagePrint by remember { mutableStateOf("") }
    val lastBarcode by viewModel.lastScannedBarcode.collectAsState()

    val focusRequester = remember { FocusRequester() }

    val activity = LocalContext.current as ComponentActivity
    val barcodeViewModel: BarcodeViewModel = koinViewModel(
        viewModelStoreOwner = activity
    )

    var showDialogCopies by remember { mutableStateOf(false) }

    val zplLabels = viewModel.labels.collectAsState().value

    LaunchedEffect(Unit) {
        barcodeViewModel.barcode.collect { scannedCode ->
            if (scannedCode!=null) {
                viewModel.actualizarCodeBar(scannedCode)
                viewModel.obtenerEtiqueta(scannedCode)
                //mostrarScanner = false
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
            viewModel.obtenerEtiqueta(result)
            // Limpia el valor para que no se vuelva a disparar accidentalmente
            currentBackStackEntry.savedStateHandle["barcode_scan_result"] = ""
        }
    }

    LoadingDialog(show = isPrintLoading)

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
                "printSetting" -> navController.navigate(Screen.PrintSetting.route)
            }
        }
    }

    CopiesAndTemplateDialog (
        show = showDialogCopies,
        onDismiss = { showDialogCopies = false },

        onConfirm = { nroCopias,templateId ->
            Log.e("Nro Copias",nroCopias.toString())
            viewModel.filPrintEtiqueta(
                nroCopias,templateId
            )

            showDialogCopies = false
        },
        templates = zplLabels,
        selectedTemplateId = null,
    )

    CopiesAndTemplateDialog (
        show = showDialogCopies,
        onDismiss = { showDialogCopies = false },

        onConfirm = { nroCopias,templateId ->
            Log.e("Nro Copias",nroCopias.toString())
            viewModel.filPrintEtiqueta(
                nroCopias,templateId
            )

            showDialogCopies = false
        },
        templates = zplLabels,
        selectedTemplateId = null,
    )

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
                                        showDialogCopies = true
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
        containerColor = FioriBackground,
        snackbarHost = { SnackbarHost(remember { SnackbarHostState() }) }
    ) {padding->
        LazyColumn (
            modifier = Modifier
                .fillMaxSize().padding(padding).padding(top = 16.dp)
        ) {


            item {
                BarcodeInputField(
                    barcodeValue = lastBarcode.toString(),
                    onValueChange = {
                        viewModel.actualizarCodeBar(it)
                        if (it.length>=17){
                            viewModel.obtenerEtiqueta(it)
                            viewModel.actualizarCodeBar("")
                            focusRequester.requestFocus()
                        }
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp),
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
                        etiquetaResult.isFailure -> {
                            val errorMsg = etiquetaResult.exceptionOrNull()?.localizedMessage ?: "Error desconocido"
                            var msj="No se ha podido obtener los datos para este codigo $lastBarcode"
                            if(errorMsg.toString().contains("Connection timed out")){
                                msj="La conexión falló. Es posible que el servidor esté experimentando dificultades o que tu conexión a internet no sea estable. Revisa tu conexión y vuelve a intentarlo."
                            }
                            CustomEmptyMessage(
                                title = "No se ha podido obtener los datos!",
                                message = msj
                            )
                        }
                        etiquetaResult.isSuccess && etiquetaResult.getOrNull() != null -> {
                            val data = etiquetaResult.getOrNull()
                            if (data != null && data.codeBar.isNotBlank()) {
                                Column (
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(vertical = 12.dp, horizontal = 4.dp)
                                ) {
                                    AnimatedVisibility(
                                        visible = data.codeBar.isNotBlank()
                                    ) {
                                        ProductoDetalleCard(
                                            item = data
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

    }

}


@Preview(showBackground = true)
@Composable
fun PreviewImpresion(){
    ImpresionScreen(
        onBack = {},
        onNavigateToRegister = {},
        onNavigateToScan = {},
        navController = NavController(LocalContext.current),
    )
}