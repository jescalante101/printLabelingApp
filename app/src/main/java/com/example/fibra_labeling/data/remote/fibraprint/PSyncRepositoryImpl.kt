package com.example.fibra_labeling.data.remote.fibraprint

import android.util.Log
import com.example.fibra_labeling.data.local.dao.fibraprint.PesajeDao
import com.example.fibra_labeling.data.local.dao.fibraprint.PrintIncDao
import com.example.fibra_labeling.data.local.dao.fibraprint.PrintOcrdDao
import com.example.fibra_labeling.data.local.dao.fibraprint.PrintOincDao
import com.example.fibra_labeling.data.local.dao.fibraprint.PrintOitmDao
import com.example.fibra_labeling.data.local.dao.fibraprint.PrintOusrDao
import com.example.fibra_labeling.data.local.dao.fibraprint.PrintOwhsDao
import com.example.fibra_labeling.data.local.mapper.fibrafil.toApiResponse
import com.example.fibra_labeling.data.local.mapper.fibraprint.toEntity

import com.example.fibra_labeling.data.model.fibrafil.OncInsertResponse
import com.example.fibra_labeling.data.model.fibraprint.onicbody.toApiResponse
import com.example.fibra_labeling.data.model.toPesajeRequest
import com.example.fibra_labeling.data.network.fibraprint.PrintApiService
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class PSyncRepositoryImpl(
    private val api: PrintApiService,
    private val printOitmDao: PrintOitmDao,
    private val ocrdDao: PrintOcrdDao,
    private val pOwhDao: PrintOwhsDao,
    private val pOusrDao: PrintOusrDao,
    private val printOincDao: PrintOincDao,
    private val printincDao: PrintIncDao,
    private val pesajeDao: PesajeDao
): PSyncRepository {
    override suspend fun syncUsers() {
        try {
            val users = api.getUsers()
            if (users.isNotEmpty()) pOusrDao.eliminarTodos()
            pOusrDao.insertarLista(users.mapNotNull { it.toEntity() })
        }catch (e:Exception){
            e.printStackTrace()
            throw e
        }
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
        try {
            val pesajes = pesajeDao.getPendingSync()
            if (pesajes.isEmpty()) return

            val batchSize = 20
            val batches = pesajes.chunked(batchSize)

            for (batch in batches) {
                val response = api.sendPesajeEnBloque(batch.map { it.toPesajeRequest() })

                if (response.success == true) {
                    pesajeDao.updateSyncStatus(batch.map { it.id })
                } else {
                     throw Exception("Error al sincronizar lote: ${response.message}")
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
            throw e
        }
    }
    override suspend fun sycOinc(docEntry: Long): OncInsertResponse {
        try {
            val oincWithDetalles = printOincDao.getOincWithDetailsByDocEntry(docEntry)
                ?.toApiResponse()
            Log.e("Error syncOincWithDocEntry ",oincWithDetalles.toString())
            if (oincWithDetalles==null){
                return OncInsertResponse(
                    data = null,
                    message = "No se encontraron datos para sincronizar",
                    success = false
                )
            }
            val response = api.sendOincWithDetails(oincWithDetalles)
            Log.e("Error syncOincWithDocEntry ",response.toString())
            if (response.success!!) {
                val fechaSync = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()).format(
                    Date())
                printOincDao.actualizarPorDocEntry(docEntry, response.data?.docNum!!,fechaSync)
                printincDao.updateIsSync(docEntry.toInt())
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
            val almacen = api.getAlmacens()
            if (almacen.isNotEmpty()) pOwhDao.eliminarTodos()
            pOwhDao.insertAllPowsh( almacen.map { it.toEntity() })

        }catch (e:Exception){
            e.printStackTrace()
            throw e
        }
    }

    override suspend fun syncProveedor() {
        try {
            val proveedor = api.getProveedor()
            if (proveedor.isNotEmpty()) ocrdDao.eliminarTodos()
            ocrdDao.insertarLista(proveedor.map { it.toEntity() })

        }catch (e:Exception){
            e.printStackTrace()
            throw e
        }
    }
}