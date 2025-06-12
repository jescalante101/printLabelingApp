package com.example.fibra_labeling.ui.screen.print.register

import android.util.Log
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
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.rememberNestedScrollInteropConnection
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.fibra_labeling.R
import com.example.fibra_labeling.ui.component.CustomAppBar
import com.example.fibra_labeling.ui.screen.component.FioriDropdown
import com.example.fibra_labeling.ui.screen.print.register.component.FioriDropdownAlmacen
import com.example.fibra_labeling.ui.theme.Fibra_labelingTheme
import org.koin.androidx.compose.koinViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NewPrintScreen(
    onBack: () -> Unit,
    code: String,
    name:String,
    viewModel: NewPrintViewModel= koinViewModel()
) {


    val almacenes by viewModel.almacenes.collectAsState()
    val loading by viewModel.loading.collectAsState()
    val motivos :List<Motivo> = viewModel.motivos
    val pisos = (1..4).map { it.toString() }
    val proveedor by viewModel.proveedorName.collectAsState()
    var lote by remember { mutableStateOf("") }

    val formState by viewModel.formState.collectAsState()
    val formErrorState by viewModel.formErrorState.collectAsState()


    var motivo by remember { mutableStateOf(motivos.first()) }
    var ubicacion by remember { mutableStateOf("") }
    var piso by remember { mutableStateOf(pisos.first()) }
    var metroLineal by remember { mutableStateOf("") }
    var equivalente by remember { mutableStateOf("") }

    val pesajeResult by viewModel.print.collectAsState()
    val snackbarHostState = remember { SnackbarHostState() }

    LaunchedEffect (Unit) {
        Log.e("code,Name", "${code},${name}")
        viewModel.onCodigoChange(code)
        viewModel.onNameChange(name)

        viewModel.getProveedorName(code)
        viewModel.getAlmacens()

    }

    LaunchedEffect(pesajeResult) {
        when {
            pesajeResult.isSuccess -> {
                val data = pesajeResult.getOrNull()
                if (data?.success == true) {
                    if (data.result.isEmpty()){
                        snackbarHostState.showSnackbar(data.message.toString())
                        onBack()
                    }else{
                        snackbarHostState.showSnackbar(data.result.toString())
                        onBack()
                    }
                }
            }
            else -> {
                snackbarHostState.showSnackbar(pesajeResult.exceptionOrNull()?.message.orEmpty())
            }
        }
    }

    Box(
        modifier = Modifier.fillMaxSize()
    ) {

        if(loading){
            CircularProgressIndicator(
                modifier = Modifier.align(androidx.compose.ui.Alignment.Center)
            )
        }else{
            Scaffold(
                floatingActionButton = {
                    FloatingActionButton(
                        containerColor = Color(0xFF2C3E50),
                        onClick = {
                            viewModel.insertPesaje()
                        }
                    ) {
                        Row(
                            modifier = Modifier.padding(horizontal = 16.dp),
                            horizontalArrangement = Arrangement.Center,
                            verticalAlignment = androidx.compose.ui.Alignment.CenterVertically
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
                                withStyle(style= SpanStyle(color = Color.Black.copy(0.6f))) {
                                    append(formState.codigo)
                                }
                            },
                            style = MaterialTheme.typography.labelSmall,
                        )
                    }
                    item {
                        Text(
                            buildAnnotatedString {
                                withStyle(style= SpanStyle(color = Color.Black)) {
                                    append("Código:")
                                }
                                withStyle(style= SpanStyle(color = Color.Black.copy(0.6f))) {
                                    append(formState.name)
                                }
                            },
                            style = MaterialTheme.typography.labelSmall,
                        )
                    }
                    item {
                        Spacer(Modifier.height(2.dp))
                    }

                    item {
                        Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(24.dp)) {
                            if (proveedor.isSuccess){
                                FioriTextField(
                                    label = "Proveedor",
                                    value = formState.proveedor,
                                    onValueChange = {
                                        viewModel.onProveedorChange(it)
                                    },
                                    enabled = false,
                                    modifier = Modifier.weight(1f)
                                )
                            }else{
                                FioriTextField(
                                    label = "Proveedor",
                                    value = formState.proveedor,
                                    onValueChange = {
                                        viewModel.onProveedorChange(it)
                                    },
                                    enabled = false,
                                    modifier = Modifier.weight(1f)
                                )
                            }
                            FioriTextField(
                                label = "Lote",
                                value = formState.lote,
                                onValueChange = {
                                    lote = it
                                    viewModel.onLoteChange(it)
                                },
                                enabled = true,
                                modifier = Modifier.weight(1f),
                                isError = formErrorState.loteError != null,
                                supportingText = { formErrorState.loteError?.let { Text(it, color = Color.Red, fontSize = 12.sp) } }

                            )
                        }

                    }

                    item {
                        Spacer(Modifier.height(16.dp))
                    }

                    item {

                        FioriDropdownAlmacen(
                            label = "Almacén",
                            options = almacenes,
                            selected = formState.almacen,
                            onSelectedChange = { viewModel.onAlmacenChange(it) },
                            isError = formErrorState.almacenError != null,
                            supportingText = { formErrorState.almacenError?.let { Text(it, color = Color.Red, fontSize = 12.sp) } }
                        )

                    }

                    item {
                        Spacer(
                            Modifier.height(16.dp)
                        )
                    }

                    item {
                        FioriDropdown(
                            label = "Motivo",
                            options = motivos.map { it->it.descripcion },
                            selected = formState.motivo.toString(),
                            onSelectedChange = { value->
                                motivo = motivos.first { it.descripcion.toString().contentEquals(value) }
                                viewModel.onMotivoChange(motivo.codigo)
                            },
                            isError = formErrorState.motivoError != null,
                            supportingText = { formErrorState.motivoError?.let { Text(it, color = Color.Red, fontSize = 12.sp) } }

                            )
                    }

                    item {
                        Spacer(Modifier.height(16.dp))
                    }

                    // Fila 3: Ubicación, Piso, Metro Lineal, Equivalente
                    item {
                        Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(24.dp)) {
                            FioriTextField(
                                label = "Ubicación",
                                value = formState.ubicacion.toString(),
                                onValueChange = {
                                    ubicacion = it
                                    viewModel.onUbicacionChange(it)
                                                },
                                enabled = true,
                                modifier = Modifier.weight(1f),
                                isError = formErrorState.ubicacionError != null,
                                supportingText = { formErrorState.ubicacionError?.let { Text(it, color = Color.Red, fontSize = 12.sp) } }
                            )
                            FioriDropdown(
                                label = "Piso",
                                options = pisos,
                                selected = formState.piso.toString(),
                                onSelectedChange = {
                                    piso = it
                                    viewModel.onPisoChange(it)
                                },
                                modifier = Modifier.weight(1f),
                                isError = formErrorState.pisoError != null,
                                supportingText = { formErrorState.pisoError?.let { Text(it, color = Color.Red, fontSize = 12.sp) } }
                            )

                        }
                    }

                    item { Spacer(Modifier.height(16.dp)) }

                    item {
                        Row(
                            Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(24.dp)
                        ) {
                            FioriTextField(
                                label = "Metro Lineal",
                                value = formState.metroLineal,
                                onValueChange = {
                                    metroLineal = it
                                    viewModel.onMetroLinealChange(it)
                                },
                                enabled = true,
                                modifier = Modifier.weight(1f),
                                onlyNumbers = true,
                                isError = formErrorState.metroLinealError != null,
                                supportingText = { formErrorState.metroLinealError?.let { Text(it, color = Color.Red, fontSize = 12.sp) } }

                            )
                            FioriTextField(
                                label = "Equivalente",
                                value = formState.equivalente.toString(),
                                onValueChange = {
                                    equivalente = it
                                    viewModel.onEquivalenteChange(it)
                                },
                                enabled = true,
                                modifier = Modifier.weight(1f),

                            )
                        }
                    }
                    item {
                        Spacer(modifier = Modifier.height(16.dp))
                    }
                    item {
                        FioriTextField(
                            label = "Peso Bruto",
                            value = formState.pesoBruto,
                            onValueChange = {
                                viewModel.onPesoBrutoChange(it)
                            },
                            enabled = true,
                            modifier = Modifier.fillMaxWidth(),
                            onlyNumbers = true,
                            isError = formErrorState.pesoBrutoError != null,
                            supportingText = { formErrorState.pesoBrutoError?.let { Text(it, color = Color.Red, fontSize = 12.sp) } }

                        )
                    }
                    item {
                        Spacer(modifier = Modifier.height(36.dp))
                    }
                }

            }
        }

        Box(
            modifier = Modifier.padding(top= 32.dp)
        ){
            CustomAppBar(
                title = { Text("IMPRESIÓN ETIQUETA", color = Color.Black, style = MaterialTheme.typography.titleMedium ) },
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

// SAP Fiori style TextField
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FioriTextField(
    label: String,
    value: String,
    onValueChange: (String) -> Unit,
    enabled: Boolean,
    modifier: Modifier = Modifier,
    onlyNumbers: Boolean = false,
    isError: Boolean = false,
    supportingText: @Composable (() -> Unit)? = null,
) {
    OutlinedTextField(
        value = value,
        onValueChange = { newValue->
            if (onlyNumbers) {
                val filteredValue = newValue.filter { it.isDigit() || it == '.' }
                if (filteredValue.count { it == '.' } <= 1) {
                    onValueChange(filteredValue)
                }
            } else {
                onValueChange(newValue)
            }
        },
        enabled = enabled,
        readOnly = !enabled,
        modifier = modifier.fillMaxWidth(),
        label = { Text(label, fontSize = 13.sp) },
        singleLine = true,
        shape = RoundedCornerShape(8.dp),
        colors = TextFieldDefaults.colors(
            focusedContainerColor = Color(0xFFF7F7F7),
            unfocusedContainerColor = Color(0xFFF7F7F7),
            disabledContainerColor = Color(0xFFF7F7F7),
            focusedIndicatorColor = Color(0xFF0070F2),
            unfocusedIndicatorColor = Color(0xFFE0E0E0)
        ),
        keyboardOptions = if (onlyNumbers) KeyboardOptions(keyboardType = KeyboardType.Decimal) else KeyboardOptions.Default,
        isError = isError,
        supportingText = supportingText
    )
}





@Preview(showBackground = true)
@Composable
fun PreviewNewScreen(){
    Fibra_labelingTheme {
        Column(modifier = Modifier.fillMaxSize()) {
            NewPrintScreen(onBack = {},"","")
        }
    }
}
