package com.example.fibra_labeling.data.model

import com.example.fibra_labeling.data.local.entity.fibraprint.PesajeEntity
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ImobPesaje(
    @SerialName("docEntry") // Añadido para mapear "DocEntry"
    val docEntry: Int?=0, // El valor es un número, así que Int es apropiado

    @SerialName("itemCode")
    val itemCode: String,

    @SerialName("name") // JSON usa "name", tu propiedad es "name" (coincide)
    val name: String,

    @SerialName("proveedor")
    val proveedor: String,

    @SerialName("lote")
    val lote: String,

    @SerialName("almacen")
    val almacen: String,

    @SerialName("motivo")
    val motivo: String,

    @SerialName("ubicacion")
    val ubicacion: String,

    @SerialName("piso")
    val piso: String,

    @SerialName("metroLineal")
    val metroLineal: String,

    @SerialName("equivalente")
    val equivalente: String?="",

    @SerialName("peso")
    val peso: Double? = 0.0,

    @SerialName("codeBar")
    val codeBar: String,

    @SerialName("userCreate")
    val userCreate: String?="",

    @SerialName("createDate")
    val createDate: String?="",

    @SerialName("status")
    val status: String? = "",

    @SerialName("userUpdate")
    val userUpdate: String? = "",

    @SerialName("updateDate")
    val updateDate: String? = "",

    @SerialName("area")
    val area: String? =""
)


fun ImobPesaje.toEntity(): PesajeEntity {
    return PesajeEntity(
        codigo = this.itemCode,
        nombre = this.name,
        proveedor = this.proveedor,
        lote = this.lote,
        almacen = this.almacen,
        ubicacion = this.ubicacion,
        piso = this.piso,
        metroLineal = this.metroLineal,
        peso = peso ?:0.0,
        codigoBarra = this.codeBar,
        usuario = this.userCreate,
        fecha = this.createDate ?: "",
        unidad = "",
        u_area = this.area
    )
}