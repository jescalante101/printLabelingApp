package com.example.fibra_labeling.data.model


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class OITMData(
    @SerialName("codesap")
    val codesap: String?,
    @SerialName("desc")
    val desc: String?,
    @SerialName("unida")
    val unida: String?,

    @SerialName("codebars")
    val codebars: String?=""
)