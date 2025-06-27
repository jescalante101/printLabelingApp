package com.example.fibra_labeling.data.local.repository.fibrafil.almacen

import com.example.fibra_labeling.data.local.dao.fibrafil.FilAlmacenDao
import com.example.fibra_labeling.data.local.entity.fibrafil.FibAlmacenEntity

class FibAlmacenRepositoryImpl(
    private val dao: FilAlmacenDao
) : FibAlmacenRepository {

    override suspend fun insertAll(items: List<FibAlmacenEntity>) = dao.insertAll(items)

    override suspend fun insert(entity: FibAlmacenEntity) = dao.insert(entity)

    override suspend fun getAll(): List<FibAlmacenEntity> = dao.getAll()

    override suspend fun getByCode(whsCode: String): FibAlmacenEntity? = dao.getByCode(whsCode)

    override suspend fun search(query: String): List<FibAlmacenEntity> = dao.search(query)

    override suspend fun delete(entity: FibAlmacenEntity) = dao.delete(entity)

    override suspend fun deleteAll() = dao.deleteAll()
}
