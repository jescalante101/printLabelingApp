package com.example.fibra_labeling.data.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ImobPasaje(
    @SerialName("DocEntry") // Añadido para mapear "DocEntry"
    val docEntry: Int, // El valor es un número, así que Int es apropiado

    @SerialName("ItemCode")
    val itemCode: String,

    @SerialName("name") // JSON usa "name", tu propiedad es "name" (coincide)
    val name: String,

    @SerialName("Proveedor")
    val proveedor: String,

    @SerialName("Lote")
    val lote: String,

    @SerialName("Almacen")
    val almacen: String,

    @SerialName("Motivo")
    val motivo: String,

    @SerialName("Ubicacion")
    val ubicacion: String,

    @SerialName("Piso")
    val piso: String,

    @SerialName("MetroLineal")
    val metroLineal: String,

    @SerialName("Equivalente")
    val equivalente: String,

    @SerialName("Peso")
    val peso: Double? = 0.0,

    @SerialName("CodeBar")
    val codeBar: String,

    @SerialName("UserCreate")
    val userCreate: String,

    @SerialName("CreateDate")
    val createDate: String,

    @SerialName("Status")
    val status: String? = null,

    @SerialName("UserUpdate")
    val userUpdate: String? = null,

    @SerialName("UpdateDate")
    val updateDate: String? = null
)
