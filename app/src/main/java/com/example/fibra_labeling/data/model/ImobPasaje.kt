package com.example.fibra_labeling.data.model

import kotlinx.serialization.Serializable

@Serializable
data class ImobPasaje(
    val itemCode: String,
    val name: String,
    val proveedor: String,
    val lote: String,
    val almacen: String,
    val motivo: String,
    val ubicacion: String,
    val piso: String,
    val metroLineal: String,
    val equivalente: String,
    val peso: Double?=0.0,
    val codeBar: String,
    val userCreate: String,
    val createDate: String,
    val status: String,
    val userUpdate: String? = null,
    val updateDate: String? = null
)
