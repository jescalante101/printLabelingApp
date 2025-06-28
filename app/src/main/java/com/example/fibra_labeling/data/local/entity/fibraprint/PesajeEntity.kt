package com.example.fibra_labeling.data.local.entity.fibraprint

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "p_fib_pesaje")
data class PesajeEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,// ID local Room (clave primaria interna)
    val peso: Double,
    val fecha: String,
    val codigoBarra: String,
    val almacen: String?,
    val usuario: String?,
    val proveedor: String?,
    val lote: String?,
    val ubicacion: String?,
    val piso: String?,
    val metroLineal: String?,
    val nombre: String?,
    val u_area: String?,
    val codigo: String?,

    // Campo local para saber si ya se sincronizó con el servidor
    val isSynced: Boolean = false,

)