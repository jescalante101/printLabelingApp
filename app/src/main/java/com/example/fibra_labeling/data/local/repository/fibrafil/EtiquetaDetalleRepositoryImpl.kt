package com.example.fibra_labeling.data.local.repository.fibrafil

import com.example.fibra_labeling.data.local.dao.EtiquetaDetalleDao
import com.example.fibra_labeling.data.local.entity.fibrafil.EtiquetaDetalleEntity

class EtiquetaDetalleRepositoryImpl(
    private val dao: EtiquetaDetalleDao
) : EtiquetaDetalleRepository {
    override suspend fun getAll() = dao.getAll()
    override suspend fun insert(etiqueta: EtiquetaDetalleEntity) = dao.insert(etiqueta)
    override suspend fun update(etiqueta: EtiquetaDetalleEntity) = dao.update(etiqueta)
    override suspend fun delete(etiqueta: EtiquetaDetalleEntity) = dao.delete(etiqueta)
    override suspend fun getNoSynced() = dao.getNoSynced()
    override suspend fun deleteAll() = dao.deleteAll()
    override suspend fun getDetailsByWhsAndItemCode(
        whsCode: String,
        itemCode: String
    ): EtiquetaDetalleEntity? =dao.getDetailsByWhsAndItemCode(whsCode, itemCode)

    override suspend fun getNoSyncedPaged(
        limit: Int,
        offset: Int
    ): List<EtiquetaDetalleEntity> =dao.getNoSyncedPaged(limit, offset)


}
