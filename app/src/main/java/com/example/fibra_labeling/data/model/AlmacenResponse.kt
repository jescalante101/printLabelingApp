package com.example.fibra_labeling.data.model


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class AlmacenResponse(
    @SerialName("whsCode")
    val whsCode: String,
    @SerialName("whsName")
    val whsName: String
)