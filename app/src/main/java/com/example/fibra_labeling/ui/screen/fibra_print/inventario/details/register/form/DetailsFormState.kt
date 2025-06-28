package com.example.fibra_labeling.ui.screen.fibra_print.inventario.details.register.form

import androidx.compose.runtime.Immutable
import com.example.fibra_labeling.data.local.entity.fibraprint.POcrdEntity
import com.example.fibra_labeling.data.local.entity.fibraprint.POwhsEntity
import com.example.fibra_labeling.data.local.entity.fibraprint.PesajeEntity

@Immutable
data class DetailsFormState(
    val codigo: String = "",
    val nombreProducto: String = "",
    val proveedor: POcrdEntity? = null,
    val almacen: POwhsEntity? =null ,
    val observacion: String = "",
    val conteo: String = "0",
    val ubicacion: String = "",
    val codeBar: String ="",
    val pesaje: PesajeEntity? =null
)