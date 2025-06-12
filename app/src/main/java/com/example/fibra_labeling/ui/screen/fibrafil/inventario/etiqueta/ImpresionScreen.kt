package com.example.fibra_labeling.ui.screen.fibrafil.inventario.etiqueta

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
import androidx.compose.foundation.layout.imePadding
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.rememberNestedScrollInteropConnection
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.fibra_labeling.R
import com.example.fibra_labeling.data.model.ImobPasaje
import com.example.fibra_labeling.data.model.ProductoDetalleUi
import com.example.fibra_labeling.ui.component.CustomAppBar
import com.example.fibra_labeling.ui.screen.component.CustomTextFormField
import com.example.fibra_labeling.ui.screen.fibrafil.inventario.component.ProductoDetalleCard
import com.example.fibra_labeling.ui.screen.print.component.SapFioriDetailCard

@Composable
fun ImpresionScreen(
    onBack: () -> Unit,
    onNavigateToRegister: () -> Unit,
){


    Box{
        Scaffold(
            floatingActionButton = {
                Column(
                    modifier = Modifier.padding(bottom = 16.dp),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.End
                ) {
//                    if (isPrint) {
//                        FloatingActionButton(
//                            onClick = {
//                                if(lastBarcode!=null){
//                                    viewModel.printPesaje(
//                                        lastBarcode.toString().trim()
//                                    )
//                                }
//
//                            },
//                            containerColor = Color(0xFF2C3E50),
//                        ) {
//                            Icon(
//                                painter = painterResource(R.drawable.ic_print),
//                                contentDescription = "Menu",
//                                tint = Color.White
//                            )
//                        }
//                    }
                    Spacer(modifier = Modifier.height(16.dp))
                    FloatingActionButton(
                        onClick = {
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
                                text = "Nuevo",
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
                        value = "lastBarcode.toString()",
                        onValueChange = {
                           // viewModel.actualizarCodeBar(it)
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
                                    //TODO: ABRIR LA PANTALLA PARA ESCANEAR POR CAMARA
                                   // onNavigateToScan()
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
                            //.focusRequester(focusRequester)
                            .padding(horizontal = 16.dp),
                    )
                }

//                item {
//                    if (loading) {
//                        Row (
//                            modifier = Modifier.fillMaxWidth(),
//                            horizontalArrangement = Arrangement.Center
//                        ) {
//                            CircularProgressIndicator()
//                        }
//                    } else {
//                        when {
//
//                            pesajeResult.isFailure -> {
//                                val errorMsg = pesajeResult.exceptionOrNull()?.localizedMessage ?: "Error desconocido"
//                                Log.d("COD NO VALID",errorMsg)
//                                var msj="No se ha podido obtener los datos para este codigo $lastBarcode"
//                                if(errorMsg.toString().contains("Connection timed out")){
//                                    msj="La conexión falló. Es posible que el servidor esté experimentando dificultades o que tu conexión a internet no sea estable. Revisa tu conexión y vuelve a intentarlo."
//                                }
//                                Text(
//                                    text = msj,
//                                    color = MaterialTheme.colorScheme.error.copy(0.8f),
//                                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 16.dp)
//                                )
//                            }
//
//                            pesajeResult.isSuccess && pesajeResult.getOrNull() != null -> {
//                                val data = pesajeResult.getOrNull()
//                                if (data != null && data.codeBar.isNotBlank()) {
//                                    Column (
//                                        modifier = Modifier
//                                            .fillMaxWidth()
//                                            .padding(vertical = 12.dp, horizontal = 4.dp)
//                                    ) {
//                                        AnimatedVisibility { }(
//                                            visible = data.codeBar.isNotBlank()
//
//                                        ) {
//                                            SapFioriDetailCard(
//                                                item = data,
//                                                modifier = Modifier.padding(horizontal = 4.dp)
//                                            )
//                                        }
//                                    }
//                                } else {
//
//                                    Text(
//                                        text = "¡Para obtener la información del producto o paquete, escanea el código de barras! Puedes usar la cámara de tu dispositivo o el lector integrado.",
//                                        color = MaterialTheme.colorScheme.error,
//                                        modifier = Modifier.padding(16.dp),
//                                        style = MaterialTheme.typography.labelLarge
//                                    )
//
//                                }
//                            }
//                            else -> {
//                                Text(
//                                    text = "Ingresa un código de barras y presiona buscar.",
//                                    modifier = Modifier.padding(16.dp)
//                                )
//                            }
//                        }
//                    }
//                }

                item {
                    ProductoDetalleCard(
                        item = ProductoDetalleUi(
                            codigo = "12345",
                            productoName = "Lámina PVC",
                            lote = "L20240612",
                            referencia = "REF-ABC-01",
                            maquina = "Cortadora A",
                            codBar = "1234567890123"
                        )
                    )
                }


            }
//            LaunchedEffect (Unit) { focusRequester.requestFocus() }

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
        onNavigateToRegister = {}
    )
}