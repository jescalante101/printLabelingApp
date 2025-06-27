package com.example.fibra_labeling.data.remote.fibraprint

import com.example.fibra_labeling.data.local.dao.fibraprint.PrintOcrdDao
import com.example.fibra_labeling.data.local.dao.fibraprint.PrintOitmDao
import com.example.fibra_labeling.data.local.dao.fibraprint.PrintOusrDao
import com.example.fibra_labeling.data.local.dao.fibraprint.PrintOwhsDao
import com.example.fibra_labeling.data.local.mapper.fibraprint.toEntity

import com.example.fibra_labeling.data.model.fibrafil.OncInsertResponse
import com.example.fibra_labeling.data.network.fibraprint.PrintApiService

class PSyncRepositoryImpl(
    private val api: PrintApiService,
    private val printOitmDao: PrintOitmDao,
    private val ocrdDao: PrintOcrdDao,
    private val pOwhDao: PrintOwhsDao,
    private val pOusrDao: PrintOusrDao
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
        TODO("Not yet implemented")
    }

    override suspend fun sycOinc(docEntry: Long): OncInsertResponse {
        TODO("Not yet implemented")
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