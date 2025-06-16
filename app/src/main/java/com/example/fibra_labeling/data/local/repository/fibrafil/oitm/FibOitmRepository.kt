package com.example.fibra_labeling.data.local.repository.fibrafil.oitm

import com.example.fibra_labeling.data.local.entity.fibrafil.FibOITMEntity
import kotlinx.coroutines.flow.Flow

interface FibOitmRepository {
    suspend fun insertAll(items: List<FibOITMEntity>)
    suspend fun insert(entity: FibOITMEntity)
    suspend fun getAll(): List<FibOITMEntity>
    suspend fun getByCode(codesap: String): FibOITMEntity?
    suspend fun search(first: String?, second: String?): Flow<List<FibOITMEntity>>
    suspend fun delete(entity: FibOITMEntity)
    suspend fun deleteAll()
}