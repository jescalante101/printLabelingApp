package com.example.fibra_labeling.data.local.repository.fibrafil.oinc

import com.example.fibra_labeling.data.local.entity.fibrafil.FibOincEntity
import kotlinx.coroutines.flow.Flow

interface FibOincRepository {
    suspend fun insert(oinc: FibOincEntity): Long

    suspend fun insertAll(items: List<FibOincEntity>): List<Long>

    fun getAll(): Flow<List<FibOincEntity>>

    suspend fun getById(id: Long): FibOincEntity?

    suspend fun getNotSynced(): List<FibOincEntity>

    suspend fun update(oinc: FibOincEntity)

    suspend fun delete(oinc: FibOincEntity)

    suspend fun deleteAll()
}
