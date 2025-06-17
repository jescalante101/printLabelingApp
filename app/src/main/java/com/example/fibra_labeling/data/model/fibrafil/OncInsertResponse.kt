package com.example.fibra_labeling.data.model.fibrafil

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class OncInsertResponse(
    @SerialName("data")
    val `data`: Data?,
    @SerialName("message")
    val message: String?,
    @SerialName("success")
    val success: Boolean?
)

@Serializable
data class Data(
    @SerialName("docEntry")
    val docEntry: Int?,
    @SerialName("docNum")
    val docNum: String?
)