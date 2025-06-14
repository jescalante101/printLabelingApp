package com.example.fibra_labeling.data.local.repository.fibrafil.fibinc

import com.example.fibra_labeling.data.local.entity.fibrafil.FibIncEntity

interface FibIncRepository {
    suspend fun insert(entity: FibIncEntity): Long

    suspend fun insertAll(items: List<FibIncEntity>): List<Long>

    suspend fun getAll(): List<FibIncEntity>

    suspend fun getById(id: Int): FibIncEntity?

    suspend fun getNotSynced(): List<FibIncEntity>

    suspend fun update(entity: FibIncEntity)

    suspend fun delete(entity: FibIncEntity)

    suspend fun deleteAll()
}