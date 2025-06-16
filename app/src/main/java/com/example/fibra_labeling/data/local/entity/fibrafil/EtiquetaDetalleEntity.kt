package com.example.fibra_labeling.data.local.entity.fibrafil

import android.R
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "etiqueta_detalle")
data class EtiquetaDetalleEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val itemCode: String,
    val itemName: String,
    val u_FIB_Ref1: String?,
    val u_FIB_Ref2: String?,
    val u_FIB_MachineCode: String?,
    val u_FIB_BinLocation: String?,
    val codeBars: String,
    val whsCode: String,
    val Cantidad: String?,
    val isSynced: Boolean = false
)
