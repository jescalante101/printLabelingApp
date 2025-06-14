package com.example.fibra_labeling.data.model.fibrafil


import com.example.fibra_labeling.data.model.ProductoDetalleUi
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class FillPrintRequest(
    @SerialName("data")
    val `data`: ProductoDetalleUi?,
    @SerialName("ipPrinter")
    val ipPrinter: String?,
    @SerialName("portPrinter")
    val portPrinter: Int?
)