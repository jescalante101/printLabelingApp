package com.example.fibra_labeling.data.model.fibrafil.oinc


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class IncApiResponse(
    @SerialName("docEntry")
    val docEntry: Int?,
    @SerialName("lineId")
    val lineId: Int?,
    @SerialName("u_CountQty")
    val uCountQty: Int?,
    @SerialName("u_Difference")
    val uDifference: Int?,
    @SerialName("u_InWhsQty")
    val uInWhsQty: Int?,
    @SerialName("u_ItemCode")
    val uItemCode: String?,
    @SerialName("u_ItemName")
    val uItemName: String?,
    @SerialName("u_WhsCode")
    val uWhsCode: String?
)