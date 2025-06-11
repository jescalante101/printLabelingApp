package com.example.fibra_labeling.data.model


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class IsPrintOnlineResponse(
    @SerialName("message")
    val message: String?,
    @SerialName("online")
    val online: Boolean?
)