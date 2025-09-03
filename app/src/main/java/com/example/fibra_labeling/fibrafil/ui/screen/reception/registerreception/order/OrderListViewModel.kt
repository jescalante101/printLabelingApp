package com.example.fibra_labeling.fibrafil.ui.screen.reception.registerreception.order

import androidx.lifecycle.ViewModel
import com.example.fibra_labeling.fibrafil.ui.screen.reception.registerreception.model.PurchaseOrder
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class OrderListViewModel : ViewModel() {

    private val _orders = MutableStateFlow<List<PurchaseOrder>>(emptyList())
    val orders = _orders.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading = _isLoading.asStateFlow()

    fun loadOrdersForSupplier(supplierId: String) {
        _isLoading.value = true
        // Simular carga de datos
        _orders.value = getSampleOrdersForSupplier(supplierId)
        _isLoading.value = false
    }

    private fun getSampleOrdersForSupplier(supplierId: String): List<PurchaseOrder> = 
        getSampleOrders().filter { it.supplierId == supplierId }

    private fun getSampleOrders(): List<PurchaseOrder> = listOf(
        PurchaseOrder("OC-789", "01/09/2025", 7, "S001"),
        PurchaseOrder("OC-790", "02/09/2025", 2, "S001"),
        PurchaseOrder("OC-791", "03/09/2025", 15, "S001"),
        PurchaseOrder("OC-801", "04/09/2025", 5, "S002"),
        PurchaseOrder("OC-802", "05/09/2025", 8, "S003"),
        PurchaseOrder("OC-803", "06/09/2025", 3, "S003"),
        PurchaseOrder("OC-804", "07/09/2025", 12, "S004"),
        PurchaseOrder("OC-805", "08/09/2025", 6, "S005")
    )
}