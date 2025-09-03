import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map

// --- 1. Modelos de Datos y Lógica (ViewModel) ---

// Estados para la navegación
enum class Screen {
SUPPLIER_LIST,
ORDER_LIST,
ITEM_LIST
}

// Modelos de datos para las nuevas pantallas
data class Supplier(val id: String, val name: String, val openOrdersCount: Int)
data class PurchaseOrder(val id: String, val date: String, val itemCount: Int, val supplierId: String)

// (Modelos de la pantalla de detalle que ya teníamos)
enum class ItemStatus { PENDING, VERIFIED, DISCREPANCY }
enum class FilterType { PENDING, VERIFIED, DISCREPANCY }
data class OrderItem(
val id: String, val desc: String, val ordered: Int,
val received: MutableState<Int>, val wh: String,
val status: MutableState<ItemStatus> = mutableStateOf(ItemStatus.PENDING)
)

class ReceptionViewModel : ViewModel() {
// --- STATE FOR NAVIGATION ---
private val _currentScreen = MutableStateFlow(Screen.SUPPLIER_LIST)
val currentScreen = _currentScreen.asStateFlow()

    // --- STATE FOR SUPPLIER LIST ---
    private val _suppliers = MutableStateFlow<List<Supplier>>(emptyList())
    val suppliers = _suppliers.asStateFlow()
    var selectedSupplier = mutableStateOf<Supplier?>(null)
        private set

    // --- STATE FOR ORDER LIST ---
    private val _orders = MutableStateFlow<List<PurchaseOrder>>(emptyList())
    val ordersForSelectedSupplier = combine(selectedSupplier, _orders) { supplier, allOrders ->
        if (supplier == null) emptyList()
        else allOrders.filter { it.supplierId == supplier.id }
    }
    var selectedOrder = mutableStateOf<PurchaseOrder?>(null)
        private set

    // --- STATE FOR ITEM DETAIL LIST ---
    private val _items = MutableStateFlow<List<OrderItem>>(emptyList())
    private val _searchQuery = MutableStateFlow("")
    private val _activeFilter = MutableStateFlow(FilterType.PENDING)
    val searchQuery = _searchQuery.asStateFlow()
    val activeFilter = _activeFilter.asStateFlow()
    val filteredItems = combine(_items, _searchQuery, _activeFilter) { items, query, filter ->
        items.filter { item ->
            val matchesSearch = item.id.contains(query, ignoreCase = true) || item.desc.contains(query, ignoreCase = true)
            val matchesFilter = when(filter) {
                FilterType.PENDING -> item.status.value == ItemStatus.PENDING
                FilterType.VERIFIED -> item.status.value == ItemStatus.VERIFIED
                FilterType.DISCREPANCY -> item.status.value == ItemStatus.DISCREPANCY
            }
            matchesSearch && matchesFilter
        }
    }
    val totalCount = _items.map { it.size }
    val checkedCount = _items.map { items -> items.count { it.status.value != ItemStatus.PENDING } }
    val discrepancyCount = _items.map { items -> items.count { it.status.value == ItemStatus.DISCREPANCY } }


    init {
        // Cargar datos de ejemplo al iniciar
        _suppliers.value = getSampleSuppliers()
        _orders.value = getSampleOrders()
    }

    // --- NAVIGATION ACTIONS ---
    fun selectSupplier(supplier: Supplier) {
        selectedSupplier.value = supplier
        _currentScreen.value = Screen.ORDER_LIST
    }

    fun selectOrder(order: PurchaseOrder) {
        selectedOrder.value = order
        _items.value = getSampleItemsForOrder(order.id) // Cargar artículos para la orden
        _currentScreen.value = Screen.ITEM_LIST
    }

    fun navigateBack() {
        when (_currentScreen.value) {
            Screen.ORDER_LIST -> {
                _currentScreen.value = Screen.SUPPLIER_LIST
                selectedSupplier.value = null
            }
            Screen.ITEM_LIST -> {
                _currentScreen.value = Screen.ORDER_LIST
                selectedOrder.value = null
                _items.value = emptyList() // Limpiar items
            }
            else -> {} // No action
        }
    }
    
    // --- ITEM DETAIL ACTIONS ---
    fun onSearchQueryChange(query: String) { _searchQuery.value = query }
    fun onFilterChange(filter: FilterType) { _activeFilter.value = filter }
    fun updateQuantity(itemId: String, newQuantity: Int) { /* ... */ }
    fun verifyItem(itemId: String) { /* ... */ }


    // --- MOCK DATA ---
    private fun getSampleSuppliers(): List<Supplier> = listOf(
        Supplier("S001", "Proveedor Global S.A.", 3),
        Supplier("S002", "Importaciones Tech", 1)
    )
    private fun getSampleOrders(): List<PurchaseOrder> = listOf(
        PurchaseOrder("OC-789", "01/09/2025", 7, "S001"),
        PurchaseOrder("OC-790", "02/09/2025", 2, "S001"),
        PurchaseOrder("OC-791", "03/09/2025", 15, "S001"),
        PurchaseOrder("OC-801", "04/09/2025", 5, "S002")
    )
    private fun getSampleItemsForOrder(orderId: String): List<OrderItem> = listOf(
         OrderItem(id = "A001", desc = "Teclado Inalámbrico Pro", ordered = 50, received = mutableStateOf(50), wh = "01-Gral"),
         OrderItem(id = "B015", desc = "Monitor Curvo 24 pulgadas", ordered = 20, received = mutableStateOf(18), wh = "02-Comp"),
         OrderItem(id = "C011", desc = "Webcam Full HD 1080p", ordered = 30, received = mutableStateOf(30), wh = "01-Gral")
    )
}


