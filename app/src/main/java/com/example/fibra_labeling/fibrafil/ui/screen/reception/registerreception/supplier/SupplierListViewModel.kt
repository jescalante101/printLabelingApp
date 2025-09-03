package com.example.fibra_labeling.fibrafil.ui.screen.reception.registerreception.supplier

import androidx.lifecycle.ViewModel
import com.example.fibra_labeling.fibrafil.ui.screen.reception.registerreception.model.Supplier
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class SupplierListViewModel : ViewModel() {

    private val _suppliers = MutableStateFlow<List<Supplier>>(emptyList())
    val suppliers = _suppliers.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading = _isLoading.asStateFlow()

    init {
        loadSuppliers()
    }

    private fun loadSuppliers() {
        _isLoading.value = true
        // Simular carga de datos
        _suppliers.value = getSampleSuppliers()
        _isLoading.value = false
    }

    private fun getSampleSuppliers(): List<Supplier> = listOf(
        Supplier("S001", "Proveedor Global S.A.", 3),
        Supplier("S002", "Importaciones Tech", 1),
        Supplier("S003", "Distribuidora Norte", 2),
        Supplier("S004", "Suministros Industriales", 4),
        Supplier("S005", "Comercial del Sur", 1)
    )
}