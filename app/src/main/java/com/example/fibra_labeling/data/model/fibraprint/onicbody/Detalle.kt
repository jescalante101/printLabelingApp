package com.example.fibra_labeling.data.model.fibraprint.onicbody


import com.example.fibra_labeling.data.local.entity.fibraprint.PIncEntity
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Detalle(
    @SerialName("docEntry")
    val docEntry: Int,
    @SerialName("lineId")
    val lineId: Int,
    @SerialName("u_Area")
    val uArea: String,
    @SerialName("u_CodeBar")
    val uCodeBar: String,
    @SerialName("u_CountQty")
    val uCountQty: Int,
    @SerialName("u_Difference")
    val uDifference: Int,
    @SerialName("u_FIB_BinLocation")
    val uFIBBinLocation: String,
    @SerialName("u_FIB_MachineCode")
    val uFIBMachineCode: String,
    @SerialName("u_FIB_Ref1")
    val uFIBRef1: String,
    @SerialName("u_FIB_Ref2")
    val uFIBRef2: String,
    @SerialName("u_InWhsQty")
    val uInWhsQty: Int,
    @SerialName("u_ItemCode")
    val uItemCode: String,
    @SerialName("u_ItemName")
    val uItemName: String,
    @SerialName("u_WhsCode")
    val uWhsCode: String
)

fun PIncEntity.toDetalle(lineId: Int = 1): Detalle {
    return Detalle(
        docEntry = this.docEntry.toInt(), // Long -> Int
        lineId = lineId, // Este campo no existe en Room, se debe proporcionar
        uArea = this.uarea ?: "", // String? -> String (manejo de null)
        uCodeBar = this.codeBar ?: "", // String? -> String
        uCountQty = this.countQty?.toInt() ?: 0, // Double? -> Int
        uDifference = this.difference?.toInt() ?: 0, // Double? -> Int
        uFIBBinLocation = this.binLocation ?: "", // String? -> String
        uFIBMachineCode = this.machineCode ?: "", // String? -> String
        uFIBRef1 = this.ref1 ?: "", // String? -> String
        uFIBRef2 = this.ref2 ?: "", // String? -> String
        uInWhsQty = this.inWhsQty?.toInt() ?: 0, // Double? -> Int
        uItemCode = this.itemCode, // String -> String
        uItemName = this.itemName, // String -> String
        uWhsCode = this.whsCode // String -> String
    )
}