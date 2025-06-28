package com.example.fibra_labeling.ui.screen.fibra_print.etiqueta.register

import android.util.Log
import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.material3.CenterAlignedTopAppBar
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.fibra_labeling.R
import com.example.fibra_labeling.ui.screen.component.CustomFioriDropDown
import com.example.fibra_labeling.ui.screen.component.CustomFioriTextField
import com.example.fibra_labeling.ui.screen.component.FioriDropdown
import com.example.fibra_labeling.ui.screen.fibra_print.etiqueta.component.PrintFioriDropdownProveedor
import com.example.fibra_labeling.ui.screen.fibra_print.etiqueta.register.component.FioriDropdownAlmacen
import com.example.fibra_labeling.ui.theme.Fibra_labelingTheme
import com.example.fibra_labeling.ui.theme.FioriBackground
import kotlinx.coroutines.delay
import org.koin.androidx.compose.koinViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NewPrintScreen(
    onBack: () -> Unit,
    code: String,
    name:String,
    viewModel: NewPrintViewModel= koinViewModel()
) {

    val pisos = (1..5).map { it.toString() }
    val formState = viewModel.formState
    val snackbarHostState = remember { SnackbarHostState() }

    val proveedores by viewModel.allProveedores.collectAsState()
    val allAlmacenes by viewModel.allAlmacenes.collectAsState()



    LaunchedEffect (Unit) {
        Log.e("code,Name", "${code},${name}")
        viewModel.onCodigoChange(code)
        viewModel.onNameChange(name)
    }

    LaunchedEffect(Unit) {
        viewModel.eventNotification.collect {
            eventNotification->
            when(eventNotification) {
                "saveSuccess" -> {
                    snackbarHostState.showSnackbar("Etiqueta guardada correctamente")
                    delay(30)
                    onBack()
                }
                "saveError" -> {
                    snackbarHostState.showSnackbar("Error al guardar la etiqueta")
                }
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
                    Text(
                        text = "Registrar Etiqueta",
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold,
                    )
                },

                )
        },
        floatingActionButton = {
            Column{
                FloatingActionButton(
                    containerColor = Color(0xFF2C3E50),
                    onClick = {
                        viewModel.saveLocal()
                    }
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 16.dp),
                        horizontalArrangement = Arrangement.Center,
                        verticalAlignment = androidx.compose.ui.Alignment.CenterVertically
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
                Spacer(Modifier.height(16.dp))
                FloatingActionButton(
                    containerColor = Color(0xFF2C3E50),
                    onClick = {
                       // viewModel.insertPesaje()
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
            }

        },
        snackbarHost = { SnackbarHost(snackbarHostState) },
        contentColor = FioriBackground
    ){ it ->
        LazyColumn (
            modifier = Modifier
                .fillMaxWidth()
                .padding(it)
                .padding(horizontal = 24.dp, vertical = 16.dp)
                .nestedScroll(rememberNestedScrollInteropConnection())
                .imePadding()
        ) {
            item {
                Spacer(Modifier.height(16.dp))
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
                Spacer(Modifier.height(8.dp))
            }
            item {
                CustomFioriDropDown(
                    label = "Proveedor",
                    options = proveedores,
                    selected = formState.proveedor,
                    onSelectedChange = {pro->
                        viewModel.onProveedorChange(pro)
                    },
                    onFilterChange = {filter->
                        viewModel.setFilter(filter)
                    },
                    itemLabel = {pro->
                        "${pro.cardName} - ${pro.cardCode}"
                    }
                )
            }
            item {
                Spacer(Modifier.height(16.dp))
            }
            item {
                CustomFioriTextField(
                    label = "Lote",
                    value = formState.lote,
                    onValueChange = {
                        viewModel.onLoteChange(it)
                    },
                    enabled = true,
                    modifier = Modifier.fillMaxWidth(),
                )
            }

            item {
                Spacer(Modifier.height(16.dp))
            }

            item {
                CustomFioriDropDown(
                    label = "Almacén",
                    options = allAlmacenes,
                    selected = formState.almacen,
                    onSelectedChange = {whs->
                        viewModel.onAlmacenChange(whs)
                    },
                    onFilterChange = {whs->
                        viewModel.setFilterAlmacen(whs)
                    },
                    itemLabel = {whs->
                        "${whs.whsName} - ${whs.whsCode}"
                    }
                )
            }
            item {
                Spacer(
                    Modifier.height(16.dp)
                )
            }

            // Fila 3: Ubicación, Piso, Metro Lineal, Equivalente
            item {
                Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(24.dp)) {
                    CustomFioriTextField(
                        label = "Ubicación",
                        value = formState.ubicacion.toString(),
                        onValueChange = {
                            viewModel.onUbicacionChange(it)
                        },
                        enabled = true,
                        modifier = Modifier.weight(1f),
                    )

                    FioriDropdown(
                        label = "Piso",
                        options = pisos,
                        selected = formState.piso.toString(),
                        onSelectedChange = {
                            viewModel.onPisoChange(it)
                        },
                        modifier = Modifier.weight(1f),
                    )

                }
            }
            item { Spacer(Modifier.height(16.dp)) }
            item {
                CustomFioriTextField(
                    label = "Metro Lineal",
                    value = formState.metroLineal,
                    onValueChange = {
                        viewModel.onMetroLinealChange(it)
                    },
                    enabled = true,
                    isOnlyNumber =  true,
                    modifier = Modifier.fillMaxWidth(),
                )

            }
            item {
                Spacer(modifier = Modifier.height(16.dp))
            }
            item {

                CustomFioriTextField(
                    label = "Peso Bruto",
                    value = formState.pesoBruto,
                    onValueChange = {
                        viewModel.onPesoBrutoChange(it)
                    },
                    enabled = true,
                    modifier = Modifier.fillMaxWidth(),
                    isOnlyNumber = true,

                )
            }
            item {
                Spacer(modifier = Modifier.height(16.dp))
            }
            item{
                CustomFioriTextField(
                    label = "Zona",
                    value = formState.zona,
                    onValueChange = {
                        viewModel.onZonaChange(it)
                    },
                    enabled = true,
                    modifier = Modifier.fillMaxWidth(),

                )
            }
        }

    }




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
