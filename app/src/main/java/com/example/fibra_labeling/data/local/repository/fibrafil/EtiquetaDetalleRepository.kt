package com.example.fibra_labeling.data.local.repository.fibrafil

import com.example.fibra_labeling.data.local.entity.fibrafil.EtiquetaDetalleEntity

interface EtiquetaDetalleRepository {
    suspend fun getAll(): List<EtiquetaDetalleEntity>
    suspend fun insert(etiqueta: EtiquetaDetalleEntity)
    suspend fun update(etiqueta: EtiquetaDetalleEntity)
    suspend fun delete(etiqueta: EtiquetaDetalleEntity)
    suspend fun getNoSynced(): List<EtiquetaDetalleEntity>
    suspend fun deleteAll()
    suspend fun getDetailsByWhsAndItemCode(whsCode: String, itemCode: String): EtiquetaDetalleEntity?

    suspend fun getNoSyncedPaged(limit: Int, offset: Int): List<EtiquetaDetalleEntity>
}
