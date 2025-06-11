package com.example.fibra_labeling.data.model


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class PesajeRequest(
    @SerialName("almacen")
    val almacen: String?,
    @SerialName("codigo")
    val codigo: String?,
    @SerialName("equi")
    val equi: String?,
    @SerialName("lote")
    val lote: String?,
    @SerialName("mLineal")
    val mLineal: String?,
    @SerialName("motivo")
    val motivo: String?,
    @SerialName("name")
    val name: String?,
    @SerialName("pesoBruto")
    val pesoBruto: String?,
    @SerialName("piso")
    val piso: String?,
    @SerialName("provee")
    val provee: String?,
    @SerialName("stepd")
    val stepd: String?,
    @SerialName("ubicacion")
    val ubicacion: String?,
    @SerialName("usuario")
    val usuario: String?
)