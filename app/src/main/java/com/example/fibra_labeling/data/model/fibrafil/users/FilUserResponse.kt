package com.example.fibra_labeling.data.model.fibrafil.users


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class FilUserResponse(
    @SerialName("u_NAME")
    val uNAME: String?,
    @SerialName("useR_CODE")
    val useRCODE: String?,
    @SerialName("userid")
    val userid: Int?
)