package com.example.fibra_labeling.data.model


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class PrintResponse(
    @SerialName("data")
    val `data`: ImobPesaje?=null,
    @SerialName("message")
    val message: String?,
    @SerialName("success")
    val success: Boolean?
)