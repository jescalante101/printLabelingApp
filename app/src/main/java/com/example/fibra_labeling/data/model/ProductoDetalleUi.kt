package com.example.fibra_labeling.data.model

import com.google.gson.annotations.SerializedName
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ProductoDetalleUi(
    @SerialName("itemCode")
    val codigo: String,
    @SerialName("itemName")
    val productoName: String,
    @SerialName("u_FIB_Ref1")
    val lote: String?,
    @SerialName("u_FIB_Ref2")
    val referencia: String?,
    @SerialName("u_FIB_MachineCode")
    val maquina: String?,
    @SerialName("u_FIB_BinLocation")
    val ubicacion: String?,
    @SerialName("codeBars")
    val codBar: String,
    @SerializedName("whsCode")
    val whsCode: String="CH3-RE"
)
