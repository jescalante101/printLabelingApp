package com.example.fibra_labeling.fibrafil.ui.screen.reception.registerreception.order

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.fibra_labeling.R
import com.example.fibra_labeling.fibrafil.ui.screen.reception.registerreception.model.PurchaseOrder
import com.example.fibra_labeling.ui.theme.FioriBackground
import org.koin.androidx.compose.koinViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun OrderListScreen(
    supplierId: String,
    onNavigateBack: () -> Unit,
    onNavigateToItemDetails: (String) -> Unit,
    viewModel: OrderListViewModel = koinViewModel()
) {
    val orders by viewModel.orders.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()
    
    LaunchedEffect(supplierId) {
        viewModel.loadOrdersForSupplier(supplierId)
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Órdenes de Compra") },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(
                            Icons.Default.ArrowBack,
                            contentDescription = "Volver a proveedores",
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
            items(orders) { order ->
                OrderCard(
                    order = order,
                    onClick = {
                        onNavigateToItemDetails(order.id)
                    }
                )
            }
        }
    }
}

@Composable
fun OrderCard(order: PurchaseOrder, onClick: () -> Unit) {
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
                    "Pedido #${order.id}",
                    fontWeight = FontWeight.Bold,
                    style = MaterialTheme.typography.titleMedium
                )
                Text(
                    "Fecha: ${order.date} - ${order.itemCount} artículos",
                    style = MaterialTheme.typography.bodyMedium,
                    color = Color.Gray
                )
            }
            Icon(
                painter = painterResource(R.drawable.ic_doc),
                contentDescription = "Ver detalle",
                tint = Color.Gray
            )
        }
    }
}