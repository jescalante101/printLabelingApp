package com.example.fibra_labeling.data.local.repository.fibrafil.almacen

import com.example.fibra_labeling.data.local.entity.fibrafil.FibAlmacenEntity

interface FibAlmacenRepository {
    suspend fun insertAll(items: List<FibAlmacenEntity>)
    suspend fun insert(entity: FibAlmacenEntity)
    suspend fun getAll(): List<FibAlmacenEntity>
    suspend fun getByCode(whsCode: String): FibAlmacenEntity?
    suspend fun search(query: String): List<FibAlmacenEntity>
    suspend fun delete(entity: FibAlmacenEntity)
    suspend fun deleteAll()
}
