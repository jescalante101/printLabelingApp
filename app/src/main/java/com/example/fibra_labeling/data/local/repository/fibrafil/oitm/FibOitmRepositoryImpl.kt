package com.example.fibra_labeling.data.local.repository.fibrafil.oitm

import com.example.fibra_labeling.data.local.dao.FibOitmDao
import com.example.fibra_labeling.data.local.entity.fibrafil.FibOITMEntity
import kotlinx.coroutines.flow.Flow

class FibOITMRepositoryImpl(
    private val dao: FibOitmDao
) : FibOitmRepository {

    override suspend fun insertAll(items: List<FibOITMEntity>) = dao.insertAll(items)

    override suspend fun insert(entity: FibOITMEntity) = dao.insert(entity)

    override suspend fun getAll(): List<FibOITMEntity> = dao.getAll()

    override suspend fun getByCode(codesap: String): FibOITMEntity? = dao.getByCode(codesap)

    override suspend fun search(first: String?, second: String?, tercero: String?): Flow<List<FibOITMEntity>> = dao.searchFlow(first,second,tercero)

    override suspend fun delete(entity: FibOITMEntity) = dao.delete(entity)

    override suspend fun deleteAll() = dao.deleteAll()
}
