package com.example.fibra_labeling.data.model


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class MaquinaData(
    @SerialName("code")
    val code: String?,
    @SerialName("name")
    val name: String?
)