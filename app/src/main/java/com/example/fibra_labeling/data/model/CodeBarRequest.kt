package com.example.fibra_labeling.data.model

import kotlinx.serialization.Serializable

@Serializable
data class CodeBarRequest(
    val codeBar: String,
    val ipPrinter:String,
    val portPrinter: Int=9100
)