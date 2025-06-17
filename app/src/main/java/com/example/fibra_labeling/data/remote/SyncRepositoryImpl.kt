package com.example.fibra_labeling.data.remote

import android.util.Log
import com.example.fibra_labeling.data.local.dao.FibOitmDao
import com.example.fibra_labeling.data.local.dao.FilUserDao
import com.example.fibra_labeling.data.local.mapper.toEntity
import com.example.fibra_labeling.data.local.mapper.toProductoDetalleUi
import com.example.fibra_labeling.data.local.repository.fibrafil.EtiquetaDetalleRepository
import com.example.fibra_labeling.data.network.ApiService

class SyncRepositoryImpl(
    private val api: ApiService,
    private val dao: FilUserDao,
    private val oitmDao: FibOitmDao,
    private val etiquetaDetalleRepository: EtiquetaDetalleRepository
): SyncRepository {
    override suspend fun syncUsers() {
        try {
            val users = api.getUsers()
            if (users.isNotEmpty()) dao.deleteAll()
            dao.insertAll(users.mapNotNull {
                it.toEntity()
            })
        }catch (e:Exception){
            e.printStackTrace()
        }
    }


    override suspend fun syncOitms() {
        try {
            val oitms = api.getallOitmsFill()
            if (oitms.isNotEmpty()) oitmDao.deleteAll()
            oitmDao.insertAll(oitms.mapNotNull { it.toEntity() })
        }catch (e:Exception){
            e.printStackTrace()
        }
    }


    override suspend fun syncEtiquetaDetalle() {
        try {
            val etiquetaDetalle = etiquetaDetalleRepository.getNoSynced()
            Log.e("Error",etiquetaDetalle.toString())
            if (etiquetaDetalle.isNotEmpty()){
                val response = api.updateOitwInfo(etiquetaDetalle.map { it.toProductoDetalleUi() })
                if (response.success){
                    etiquetaDetalle.forEach {
                        it.isSynced = true
                        etiquetaDetalleRepository.update(it)
                    }
                }
            }
        }catch (e:Exception){
            e.printStackTrace()
            Log.e("Error",e.message.toString())
        }
    }



}