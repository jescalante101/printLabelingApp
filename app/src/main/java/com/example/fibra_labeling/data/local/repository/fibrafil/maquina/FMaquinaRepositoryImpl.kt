package com.example.fibra_labeling.data.local.repository.fibrafil.maquina

import android.util.Log
import com.example.fibra_labeling.data.local.dao.FMaquinaDao
import com.example.fibra_labeling.data.local.entity.fibrafil.FMaquinaEntity
import com.example.fibra_labeling.data.local.mapper.toEntity
import com.example.fibra_labeling.data.network.ApiService
import kotlinx.coroutines.flow.Flow

class FMaquinaRepositoryImpl(
    private val dao: FMaquinaDao,
    private val apiService: ApiService
): FMaquinaRepository {
    override suspend fun insertAll(maquinas: List<FMaquinaEntity>) =dao.insertAll(maquinas)

    override suspend fun getAllFlow(): Flow<List<FMaquinaEntity>> =dao.getAllFlow()
    override suspend fun searchByNameAndCode(
        name: String?,
        code: String?
    ): Flow<List<FMaquinaEntity>> =dao.searchByNameAndCode(name,code)


    override suspend fun syncMaquinas() {
        try {
            val maquinas = apiService.getMaquinas("",1,500).data
            if (!maquinas.isNullOrEmpty()){
                dao.insertAll(maquinas.map { it!!.toEntity() })
            }
        }catch (e: Exception){
            // TODO: MOSTAR LOS MENSAJES DE ERROR
            Log.e("Sync", "No hay conexión: ${e.message}")
            throw e
        }

    }

    override suspend fun getByCode(code: String): FMaquinaEntity? =dao.getByCode(code)


}