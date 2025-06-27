package com.example.fibra_labeling.data.model.fibraprint


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class OcrdResponse(
    @SerialName("cardCode")
    val cardCode: String,
    @SerialName("cardName")
    val cardName: String?
)