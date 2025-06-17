package com.example.fibra_labeling.data.local.repository.fibrafil.fibinc

import com.example.fibra_labeling.data.local.entity.fibrafil.FibIncEntity
import kotlinx.coroutines.flow.Flow

interface FibIncRepository {
    suspend fun insert(entity: FibIncEntity): Long

    suspend fun insertAll(items: List<FibIncEntity>): List<Long>

    fun getAll(): Flow<List<FibIncEntity>>

    suspend fun getById(id: Int): FibIncEntity?

    suspend fun getNotSynced(): List<FibIncEntity>

    suspend fun update(entity: FibIncEntity)

    fun getByDocEntry(docEntry: Int,filter: String): Flow<List<FibIncEntity>>

    suspend fun delete(entity: FibIncEntity)

    suspend fun deleteAll()
}