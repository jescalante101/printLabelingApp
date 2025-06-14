package com.example.fibra_labeling.data.model.fibrafil.oinc


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class OincInsertApiResponse(
    @SerialName("data")
    val `data`: OincApiResponse?,
    @SerialName("message")
    val message: String?,
    @SerialName("success")
    val success: Boolean?
)