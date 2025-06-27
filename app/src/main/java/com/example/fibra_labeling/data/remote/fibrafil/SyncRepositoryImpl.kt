package com.example.fibra_labeling.data.remote.fibrafil

import android.util.Log
import com.example.fibra_labeling.data.local.dao.fibrafil.FibIncDao
import com.example.fibra_labeling.data.local.dao.fibrafil.FibOincDao
import com.example.fibra_labeling.data.local.dao.fibrafil.FibOitmDao
import com.example.fibra_labeling.data.local.dao.fibrafil.FilAlmacenDao
import com.example.fibra_labeling.data.local.dao.fibrafil.FilUserDao
import com.example.fibra_labeling.data.local.mapper.fibrafil.toApiResponse
import com.example.fibra_labeling.data.local.mapper.fibrafil.toEntity
import com.example.fibra_labeling.data.local.mapper.fibrafil.toProductoDetalleUi
import com.example.fibra_labeling.data.local.repository.fibrafil.EtiquetaDetalleRepository
import com.example.fibra_labeling.data.model.fibrafil.OncInsertResponse
import com.example.fibra_labeling.data.network.fibrafil.ApiService
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class SyncRepositoryImpl(
    private val api: ApiService,
    private val dao: FilUserDao,
    private val oitmDao: FibOitmDao,
    private val etiquetaDetalleRepository: EtiquetaDetalleRepository,
    private val fibOincDao: FibOincDao,
    private val fibIncDao: FibIncDao,
    private val almacenDao: FilAlmacenDao
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
            throw e
        }
    }


    override suspend fun syncOitms() {
        try {
            val oitms = api.getallOitmsFill()
            if (oitms.isNotEmpty()) oitmDao.deleteAll()
            oitmDao.insertAll(oitms.mapNotNull { it.toEntity() })
        }catch (e:Exception){
            e.printStackTrace()
            throw e
        }
    }


    override suspend fun syncEtiquetaDetalle() {
        try {
            val batchSize = 20
            var offset = 0
            while (true) {
                val etiquetaDetalleBatch = etiquetaDetalleRepository.getNoSyncedPaged(batchSize, offset)
                if (etiquetaDetalleBatch.isEmpty()) break

                val response = api.updateOitwInfo(etiquetaDetalleBatch.map { it.toProductoDetalleUi() })
                Log.e("SentETI", response.toString())

                if (response.isNotEmpty()) {
                    etiquetaDetalleBatch.forEach {
                        it.isSynced = true
                        etiquetaDetalleRepository.update(it)
                    }
                }
                offset += batchSize
            }
        } catch (e: Exception) {
            e.printStackTrace()
            Log.e("Error", e.message.toString())
            throw e
        }
    }




    override suspend fun sycOinc(docEntry: Long):OncInsertResponse {
       try {
           val oincWithDetalles = fibOincDao.getOincNotSyncedWithDetalles(docEntry).toApiResponse()
           Log.e("Error syncOincWithDocEntry ",oincWithDetalles.toString())
           val response = api.insertOinc(oincWithDetalles)
           Log.e("Error syncOincWithDocEntry ",response.toString())
           if (response.success!!) {
               val fechaSync = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()).format(
                   Date())
               fibOincDao.markOincAsSynced(docEntry, response.data?.docNum!!,fechaSync)
               fibIncDao.markIncAsSyncedByDocEntry(docEntry)
           }
           return response
       }catch (e: IOException) {
           // Captura excepciones de red (por ejemplo, sin conexión a Internet)
           Log.e("syncOinc", "Error de conexión: ${e.message}")
           return OncInsertResponse(
               data = null,
               message = e.message,
               success = false
           )
       } catch (e: Exception) {
           // Captura excepciones generales
           Log.e("syncOinc", "Error inesperado: ${e.message}")
           return OncInsertResponse(
               data = null,
               message = e.message,
               success = false
           )
       }
    }

    override suspend fun syncAlmacen() {
        try {
            val almacen=api.getAlmacenesFill()
            if (almacen.isNotEmpty()) almacenDao.deleteAll()
            almacenDao.insertAll(almacen.map { it.toEntity() })
        }catch (e: Exception){
            e.printStackTrace()
            throw e
        }
    }


}