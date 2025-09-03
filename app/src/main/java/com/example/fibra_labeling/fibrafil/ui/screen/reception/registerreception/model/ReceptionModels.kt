package com.example.fibra_labeling.fibrafil.ui.screen.reception.registerreception.model

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf

// Estados para la navegación
enum class FilterType { PENDING, VERIFIED, DISCREPANCY }
enum class ItemStatus { PENDING, VERIFIED, DISCREPANCY }

// Modelos de datos
data class Supplier(
    val id: String,
    val name: String,
    val openOrdersCount: Int
)

data class PurchaseOrder(
    val id: String,
    val date: String,
    val itemCount: Int,
    val supplierId: String
)

data class OrderItem(
    val id: String,
    val desc: String,
    val ordered: Int,
    val received: MutableState<Int>,
    val wh: String,
    val status: MutableState<ItemStatus> = mutableStateOf(ItemStatus.PENDING)
)