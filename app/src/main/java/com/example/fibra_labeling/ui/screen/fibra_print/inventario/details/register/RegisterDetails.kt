package com.example.fibra_labeling.ui.screen.fibra_print.inventario.details.register

import androidx.compose.foundation.clickable
import androidx.compose.foundation.focusable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.FilledTonalIconButton
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
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
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.rememberNestedScrollInteropConnection
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import com.example.fibra_labeling.R
import com.example.fibra_labeling.ui.navigation.Screen
import com.example.fibra_labeling.ui.screen.component.CustomCountingWidget
import com.example.fibra_labeling.ui.screen.component.CustomFioriDropDown
import com.example.fibra_labeling.ui.screen.component.CustomFioriTextField
import com.example.fibra_labeling.ui.theme.FioriBackground
import org.koin.androidx.compose.koinViewModel


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PrintRegisterIncDetailsScreen(
    onBackNavigation: () -> Unit,
    viewModel: PrintRegisterIncDetailsViewModel= koinViewModel(),
    itemCode: String,
    itemName: String,
    codeBar: String,
) {


    val formState by viewModel.formState.collectAsState()
    val allAlmacenes by viewModel.allAlmacenes.collectAsState()

    var showSheet by remember { mutableStateOf(false) }
    var showSheetMetro by remember { mutableStateOf(false) }
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    val snackbarHostState = remember { SnackbarHostState() }

    val conteoMode by viewModel.conteoMode.collectAsState()

    val loading by viewModel.loading.collectAsState()


    LaunchedEffect (Unit) {
        viewModel.setCodigo(itemCode)
        viewModel.setNombreProducto(itemName)
        viewModel.setCodeBar(codeBar)
        viewModel.loadData()
    }

    if (loading) {
        AlertDialog(
            onDismissRequest = {},
            title = { Box(Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
                Text("Guardando...", fontWeight = FontWeight.Bold) } },
            text = {
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    CircularProgressIndicator()
                    Spacer(Modifier.height(16.dp))
                }
            },
            confirmButton = {}
        )
    }

    LaunchedEffect(Unit) {
        viewModel.eventoNavegacion.collect { destino ->
            when (destino) {
                "saved"->onBackNavigation()
            }
        }
    }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        "Registrar Detalle",
                        style = MaterialTheme.typography.titleLarge,
                        // Considera usar un color del tema, como:
                        color = MaterialTheme.colorScheme.onSurface,
                        fontWeight = FontWeight.Bold
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onBackNavigation) { // Acción a ejecutar al hacer clic
                        Icon(
                            painter = painterResource(id = R.drawable.ic_arrow_left), // Tu recurso drawable
                            contentDescription = "Atrás", // Descripción para accesibilidad
                            tint = MaterialTheme.colorScheme.primary // Tinte del ícono
                        )
                    }
                },
                actions = {
                    Row {
                        IconButton(
                            onClick = {
                                viewModel.saveInc()
                            }
                        ) {
                            Icon(
                                painter = painterResource(R.drawable.ic_save),
                                contentDescription = "Guardar",
                            )
                        }
                    }
                }
            )
        },
