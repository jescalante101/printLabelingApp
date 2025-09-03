package com.example.fibra_labeling.fibraprint.ui.screen.inventario.details.register.form

import androidx.compose.runtime.Immutable
import com.example.fibra_labeling.data.local.entity.fibraprint.PIncEntity
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
    val conteo: String = "",
    val ubicacion: String = "",
    val codeBar: String ="",
    val pesaje: PesajeEntity? =null,
    val metroLineal: String = "",
    val unidad: String = "",
    val inc: PIncEntity?=null
)