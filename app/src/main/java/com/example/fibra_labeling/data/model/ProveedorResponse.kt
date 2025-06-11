package com.example.fibra_labeling.data.model


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ProveedorResponse(
    @SerialName("cardName")
    val cardName: String?
)