//        floatingActionButton = {
//            ExtendedFloatingActionButton (
//                onClick = {
//                    viewModel.saveInc()
//                },
//                containerColor = Color(0xFF2C3E50)
//            ){
//                Text(
//                    text = "Guardar",
//                    style = MaterialTheme.typography.labelLarge,
//                    color = Color.White,
//                    fontWeight = FontWeight.Bold
//                )
//            }
//        },
        containerColor = FioriBackground,
        snackbarHost = { SnackbarHost(snackbarHostState) }
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .fillMaxWidth()
                .padding(paddingValues)
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
                    style = MaterialTheme.typography.bodyMedium,
                )
            }

            item {
                Text(
                    buildAnnotatedString {
                        withStyle(style= SpanStyle(color = Color.Black)) {
                            append("Producto:")
                        }
                        withStyle(style= SpanStyle(color = Color.Black.copy(0.6f))) {
                            append(formState.nombreProducto)
                        }
                    },
                    style = MaterialTheme.typography.bodyMedium,
                )
            }
            item {

                CustomFioriTextField(
                    value = formState.proveedor?.cardName ?:"",
                    onValueChange = {

                    },
                    label ="Proveeor",
                    modifier = Modifier.fillMaxWidth().padding(top = 16.dp),
                    enabled = false,
                    singleLine = true,
                    trailingIcon = {
                        IconButton(
                            onClick = {}
                        ) {
                            Icon(
                                painter = painterResource(R.drawable.ic_edit),
                                contentDescription = "Editar",
                                tint = MaterialTheme.colorScheme.primary.copy(0.3f)
                            )
                        }
                    },
                    readOnly = true,
                )

            }

            item {
                CustomFioriDropDown(
                    label = "Almacén",
                    options = allAlmacenes,
                    selected = formState.almacen,
                    onSelectedChange = {whs->
                        viewModel.setAlmacen(whs)
                    },
                    onFilterChange = {whs->
                        viewModel.setFilterAlmacen(whs)
                    },
                    itemLabel = {whs->
                        "${whs.whsName} - ${whs.whsCode}"
                    },
                    enabled = false,
                    modifier = Modifier.padding(top = 16.dp)
                )
            }

            item {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.padding(top = 16.dp)
                ) {
                    Row {
                        FilledTonalIconButton (
                            colors = IconButtonDefaults.iconButtonColors(
                                MaterialTheme.colorScheme.primary.copy(0.3f)
                            ),
                            onClick = {
                                viewModel.decrementarConteo()
                            }
                        ) {
                            Icon(
                                painter = painterResource(R.drawable.ic_minus),
                                contentDescription = "minus",

                                )
                        }
                        Spacer(Modifier.width(4.dp))
                        FilledTonalIconButton(
                            colors = IconButtonDefaults.iconButtonColors(
                                MaterialTheme.colorScheme.primary.copy(0.3f)
                            ),
                            onClick = {
                                viewModel.incrementarConteo()
                            }
                        ) {
                            Icon(
                                painter = painterResource(R.drawable.ic_plus),
                                contentDescription = "plus",

                                )
                        }
                    }
                    Spacer(Modifier.width(8.dp))
                    CustomFioriTextField(
                        label = "Conteo",
                        placeholder = "0",
                        value = formState.conteo,
                        onValueChange = {
                            viewModel.onConteoChange(it)
                        },
                        enabled = !conteoMode,
                        modifier = Modifier.weight(1f).clickable(
                            onClick = {
                                if (conteoMode) {
                                    showSheet = true
                                }
                            }
                        ).onFocusChanged {
                            if (it.isFocused && conteoMode) {
                                showSheet = true
                            }
                        },
                        readOnly = conteoMode,
                        isOnlyNumber = true,
                        trailingIcon = {
                            Text(
                                formState.unidad,
                                style = MaterialTheme.typography.labelSmall,
                                color = Color.Black.copy(0.6f),
                                fontWeight = FontWeight.Bold
                            )
                        }

                    )
                    Spacer(Modifier.width(8.dp))
                }
            }
            item{

                CustomFioriTextField(
                    label = "Metro Lineal",
                    value = formState.metroLineal,
                    onValueChange = {
                        viewModel.onChangeMetroL(it)
                    },
                    enabled = !conteoMode,
                    readOnly = conteoMode,
                    modifier = Modifier.fillMaxWidth().padding(top = 16.dp).clickable(
                        onClick = {
                            if (conteoMode) {
                                showSheetMetro = true
                            }
                        }

                    ).onFocusChanged {
                        if (it.isFocused && conteoMode) {
                            showSheetMetro = true
                        }
                    },
                    isOnlyNumber = true
                )
            }

            item{
                CustomFioriTextField(
                    label = "Observación",
                    value = formState.observacion,
                    onValueChange = {
                        viewModel.onObservacionChange(it)
                    },
                    enabled = true,
                    modifier = Modifier.fillMaxWidth().padding(top = 16.dp),
                )
            }

        }
    }
    if (showSheet) {
        ModalBottomSheet(
            onDismissRequest = { showSheet = false },
            sheetState = sheetState,
        ) {
            CustomCountingWidget(
                onSave = { cantidad ->
//                    viewModel.onConteoChange(cantidad.toString()) // ← Aquí obtienes el valor cuando el usuario confirma
                    viewModel.onConteoChange(cantidad)
                    showSheet = false
                },
                product = formState.nombreProducto,
                itemCode = formState.codigo,
                conteo = formState.conteo.toDoubleOrNull() ?:0.0,
                unidad = formState.unidad
            )
        }
    }

    if (showSheetMetro) {
        ModalBottomSheet(
            onDismissRequest = { showSheetMetro = false },
            sheetState = sheetState,
        ) {
            CustomCountingWidget(
                onSave = { metro ->
//                    viewModel.onConteoChange(cantidad.toString()) // ← Aquí obtienes el valor cuando el usuario confirma
                    viewModel.onChangeMetroL(metro)
                    showSheetMetro = false
                },
                product = formState.nombreProducto,
                itemCode = formState.codigo,
                conteo = formState.metroLineal.toDoubleOrNull() ?:0.0,
                operation = "Metro Lineal"
            )
        }
    }
}