package com.example.fibra_labeling.data.local.repository.fibrafil.oinc

import com.example.fibra_labeling.data.local.dao.FibOincDao
import com.example.fibra_labeling.data.local.entity.fibrafil.FibOincEntity
import com.example.fibra_labeling.data.local.entity.fibrafil.FibOincWithDetalles
import kotlinx.coroutines.flow.Flow

class FibOincRepositoryImpl(
    private val dao: FibOincDao
): FibOincRepository{
    override suspend fun insert(oinc: FibOincEntity): Long =dao.insert(oinc)
    override suspend fun insertAll(items: List<FibOincEntity>): List<Long> =dao.insertAll(items)

    override fun getAll(filter:String): Flow<List<FibOincEntity>> = dao.getAllFlow(filter)

    override suspend fun getById(id: Long): FibOincEntity? = dao.getById(id)
    override suspend fun getNotSynced(): List<FibOincEntity> = dao.getNotSynced()

    override suspend fun update(oinc: FibOincEntity) =dao.update(oinc)

    override suspend fun delete(oinc: FibOincEntity) = dao.delete(oinc)
    override  fun getOincWithDetalles(filter: String): Flow<List<FibOincWithDetalles>> =dao.getOincWithDetalles(filter)

    override suspend fun deleteAll()=dao.deleteAll()

}