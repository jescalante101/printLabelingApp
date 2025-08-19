package com.example.fibra_labeling.ui.screen.fibra_print.etiqueta.register

import android.net.Uri
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.intl.Locale
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.toUpperCase
import androidx.compose.ui.unit.dp
import com.example.fibra_labeling.R
import com.example.fibra_labeling.data.local.mapper.fibrafil.toOitmData
import com.example.fibra_labeling.ui.screen.component.CustomEmptyMessage
import com.example.fibra_labeling.ui.screen.fibra_print.etiqueta.register.component.ProductCard
import com.example.fibra_labeling.ui.screen.fibra_print.etiqueta.register.component.SearchBar
import com.example.fibra_labeling.ui.theme.FioriBackground
import org.koin.androidx.compose.koinViewModel


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PrintRegisterScreen(
    onBack: () -> Unit,
    onNavigateToNewPrint: (code: String, name: String) -> Unit,
    onNavigateToFillPrint: (itemCode:String,productName:String) -> Unit,
    viewModel: RegisterViewModel= koinViewModel(),
    isPrint: Boolean=true,
) {

    val oitmResult by viewModel.oitmResponse.collectAsState()
    var searchText by remember { mutableStateOf("") }
    val productos by viewModel.productos.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.changeIsPrint(isPrint)
        viewModel.getOitm(isFill = !isPrint)
    }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                navigationIcon = {
                    IconButton(
                        onClick = {
                            viewModel.updateUser()
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
                        text = "Productos",
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold,
                    )
                },
            )
        },
        containerColor = FioriBackground
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(it)
                .padding(horizontal = 20.dp)
        ) {

            Spacer(modifier = Modifier.height(16.dp))

            // Campo de búsqueda
            SearchBar(
                searchText = searchText,
                onSearchTextChange = { filter ->
                    searchText = filter.toUpperCase(Locale.current)
                    viewModel.setFiltro(filter)
                },
                modifier = Modifier.padding(bottom = 16.dp),
                onSearch = { text ->
                    if (isPrint) {
                        viewModel.getOitm(filter = text, isFill = false)
                    }
                    viewModel.setFiltro(text)
                },
                onDone = {
                    if (isPrint) {
                        viewModel.getOitm(filter = searchText, isFill = false)
                    }
                    viewModel.setFiltro(searchText)
                }
            )

            // Contador de resultados
            Text(
                text = "${productos.size} producto(s) encontrado(s)",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.padding(bottom = 12.dp)
            )

            // Conditional content based on isPrint flag
            if (!isPrint) {
                // Logic for isPrint = false (local list)
                if (productos.isEmpty()) {
                    CustomEmptyMessage(
                        title = "No se encontraron productos",
                        message = "Intenta con otros términos de búsqueda"
                    )
                } else {
                    LazyColumn(
                        verticalArrangement = Arrangement.spacedBy(12.dp),
                        contentPadding = PaddingValues(vertical = 8.dp)
                    ) {
                        items(productos.size) { index ->
                            val product = productos[index]
                            ProductCard(
                                producto = product.toOitmData()!!,
                                modifier = Modifier.animateItem(),
                                onNavigateToDetail = { data ->
                                    val encodedProductName = Uri.encode(data.desc)
                                    val code = Uri.encode(data.codesap)
                                    if (isPrint) {
                                        onNavigateToNewPrint(code, encodedProductName)
                                    } else {
                                        onNavigateToFillPrint(code, encodedProductName)
                                    }
                                }
                            )
                        }
                    }
                }
            } else {
                // Logic for isPrint = true (fetches from network)
                when {
                    oitmResult.isFailure -> {
                        val errorMsg = oitmResult.exceptionOrNull()?.message ?: "Error desconocido"
                        var msj = errorMsg.toString()
                        if (errorMsg.toString().contains("Connection timed out")) {
                            msj =
                                "La conexión falló. Es posible que el servidor esté experimentando dificultades o que tu conexión a internet no sea estable. Revisa tu conexión y vuelve a intentarlo."
                        }
                        Column(
                            modifier = Modifier.fillMaxSize(),
                            verticalArrangement = Arrangement.Center,
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            CustomEmptyMessage(title = "Error de Conexión", message = msj)
                            Spacer(modifier = Modifier.height(16.dp))
                            Button(
                                onClick = { viewModel.getOitm(searchText) },
                            ) {
                                Text("Reintentar")
                            }
                        }
                    }

                    productos.isEmpty() -> {
                        Box(
                            modifier = Modifier.fillMaxSize(),
                            contentAlignment = Alignment.Center
                        ) {
                            CircularProgressIndicator()
                        }
                    }

                    else -> {
                        LazyColumn(
                            verticalArrangement = Arrangement.spacedBy(12.dp),
                            contentPadding = PaddingValues(vertical = 8.dp)
                        ) {
                            items(productos.size) { index ->
                                val product = productos[index]
                                ProductCard(
                                    producto = product.toOitmData()!!,
                                    modifier = Modifier.animateItem(),
                                    onNavigateToDetail = { data ->
                                        val encodedProductName = Uri.encode(data.desc)
                                        val code = Uri.encode(data.codesap)
                                        if (isPrint) {
                                            onNavigateToNewPrint(code, encodedProductName)
                                        } else {
                                            onNavigateToFillPrint(code, encodedProductName)
                                        }
                                    }
                                )
                            }
                        }
                    }
                }
            }
        }
    }

}