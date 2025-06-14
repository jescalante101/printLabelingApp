package com.example.fibra_labeling.data.local.repository.fibrafil.fibinc

import com.example.fibra_labeling.data.local.dao.FibIncDao
import com.example.fibra_labeling.data.local.entity.fibrafil.FibIncEntity

class FibIncRepositoryImpl(private val dao: FibIncDao): FibIncRepository {

    override suspend fun insert(entity: FibIncEntity) = dao.insert(entity)

    override suspend fun insertAll(items: List<FibIncEntity>) =dao.insertAll(items)

    override suspend fun getAll(): List<FibIncEntity> =dao.getAll()

    override suspend fun getById(id: Int): FibIncEntity? =dao.getById(id)
    override suspend fun getNotSynced(): List<FibIncEntity> =dao.getNotSynced()

    override suspend fun update(entity: FibIncEntity) =dao.update(entity)

    override suspend fun delete(entity: FibIncEntity) =dao.delete(entity)

    override suspend fun deleteAll() =dao.deleteAll()
}