package com.example.fibra_labeling.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "pesaje")
data class PesajeEntity(
    @PrimaryKey(autoGenerate = true)
    val localId: Long = 0,          // ID local Room (clave primaria interna)
    val id: String,                 // ID remoto o generado (puede ser UUID, si aún no sube usar uno temporal)
    val peso: Double,
    val fecha: String,
    val codigoBarra: String,
    val almacen: String?,
    val usuario: String?,
    val proveedor: String?,
    val lote: String?,
    val motivo: String?,
    val ubicacion: String?,
    val piso: String?,
    val metroLineal: String?,
    val equivalente: String?,
    val estado: String?,
    val nombre: String?,

    // Campo local para saber si ya se sincronizó con el servidor
    val isSynced: Boolean = false,
    /*
    almacen = formState.value.almacen?.whsName,
                   codigo = formState.value.codigo,
                   equi = formState.value.equivalente,
                   lote = formState.value.lote,
                   mLineal = formState.value.metroLineal,
                   motivo = formState.value.motivo,
                   name = formState.value.name,
                   pesoBruto = formState.value.pesoBruto,
                   piso = formState.value.piso,
                   provee = formState.value.proveedor,
                   stepd = formState.value.stepd,
                   ubicacion = formState.value.ubicacion,
                   usuario = formState.value.usuario
     */
)