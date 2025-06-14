package com.example.fibra_labeling.data.model.fibrafil


import com.example.fibra_labeling.data.model.fibrafil.ProductoDetalleUi
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class FilPrintResponse(
    @SerialName("data")
    val `data`: ProductoDetalleUi?=null,
    @SerialName("message")
    val message: String,
    @SerialName("success")
    val success: Boolean
)