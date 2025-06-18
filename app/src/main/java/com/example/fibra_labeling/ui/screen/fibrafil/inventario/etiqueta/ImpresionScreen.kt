package com.example.fibra_labeling.ui.screen.fibrafil.inventario.etiqueta

import android.annotation.SuppressLint
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
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
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.example.fibra_labeling.R
import com.example.fibra_labeling.data.model.fibrafil.ProductoDetalleUi
import com.example.fibra_labeling.ui.BarcodeViewModel
import com.example.fibra_labeling.ui.component.CustomAppBar
import com.example.fibra_labeling.ui.navigation.Screen
import com.example.fibra_labeling.ui.screen.fibrafil.inventario.component.CopiesInputDialog
import com.example.fibra_labeling.ui.screen.fibrafil.inventario.component.ProductoDetalleCard
import com.example.fibra_labeling.ui.screen.print.component.LoadingDialog
import com.example.fibra_labeling.ui.screen.print.component.MessageDialog
import org.koin.androidx.compose.koinViewModel

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

    CopiesInputDialog(
        show = showDialogCopies,
        onDismiss = { showDialogCopies = false },
        onConfirm = { nroCopias ->
            Log.e("Nro Copias",nroCopias.toString())
            viewModel.filPrintEtiqueta(
                nroCopias
            )
            showDialogCopies = false
        }
    )

    Box{
        Scaffold(
            floatingActionButton = {
                Column(
                    modifier = Modifier.padding(bottom = 16.dp),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.End
                ) {
                    if (isPrint) {
                        FloatingActionButton(
                            onClick = {
                                if(lastBarcode!=null){
                                    showDialogCopies=true
                                }

                            },
                            containerColor = Color(0xFF2C3E50),
                        ) {
                            Icon(
                                painter = painterResource(R.drawable.ic_print),
                                contentDescription = "Print",
                                tint = Color.White
                            )
                        }
                    }
                    Spacer(modifier = Modifier.height(16.dp))
                    FloatingActionButton(
                        onClick = {
                            // to product List
                            onNavigateToRegister()
                        },
                        containerColor = Color(0xFF2C3E50).copy(0.9f),
                    ) {
                        Row(
                            modifier = Modifier
                                .padding(horizontal = 16.dp)
                                .background(Color.Transparent),
                            horizontalArrangement = Arrangement.Center,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "Nueva Etiqueta",
                                color = Color.White
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Icon(
                                painter = painterResource(R.drawable.ic_new),
                                contentDescription = "Menu",
                                tint = Color.White
                            )
                        }
                    }
                }

            }
        ) {
            LazyColumn (
                modifier = Modifier
                    .fillMaxSize().padding(it).padding(top = 48.dp)
            ) {

                item {
                    Spacer(modifier = Modifier.height(24.dp))
                }
                item {

                    OutlinedTextField(
                        value = lastBarcode.toString(),
                        onValueChange = {
                            viewModel.actualizarCodeBar(it)
                        },
                        placeholder = {
                            Text(
                                text = "Use el dispositivo o cámara para escanear el código de barras....",
                                color = Color(0xFF95A5A6),
                                style = MaterialTheme.typography.bodySmall
                            )
                        },
                        leadingIcon = {
                            Icon(
                                painter = painterResource(R.drawable.barcode_scan),
                                contentDescription = "Barcode Icon",
                                tint = Color(0xFF7F8C8D)
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
                                    tint = Color(0xFF7F8C8D)
                                )
                            }
                        },
                        readOnly = true,
                        enabled = false,
                        singleLine = true,
                        shape = RoundedCornerShape(12.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = Color(0xFF3498DB),
                            unfocusedBorderColor = Color(0xFFE0E0E0),
                            focusedContainerColor = Color.White,
                            unfocusedContainerColor = Color.White,

                            ),
                        textStyle = MaterialTheme.typography.bodyMedium.copy(
                            color = Color.Black,
                            fontWeight = FontWeight.Medium
                        ),
                        modifier = Modifier
                            .fillMaxWidth()
                            .focusRequester(focusRequester)
                            .padding(horizontal = 16.dp),
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
                                Log.d("COD NO VALID",errorMsg)
                                var msj="No se ha podido obtener los datos para este codigo $lastBarcode"
                                if(errorMsg.toString().contains("Connection timed out")){
                                    msj="La conexión falló. Es posible que el servidor esté experimentando dificultades o que tu conexión a internet no sea estable. Revisa tu conexión y vuelve a intentarlo."
                                }
                                Text(
                                    text = msj,
                                    color = MaterialTheme.colorScheme.error.copy(0.8f),
                                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 16.dp)
                                )
                            }

                            etiquetaResult.isSuccess && etiquetaResult.getOrNull() != null -> {
                                val data = etiquetaResult.getOrNull()
                                if (data != null && data.codBar.isNotBlank()) {
                                    Column (
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .padding(vertical = 12.dp, horizontal = 4.dp)
                                    ) {
                                        AnimatedVisibility(
                                            visible = data.codBar.isNotBlank()
                                        ) {
                                            ProductoDetalleCard(
                                                item = data
                                            )
                                        }
                                    }
                                } else {

                                    Text(
                                        text = "¡Para obtener la información del producto o paquete, escanea el código de barras! Puedes usar la cámara de tu dispositivo o el lector integrado.",
                                        color = MaterialTheme.colorScheme.error,
                                        modifier = Modifier.padding(16.dp),
                                        style = MaterialTheme.typography.labelLarge
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
                }


            }
            LaunchedEffect (Unit) { focusRequester.requestFocus() }

        }


        Box(
            modifier = Modifier.padding(top= 32.dp)
        ){
            CustomAppBar(
                title = { Text("Generar Etiqueta", color = Color.Black, style = MaterialTheme.typography.titleMedium ) },
                leadingIcon = {
                    IconButton(
                        onClick = {
                            onBack()
                        }
                    ) {
                        Icon(
                            painter = painterResource(R.drawable.ic_arrow_left),
                            contentDescription = "Back",
                            tint = Color.Black
                        )
                    }
                },
                trailingIcon ={
                    IconButton(
                        onClick = {

                        }
                    ){
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