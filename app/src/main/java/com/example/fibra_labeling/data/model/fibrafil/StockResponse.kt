package com.example.fibra_labeling.data.model.fibrafil


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class StockResponse(
    @SerialName("onHand")
    val onHand: Int?
)