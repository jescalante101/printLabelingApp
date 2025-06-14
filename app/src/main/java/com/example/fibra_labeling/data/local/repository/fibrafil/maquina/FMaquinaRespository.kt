package com.example.fibra_labeling.data.local.repository.fibrafil.maquina

import com.example.fibra_labeling.data.local.entity.fibrafil.FMaquinaEntity
import kotlinx.coroutines.flow.Flow

interface FMaquinaRepository {
    suspend fun insertAll(maquinas: List<FMaquinaEntity>)
    suspend fun getAllFlow(): Flow<List<FMaquinaEntity>>
    suspend fun searchByNameAndCode(name: String?, code: String?): Flow<List<FMaquinaEntity>>
    suspend fun syncMaquinas()
}