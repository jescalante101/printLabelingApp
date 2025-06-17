package com.example.fibra_labeling.data.model.fibrafil


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class UpdateITWResponse(
    @SerialName("itemCode")
    val itemCode: String?=null,
    @SerialName("message")
    val message: String?=null,
    @SerialName("success")
    val success: Boolean?=null,
    @SerialName("whsCode")
    val whsCode: String?=null
)