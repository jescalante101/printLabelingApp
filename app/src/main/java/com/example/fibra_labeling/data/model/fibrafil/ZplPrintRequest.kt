package com.example.fibra_labeling.data.model.fibrafil


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ZplPrintRequest(
    @SerialName("ip")
    val ip: String?,
    @SerialName("port")
    val port: Int=9100,
    @SerialName("zplContent")
    val zplContent: String?
)