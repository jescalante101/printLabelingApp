package com.example.fibra_labeling.fibrafil.ui.screen.reception.registerreception.supplier

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.fibra_labeling.R
import com.example.fibra_labeling.fibrafil.ui.screen.reception.registerreception.model.Supplier
import com.example.fibra_labeling.ui.theme.FioriBackground
import org.koin.androidx.compose.koinViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SupplierListScreen(
    onNavigateBack: () -> Unit,
    onNavigateToOrderList: (String) -> Unit,
    viewModel: SupplierListViewModel = koinViewModel()
) {
    val suppliers by viewModel.suppliers.collectAsState()
    var searchQuery by remember { mutableStateOf("") }

    val filteredSuppliers = suppliers.filter { supplier ->
        supplier.name.contains(searchQuery, ignoreCase = true)
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Proveedores Pendientes") },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(
                            Icons.Default.ArrowBack,
                            contentDescription = "Volver",
                            tint = MaterialTheme.colorScheme.primary
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color.White,
                    titleContentColor = MaterialTheme.colorScheme.primary
                )
            )
        },
        containerColor = FioriBackground
    ) { padding ->
        LazyColumn(
            modifier = Modifier.padding(padding),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            item {
                OutlinedTextField(
                    value = searchQuery,
                    onValueChange = { searchQuery = it },
                    modifier = Modifier.fillMaxWidth(),
                    placeholder = { Text("Buscar proveedor...") },
                    leadingIcon = { Icon(Icons.Default.Search, "Search") },
                    shape = RoundedCornerShape(12.dp)
                )
            }
            
            items(filteredSuppliers) { supplier ->
                SupplierCard(
                    supplier = supplier,
                    onClick = {
                        onNavigateToOrderList(supplier.id)
                    }
                )
            }
        }
    }
}

@Composable
fun SupplierCard(supplier: Supplier, onClick: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick),
        elevation = CardDefaults.cardElevation(2.dp),
        shape = RoundedCornerShape(16.dp)
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column(Modifier.weight(1f)) {
                Text(
                    supplier.name,
                    fontWeight = FontWeight.Bold,
                    style = MaterialTheme.typography.titleMedium
                )
                Text(
                    "${supplier.openOrdersCount} Órdenes Abiertas",
                    style = MaterialTheme.typography.bodyMedium,
                    color = Color.Gray
                )
            }
            Icon(
                painter = painterResource(R.drawable.ic_doc),
                contentDescription = "Ver órdenes",
                tint = Color.Gray
            )
        }
    }
}