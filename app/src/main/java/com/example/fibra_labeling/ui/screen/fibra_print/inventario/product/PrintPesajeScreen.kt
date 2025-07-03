package com.example.fibra_labeling.ui.screen.fibra_print.inventario.product

import android.annotation.SuppressLint
import android.net.Uri
import androidx.activity.ComponentActivity
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.intl.Locale
import androidx.compose.ui.text.toUpperCase
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.example.fibra_labeling.R
import com.example.fibra_labeling.data.local.mapper.fibraprint.toOitmData
import com.example.fibra_labeling.ui.BarcodeViewModel
import com.example.fibra_labeling.ui.screen.component.CustomEmptyMessage
import com.example.fibra_labeling.ui.screen.component.CustomFioriTextField
import com.example.fibra_labeling.ui.screen.fibra_print.etiqueta.register.component.ProductCard
import com.example.fibra_labeling.ui.theme.FioriBackground
import org.koin.androidx.compose.koinViewModel

@SuppressLint("ContextCastToActivity")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PrintPesajeScreen(
    onNavigateBack: () -> Unit = {},
    viewModel: PrintPesajeViewModel= koinViewModel(),
    onNavigateToIncRegister: (String, String, String) -> Unit ,
    onNavigateToProducts: () -> Unit,
    navController: NavController,
    onNavigateToScan: ()-> Unit
) {

    val productos by viewModel.productos.collectAsState()
    val filtro by viewModel.filtro.collectAsState()
    val currentBackStackEntry = navController.currentBackStackEntryAsState().value

    val activity = LocalContext.current as ComponentActivity
    val barcodeViewModel: BarcodeViewModel = koinViewModel(
        viewModelStoreOwner = activity
    )

    LaunchedEffect(Unit) {
        barcodeViewModel.barcode.collect { scannedCode ->
            viewModel.setFiltro("$scannedCode*")
//            viewModel.obtenerPesaje(scannedCode)
            //mostrarScanner = false
        }
    }

    LaunchedEffect(currentBackStackEntry) {
        val result = currentBackStackEntry
            ?.savedStateHandle
            ?.get<String>("barcode_scan_result")
        if (result != null) {
            // Actualiza el ViewModel o llama a tu función:
            viewModel.setFiltro("$result*")
//            viewModel.obtenerPesaje(result)
            // Limpia el valor para que no se vuelva a disparar accidentalmente
            currentBackStackEntry.savedStateHandle["barcode_scan_result"] = ""
        }
    }

//    LaunchedEffect(Unit) {
//        viewModel.productos.collect { data ->
//            if (data.size<=1 && filtro.length>=17){
//                viewModel.setFiltro("")
//            }
//        }
//    }

    Scaffold (
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        text = "Productos a Inventariar",
                        style = MaterialTheme.typography.titleLarge,
                        color = MaterialTheme.colorScheme.onSurface,
                        fontWeight = FontWeight.Bold
                    )
                },
                navigationIcon = {
                    IconButton (
                        onClick = {
                            viewModel.updateUser()
                            onNavigateBack()
                        }

                    ) {
                        Icon(
                            painter = painterResource(R.drawable.ic_arrow_left),
                            contentDescription = "Volver",
                            tint = MaterialTheme.colorScheme.primary
                        )
                    }
                },
                actions = {
                    IconButton (
                        onClick = {
                            onNavigateToProducts()
                        }

                    ) {
                        Icon(
                            painter = painterResource(R.drawable.ic_plus),
                            contentDescription = "Add",
                            tint = MaterialTheme.colorScheme.primary
                        )
                    }
                }
            )
        },
        containerColor = FioriBackground
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(horizontal = 20.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            Spacer(modifier = Modifier.height(16.dp))

            // Campo de búsqueda

            CustomFioriTextField(
                value = filtro,
                onValueChange = {value->
                    viewModel.setFiltro(value)
                },
                label = "Buscar por nombre o código...",
                modifier = Modifier.padding(bottom = 20.dp).fillMaxWidth(),
                singleLine = true,
                leadingIcon = {
                    Icon(
                        painter = painterResource(R.drawable.ic_search),
                        contentDescription = "Buscar",
                        tint = MaterialTheme.colorScheme.primary
                    )
                },
                // mostrar solo si el filtro no está vacío
                trailingIcon = {
                    if (filtro.isNotEmpty()) {
                        IconButton(
                            onClick = {
//                                onNavigateToScan()
                                viewModel.setFiltro("")
                            }
                        ) {
                            Icon(
                                imageVector = Icons.Default.Close,
                                contentDescription = "clear",
                                tint = MaterialTheme.colorScheme.primary
                            )
                        }
                    }

                },
            )

            // Contador de resultados
            Text(
                text = "${productos.size} producto(s) encontrado(s)",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.padding(bottom = 12.dp)
            )

            if (productos.isEmpty()) {
                CustomEmptyMessage(
                    message = "Cree etiquetas para comenzar a inventariar productos"
                )
                Spacer(modifier = Modifier.height(16.dp))
                FilledTonalButton(
                    onClick = {
                        onNavigateToProducts()
                    },
                    colors = ButtonDefaults.filledTonalButtonColors(
                        containerColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.3f),
                    )
                ) {
                    Row(
                        modifier = Modifier
                            .padding(vertical = 4.dp)
                    ) {
                        Text(
                            "Añadir productos",
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurface,
                            fontWeight = FontWeight.Bold
                        )

                        Spacer(modifier = Modifier.height(10.dp))
                        Icon(
                            painter = painterResource(R.drawable.ic_plus),
                            contentDescription = "Añadir productos",
                            tint = MaterialTheme.colorScheme.onSurface
                        )
                    }

                }
            } else {
                LazyColumn(
                    verticalArrangement = Arrangement.spacedBy(12.dp),
                    contentPadding = PaddingValues(bottom = 24.dp)
                ) {
                    items(productos.size) { index ->
                        val product = productos[index]
                        ProductCard(
                            producto = product.toOitmData(),
                            modifier = Modifier.animateItem(),
                            onNavigateToDetail = {oITMData ->
                                val encodedProductName = Uri.encode(oITMData.desc)
                                val code = Uri.encode(oITMData.codesap)
                                val codeBar=Uri.encode(oITMData.codebars)
                                onNavigateToIncRegister(code, encodedProductName, codeBar)
                            }
                        )
                    }
                }
            }
        }
    }
}