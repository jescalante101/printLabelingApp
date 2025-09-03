package com.example.fibra_labeling.fibrafil.ui.screen.reception.registerreception.register

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.example.fibra_labeling.fibrafil.ui.screen.reception.registerreception.model.FilterType
import com.example.fibra_labeling.fibrafil.ui.screen.reception.registerreception.model.ItemStatus
import com.example.fibra_labeling.fibrafil.ui.screen.reception.registerreception.model.OrderItem
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map

class ReceptionFibrafilViewModel(): ViewModel() {

    private val _items = MutableStateFlow<List<OrderItem>>(emptyList())
    private val _searchQuery = MutableStateFlow("")
    private val _activeFilter = MutableStateFlow(FilterType.PENDING)

    val searchQuery = _searchQuery.asStateFlow()
    val activeFilter = _activeFilter.asStateFlow()

    init {
        _items.value = getSampleData()
    }

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

    fun onSearchQueryChange(query: String) { _searchQuery.value = query }
    fun onFilterChange(filter: FilterType) { _activeFilter.value = filter }

    fun updateQuantity(itemId: String, newQuantity: Int) {
        _items.value = _items.value.map {
            if (it.id == itemId) {
                it.copy(received = mutableStateOf(newQuantity), status = mutableStateOf(ItemStatus.PENDING))
            } else it
        }
    }

    fun verifyItem(itemId: String) {
        _items.value = _items.value.map {
            if (it.id == itemId) {
                val newStatus = if (it.received.value == it.ordered) ItemStatus.VERIFIED else ItemStatus.DISCREPANCY
                it.copy(status = mutableStateOf(newStatus))
            } else it
        }
    }

    private fun getSampleData(): List<OrderItem> = listOf(
        OrderItem(id = "A001", desc = "Teclado Inalámbrico Pro", ordered = 50, received = mutableStateOf(50), wh = "01-Gral"),
        OrderItem(id = "B015", desc = "Monitor Curvo 24 pulgadas", ordered = 20, received = mutableStateOf(18), wh = "02-Comp"),
        OrderItem(id = "C011", desc = "Webcam Full HD 1080p", ordered = 30, received = mutableStateOf(30), wh = "01-Gral"),
        OrderItem(id = "A002", desc = "Mouse Óptico USB Gamer", ordered = 75, received = mutableStateOf(75), wh = "01-Gral"),
        OrderItem(id = "D405", desc = "Hub USB-C 7-en-1", ordered = 40, received = mutableStateOf(40), wh = "03-Acc"),
        OrderItem(id = "F112", desc = "Silla Ergonómica de Oficina", ordered = 15, received = mutableStateOf(15), wh = "05-Mob"),
        OrderItem(id = "B016", desc = "Brazo para Monitor VESA", ordered = 20, received = mutableStateOf(21), wh = "02-Comp")
    )
}
