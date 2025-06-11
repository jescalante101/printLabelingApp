package com.example.fibra_labeling.data.model


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class PesajeResponse(

    @SerialName("result")
    val result: String="",

    @SerialName("success")
    val success: Boolean?=false,

    @SerialName("data")
    val data: ImobPasaje?=null,

    @SerialName("message")
    val message: String=""

)