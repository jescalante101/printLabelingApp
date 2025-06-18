package com.example.fibra_labeling.ui.screen.fibrafil.inventario.etiquetanueva

import android.util.Log
import androidx.compose.foundation.clickable
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
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
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
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
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
import androidx.navigation.NavController
import com.example.fibra_labeling.R
import com.example.fibra_labeling.ui.component.CustomAppBar
import com.example.fibra_labeling.ui.navigation.Screen
import com.example.fibra_labeling.ui.screen.component.CustomTextFormField
import com.example.fibra_labeling.ui.screen.component.FioriDropdownMaquina
import com.example.fibra_labeling.ui.screen.inventory.register.stock.ICountingScreen
import com.example.fibra_labeling.ui.screen.print.register.component.FioriDropdownAlmacen
import org.koin.androidx.compose.koinViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddEtiquetaScreen(
    itemCode:String,
    productName:String,
    onBack: () -> Unit,
    navController: NavController,
    viewmodel: FillEtiquetaViewModel= koinViewModel()
){
    val formState = viewmodel.formState
    val errorState = viewmodel.errorState
    val isFormValid = viewmodel.isFormValid()
    val loading by viewmodel.loading.collectAsState()
    val almacenes by viewmodel.almacenes.collectAsState()
    val maquinas by viewmodel.maquinas.collectAsState()
    val user by viewmodel.user.collectAsState()

    val pesajeResult by viewmodel.print.collectAsState()
    val snackbarHostState = remember { SnackbarHostState() }

    var showSheet by remember { mutableStateOf(false) }
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)

    LaunchedEffect(Unit) {
        viewmodel.onCodigoChange(itemCode)
        viewmodel.onProductoChange(productName)
        viewmodel.getAlmacens()
        viewmodel.searchMaquina("","")
        viewmodel.getUserLogin()


    }



    LaunchedEffect(pesajeResult) {
        when {
            pesajeResult.isSuccess -> {
                val data = pesajeResult.getOrNull()
                if (data?.success == true) {
                    if (data.data==null){
                        snackbarHostState.showSnackbar(data.message.toString())
                        onBack()
                    }else{
                        snackbarHostState.showSnackbar(data.message.toString())
                        onBack()
                    }
                }
            }
            else -> {
                snackbarHostState.showSnackbar(pesajeResult.exceptionOrNull()?.message.orEmpty())
            }
        }
    }
    LaunchedEffect(Unit) {
        viewmodel.eventoNavegacion.collect { destino ->
            when (destino) {
                "printSetting" -> navController.navigate(Screen.PrintSetting.route)
                "savedLocal"->viewmodel.printEtiqueta()
                "successPrint" ->navController.popBackStack()
                "savedLocalNoPrint"->{
                    snackbarHostState.showSnackbar("Etiqueta guardada localmente, ahora puede imprimir")
                }
            }
        }
    }

    Box(
        modifier = Modifier.fillMaxSize()
    ) {

        if(loading){
            CircularProgressIndicator(
                modifier = Modifier.align(Alignment.Center)
            )
        }else{
            Scaffold(
                floatingActionButton = {
                    Column (
                        horizontalAlignment = Alignment.End,
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ){

                        FloatingActionButton(
                            containerColor = Color(0xFF2C3E50),
                            onClick =  {

                                viewmodel.updateOitw(false)
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
                                    contentDescription = "save",
                                    tint = Color.White
                                )
                            }
                        }
                        Spacer(Modifier.height(8.dp))
                        FloatingActionButton(
                            containerColor = Color(0xFF2C3E50),
                            onClick =  {
                                viewmodel.updateOitw(true)
                            }
                        ) {
                            Row(
                                modifier = Modifier.padding(horizontal = 16.dp),
                                horizontalArrangement = Arrangement.Center,
                                verticalAlignment = Alignment.CenterVertically
                            ) {

                                Text(
                                    "Guardar e Imprimir",

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
                    }


                },
                snackbarHost = { SnackbarHost(snackbarHostState) }
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
                                withStyle(style= SpanStyle(color = Color.Black.copy(0.8f))) {
                                     append(formState.codigo)
                                }
                            },
                            style = MaterialTheme.typography.labelLarge,
                        )
                    }
                    item {
                        Text(
                            buildAnnotatedString {
                                withStyle(style= SpanStyle(color = Color.Black)) {
                                    append("Producto:")
                                }
                                withStyle(style= SpanStyle(color = Color.Black.copy(0.8f))) {
                                    append(formState.producto)
                                }
                            },
                            style = MaterialTheme.typography.labelLarge,
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
                        Spacer(Modifier.height(8.dp))
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
                        Spacer(Modifier.height(8.dp))
                    }

                    // Fila 3: Ubicación, Piso, Metro Lineal, Equivalente
                    item {
                        CustomTextFormField(
                            label = "Refenencia",
                            value = formState.codigoReferencia,
                            onValueChange = {
                                viewmodel.onCodigoReferenciaChange(it)
                            },
                            enabled = true,

//                                isError = formErrorState.loteError != null,
//                                supportingText = { formErrorState.loteError?.let { Text(it, color = Color.Red, fontSize = 12.sp) } }

                        )
                    }
                    item {
                        Spacer(Modifier.height(8.dp))
                    }
                    item {

                        FioriDropdownMaquina(
                            label = "Máquina",
                            options = maquinas,
                            selected = formState.maquina,
                            onSelectedChange = { viewmodel.onMaquinaChange(it) },
                            onFilterChange = { text -> viewmodel.searchMaquina(text, text) },
                            isError = errorState.almacenError != null,
                            supportingText = { errorState.almacenError?.let { Text(it, color = Color.Red, fontSize = 12.sp) } }
                        )

                    }
                    item { Spacer(Modifier.height(8.dp)) }
                    item {
                        CustomTextFormField(
                            label = "Ubicacion",
                            value = formState.ubicacion,
                            onValueChange = {
                                viewmodel.onUbicacionChange(it)
                            },
                            enabled = true,

                            )
                    }
                    item {
                        Spacer(modifier = Modifier.height(8.dp))
                    }
                    item {
                        if(user.isNotBlank()){
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                            ) {
                                Button(
                                    onClick = {
                                        showSheet = true
                                    }
                                ) {
                                    Text("Conteo")
                                }
                                Spacer(Modifier.width(8.dp))
                                CustomTextFormField(
                                    label = "Conteo",
                                    value = formState.cantidad,
                                    onValueChange = {
                                        viewmodel.onCantidadChange(it)
                                    },
                                    enabled = false,
                                    modifier = Modifier.weight(1f).clickable(
                                        onClick = {
                                            showSheet = true
                                        }
                                    ),
                                    onlyNumbers = true
                                )
                                Spacer(Modifier.width(8.dp))


                            }
                        }
                    }
                    item {
                        if (user.isNotBlank()){
                            errorState.cantidadError?.let { Text(it, color = Color.Red, fontSize = 12.sp) }
                        }
                    }

                }

            }
        }

        if (showSheet) {
            ModalBottomSheet(
                onDismissRequest = { showSheet = false },
                sheetState = sheetState,
            ) {
                ICountingScreen(
                    onSave = { cantidad, stock ->
                        viewmodel.onCantidadChange(cantidad) // ← Aquí obtienes el valor cuando el usuario confirma
                        viewmodel.onStockChange(stock.toString())
                        showSheet = false
                    },
                    product = formState.producto,
                    itemCode = formState.codigo,
                    whsCode = formState.almacen?.whsCode ?: "CH3-RE"
                )
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
        onBack = TODO(),
        navController = TODO(),
        viewmodel = TODO()
    )
}