// --- 2. COMPOSABLE PRINCIPAL (NAVEGADOR) ---

@Composable
fun ReceptionFlowScreen(viewModel: ReceptionViewModel = viewModel()) {
val currentScreen by viewModel.currentScreen.collectAsState()

    Box(Modifier.fillMaxSize()) {
        AnimatedVisibility(
            visible = currentScreen == Screen.SUPPLIER_LIST,
            enter = fadeIn(),
            exit = slideOutHorizontally(targetOffsetX = { -it / 3 }) + fadeOut()
        ) {
            SupplierListScreen(viewModel = viewModel)
        }

        AnimatedVisibility(
            visible = currentScreen == Screen.ORDER_LIST,
            enter = slideInHorizontally(initialOffsetX = { it }) + fadeIn(),
            exit = slideOutHorizontally(targetOffsetX = { -it / 3 }) + fadeOut()
        ) {
            OrderListScreen(viewModel = viewModel)
        }
        
         AnimatedVisibility(
            visible = currentScreen == Screen.ITEM_LIST,
            enter = slideInHorizontally(initialOffsetX = { it }) + fadeIn(),
            exit = fadeOut() // La última pantalla no necesita slide out
        ) {
            ItemDetailScreen(viewModel = viewModel)
        }
    }
}


// --- 3. COMPOSABLES PARA CADA PANTALLA ---

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SupplierListScreen(viewModel: ReceptionViewModel) {
val suppliers by viewModel.suppliers.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Proveedores Pendientes") },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    titleContentColor = MaterialTheme.colorScheme.onPrimary
                )
            )
        }
    ) { padding ->
        LazyColumn(
            modifier = Modifier.padding(padding),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            item {
                 OutlinedTextField(
                    value = "", onValueChange = {},
                    modifier = Modifier.fillMaxWidth(),
                    placeholder = { Text("Buscar proveedor...") },
                    leadingIcon = { Icon(Icons.Default.Search, "Search") },
                    shape = RoundedCornerShape(12.dp)
                )
            }
            items(suppliers) { supplier ->
                SupplierCard(supplier = supplier, onClick = { viewModel.selectSupplier(supplier) })
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
Text(supplier.name, fontWeight = FontWeight.Bold, style = MaterialTheme.typography.titleMedium)
Text("${supplier.openOrdersCount} Órdenes Abiertas", style = MaterialTheme.typography.bodyMedium, color = Color.Gray)
}
Icon(Icons.Default.ChevronRight, contentDescription = "Ver órdenes", tint = Color.Gray)
}
}
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun OrderListScreen(viewModel: ReceptionViewModel) {
val supplier by remember { viewModel.selectedSupplier }
val orders by viewModel.ordersForSelectedSupplier.collectAsState(initial = emptyList())

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(supplier?.name ?: "Órdenes de Compra") },
                navigationIcon = {
                    IconButton(onClick = { viewModel.navigateBack() }) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Volver a proveedores")
                    }
                },
                 colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    titleContentColor = MaterialTheme.colorScheme.onPrimary,
                    navigationIconContentColor = MaterialTheme.colorScheme.onPrimary
                )
            )
        }
    ) { padding ->
        LazyColumn(
            modifier = Modifier.padding(padding),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(orders) { order ->
                OrderCard(order = order, onClick = { viewModel.selectOrder(order) })
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
Text("Pedido #${order.id}", fontWeight = FontWeight.Bold, style = MaterialTheme.typography.titleMedium)
Text("Fecha: ${order.date} - ${order.itemCount} artículos", style = MaterialTheme.typography.bodyMedium, color = Color.Gray)
}
Icon(Icons.Default.ChevronRight, contentDescription = "Ver detalle", tint = Color.Gray)
}
}
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ItemDetailScreen(viewModel: ReceptionViewModel) {
// Este es el código de la pantalla de detalle que ya teníamos,
// ahora integrado en el flujo.
val order by remember { viewModel.selectedOrder }
val filteredItems by viewModel.filteredItems.collectAsState(initial = emptyList())
val searchQuery by viewModel.searchQuery.collectAsState()
val activeFilter by viewModel.activeFilter.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Pedido #${order?.id ?: ""}") },
                navigationIcon = {
                    IconButton(onClick = { viewModel.navigateBack() }) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Volver a órdenes")
                    }
                },
                 colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    titleContentColor = MaterialTheme.colorScheme.onPrimary,
                    navigationIconContentColor = MaterialTheme.colorScheme.onPrimary
                )
            )
        },
        bottomBar = { /* BottomActionBar() */ }
    ) { paddingValues ->
        Column(Modifier.padding(paddingValues)) {
            // SummaryBar(viewModel)
            // SearchAndFilter(...)
            // ItemList(...)
        }
    }
}


// --- 4. VISTA PREVIA ---
@Preview(showBackground = true, widthDp = 360, heightDp = 720)
@Composable
fun ReceptionFlowPreview() {
AppTheme {
ReceptionFlowScreen()
}
}

