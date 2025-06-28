package com.example.fibra_labeling.ui.screen.fibra_print.etiqueta.products

import android.net.Uri
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.intl.Locale
import androidx.compose.ui.text.toUpperCase
import androidx.compose.ui.unit.dp
import com.example.fibra_labeling.R
import com.example.fibra_labeling.data.local.mapper.fibraprint.toOitmData
import com.example.fibra_labeling.ui.screen.component.CustomEmptyMessage
import com.example.fibra_labeling.ui.screen.fibra_print.etiqueta.register.component.ProductCard
import com.example.fibra_labeling.ui.screen.fibra_print.etiqueta.register.component.SearchBar
import com.example.fibra_labeling.ui.theme.FioriBackground
import org.koin.androidx.compose.koinViewModel


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PrintProductScreen(
    viewModel: PrintProductViewModel = koinViewModel(),
    onNavigateToNewPrint: (String, String) -> Unit,
    onBack: () -> Unit
){

    val filtro by viewModel.filtro.collectAsState()
    val productos by viewModel.productos.collectAsState()

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
                        text = "Productos a etiquetar",
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold,
                    )
                },

            )
        },
        containerColor = FioriBackground,

    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(FioriBackground)
                .padding(it)
                .padding(horizontal = 20.dp)
        ) {

            Spacer(modifier = Modifier.height(16.dp))

            // Campo de búsqueda
            SearchBar(
                searchText = filtro,
                onSearchTextChange = {
                    viewModel.setFiltro(it.toUpperCase(Locale.current))
                },
                modifier = Modifier.padding(bottom = 20.dp),
                onSearch = {
                    viewModel.setFiltro(it)
                },
                onDone = {
                    viewModel.setFiltro(filtro)
                }
            )

            // Contador de resultados
            Text(
                text = "${productos.size} producto(s) encontrado(s)",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.padding(bottom = 12.dp)
            )

            if (productos.isEmpty()) {
                CustomEmptyMessage()
            } else {
                LazyColumn(
                    verticalArrangement = Arrangement.spacedBy(12.dp),
                    contentPadding = PaddingValues(bottom = 24.dp)
                ) {
                    items(productos.size) { index ->
                        val product = productos[index]
                        ProductCard(
                            producto = product.toOitmData()!!,
                            modifier = Modifier.animateItem(),
                            onNavigateToDetail = {
                                val encodedProductName = Uri.encode(it.desc)
                                val code = Uri.encode(it.codesap)

                                onNavigateToNewPrint(code, encodedProductName)
//                                if (isPrint) {
//                                    onNavigateToNewPrint(code, encodedProductName)
//                                } else {
//                                    onNavigateToFillPrint(code, encodedProductName)
//                                }
                            }
                        )
                    }
                }
            }
        }
    }


}