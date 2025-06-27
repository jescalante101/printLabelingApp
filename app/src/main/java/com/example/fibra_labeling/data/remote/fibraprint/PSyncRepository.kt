package com.example.fibra_labeling.data.remote.fibraprint

import com.example.fibra_labeling.data.model.fibrafil.OncInsertResponse

interface PSyncRepository {
    suspend fun syncUsers()
    suspend fun syncOitms()

    //Send
    suspend fun syncEtiquetaDetalle()

    suspend fun sycOinc(docEntry: Long):OncInsertResponse

    suspend fun syncAlmacen()

    suspend fun syncProveedor()

}