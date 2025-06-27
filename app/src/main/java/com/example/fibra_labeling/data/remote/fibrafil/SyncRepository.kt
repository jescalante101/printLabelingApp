package com.example.fibra_labeling.data.remote.fibrafil

import com.example.fibra_labeling.data.model.fibrafil.OncInsertResponse

interface SyncRepository {
    //Get
    suspend fun syncUsers()
    suspend fun syncOitms()

    //Send
    suspend fun syncEtiquetaDetalle()

    suspend fun sycOinc(docEntry: Long):OncInsertResponse

    suspend fun syncAlmacen()
}