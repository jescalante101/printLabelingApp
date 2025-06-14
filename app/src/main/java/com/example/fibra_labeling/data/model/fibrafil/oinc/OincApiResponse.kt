package com.example.fibra_labeling.data.model.fibrafil.oinc

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class OincApiResponse(
    @SerialName("docEntry")
    val docEntry: Int?,
    @SerialName("u_CountDate")
    val uCountDate: String?,
    @SerialName("u_EndTime")
    val uEndTime: String?,
    @SerialName("u_Ref")
    val uRef: String?,
    @SerialName("u_Remarks")
    val uRemarks: String?,
    @SerialName("u_StartTime")
    val uStartTime: String?
)