package com.example.fibra_labeling.data.model


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class MaquinasResponse(
    @SerialName("data")
    val `data`: List<MaquinaData?>?,
    @SerialName("page")
    val page: Int?,
    @SerialName("pageSize")
    val pageSize: Int?,
    @SerialName("total")
    val total: Int?
)