package com.example.fibra_labeling.ui.screen.fibrafil.inventario.etiquetanueva

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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.rememberNestedScrollInteropConnection
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.fibra_labeling.R
import com.example.fibra_labeling.ui.component.CustomAppBar
import com.example.fibra_labeling.ui.screen.component.CustomTextFormField
import com.example.fibra_labeling.ui.screen.print.register.component.FioriDropdownAlmacen
import org.koin.androidx.compose.koinViewModel

@Composable
fun AddEtiquetaScreen(
    itemCode:String,
    productName:String,
    onBack: () -> Unit,
    viewmodel: FillEtiquetaViewModel= koinViewModel()
){
    val formState = viewmodel.formState
    val errorState = viewmodel.errorState
    val isFormValid = viewmodel.isFormValid()

    val almacenes by viewmodel.almacenes.collectAsState()

    LaunchedEffect(Unit) {
        viewmodel.onCodigoChange(itemCode)
        viewmodel.onProductoChange(productName)
        viewmodel.getAlmacens()
    }

    Box(
        modifier = Modifier.fillMaxSize()
    ) {

        if(false){
            CircularProgressIndicator(
                modifier = Modifier.align(Alignment.Center)
            )
        }else{
            Scaffold(
                floatingActionButton = {
                    Column {
                        FloatingActionButton(
                            containerColor = Color(0xFF2C3E50),
                            onClick = {
                                //viewModel.insertPesaje()
                            }
                        ) {
                            Row(
                                modifier = Modifier.padding(horizontal = 16.dp),
                                horizontalArrangement = Arrangement.Center,
                                verticalAlignment = Alignment.CenterVertically
                            ) {

                                Text(
                                    "Imprimir",

                                    style = MaterialTheme.typography.bodyMedium,
                                    color = Color.White
                                )

                                Spacer(Modifier.width(8.dp))

                                Icon(
                                    painter = painterResource(R.drawable.ic_print),
                                    contentDescription = "Print",
                                    tint = Color.White
                                )
                            }
                        }
                        Spacer(modifier = Modifier.height(16.dp))
                        FloatingActionButton(
                            containerColor = Color(0xFF2C3E50),
                            onClick = {
                                //viewModel.insertPesaje()
                            }
                        ) {
                            Row(
                                modifier = Modifier.padding(horizontal = 16.dp),
                                horizontalArrangement = Arrangement.Center,
                                verticalAlignment = Alignment.CenterVertically
                            ) {

                                Text(
                                    "Guardar",

                                    style = MaterialTheme.typography.bodyMedium,
                                    color = Color.White
                                )

                                Spacer(Modifier.width(8.dp))

                                Icon(
                                    painter = painterResource(R.drawable.ic_save),
                                    contentDescription = "Print",
                                    tint = Color.White
                                )
                            }
                        }
                    }


                },
                //snackbarHost = { SnackbarHost(snackbarHostState) }
            ){



                LazyColumn (
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(it)
                        .padding(horizontal = 24.dp, vertical = 16.dp)
                        .nestedScroll(rememberNestedScrollInteropConnection())
                        .imePadding()
                ) {
                    item {
                        Spacer(Modifier.height(70.dp))
                    }
                    item{
                        Text(
                            buildAnnotatedString {
                                withStyle(style= SpanStyle(color = Color.Black)) {
                                    append("Código:")
                                }
                                withStyle(style= SpanStyle(color = Color.Black.copy(0.6f))) {
                                     append(formState.codigo)
                                    "FFC"
                                }
                            },
                            style = MaterialTheme.typography.labelSmall,
                        )
                    }
                    item {
                        Text(
                            buildAnnotatedString {
                                withStyle(style= SpanStyle(color = Color.Black)) {
                                    append("Producto:")
                                }
                                withStyle(style= SpanStyle(color = Color.Black.copy(0.6f))) {
                                    append(formState.producto)
                                }
                            },
                            style = MaterialTheme.typography.labelSmall,
                        )
                    }
                    item {
                        Spacer(Modifier.height(2.dp))
                    }

                    item {
                        CustomTextFormField(
                            label = "Lote",
                            value = formState.lote,
                            onValueChange = {
//                                    lote = it
                                    viewmodel.onLoteChange(it)
                            },
                            enabled = true,
                            )

                    }

                    item {
                        Spacer(Modifier.height(16.dp))
                    }

                    item {


                        FioriDropdownAlmacen(
                            label = "Almacén",
                            options = almacenes,
                            selected = formState.almacen,
                            onSelectedChange = { viewmodel.onAlmacenChange(it) },
                            isError = errorState.almacenError != null,
                            supportingText = { errorState.almacenError?.let { Text(it, color = Color.Red, fontSize = 12.sp) } }
                        )

                    }

                    item {
                        Spacer(
                            Modifier.height(16.dp)
                        )
                    }

                    item {
                        Spacer(Modifier.height(16.dp))
                    }

                    // Fila 3: Ubicación, Piso, Metro Lineal, Equivalente
                    item {
                        Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(24.dp)) {
                            CustomTextFormField(
                                label = "Cod. Refenencia",
                                value = formState.codigoReferencia,
                                onValueChange = {
                                    viewmodel.onCodigoReferenciaChange(it)
                                },
                                enabled = true,
                                modifier = Modifier.weight(1f),
//                                isError = formErrorState.loteError != null,
//                                supportingText = { formErrorState.loteError?.let { Text(it, color = Color.Red, fontSize = 12.sp) } }

                            )

                            CustomTextFormField(
                                label = "Maquina",
                                value = formState.maquina,
                                onValueChange = {
                                    viewmodel.onMaquinaChange(it)
                                },
                                enabled = true,
                                modifier = Modifier.weight(1f),
//                                isError = formErrorState.loteError != null,
//                                supportingText = { formErrorState.loteError?.let { Text(it, color = Color.Red, fontSize = 12.sp) } }

                            )

                        }
                    }

                    item { Spacer(Modifier.height(16.dp)) }

                    item {
                        CustomTextFormField(
                            label = "Ubicacion",
                            value = formState.ubicacion,
                            onValueChange = {
                                viewmodel.onUbicacionChange(it)
                            },
                            enabled = true,

//                                isError = formErrorState.loteError != null,
//                                supportingText = { formErrorState.loteError?.let { Text(it, color = Color.Red, fontSize = 12.sp) } }

                        )
                    }
                    item {
                        Spacer(modifier = Modifier.height(16.dp))
                    }

                }

            }
        }

        Box(
            modifier = Modifier.padding(top= 32.dp)
        ){
            CustomAppBar(
                title = { Text("Nueva Etiqueta", color = Color.Black, style = MaterialTheme.typography.titleMedium ) },
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
fun PreviewAddEtiquetaScreen() {
    AddEtiquetaScreen(
        itemCode = TODO(),
        productName = TODO(),
        onBack = TODO()
    )
}
