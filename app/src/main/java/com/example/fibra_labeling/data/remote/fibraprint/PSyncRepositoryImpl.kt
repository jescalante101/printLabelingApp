package com.example.fibra_labeling.data.remote.fibraprint

import com.example.fibra_labeling.data.local.dao.fibraprint.PrintOitmDao
import com.example.fibra_labeling.data.local.mapper.fibraprint.toEntity

import com.example.fibra_labeling.data.model.fibrafil.OncInsertResponse
import com.example.fibra_labeling.data.network.fibraprint.PrintApiService

class PSyncRepositoryImpl(
    private val api: PrintApiService,
    private val printOitmDao: PrintOitmDao
): PSyncRepository {
    override suspend fun syncUsers() {
        TODO("Not yet implemented")
    }

    override suspend fun syncOitms() {
        try {
            val oitms = api.getOitms()

            if (oitms.isNotEmpty()) printOitmDao.clearAll()
            printOitmDao.insertAll(oitms.mapNotNull { it.toEntity() })
        }catch (e:Exception){
            e.printStackTrace()
            throw e
        }
    }

    override suspend fun syncEtiquetaDetalle() {
        TODO("Not yet implemented")
    }

    override suspend fun sycOinc(docEntry: Long): OncInsertResponse {
        TODO("Not yet implemented")
    }

    override suspend fun syncAlmacen() {
        TODO("Not yet implemented")
    }
}