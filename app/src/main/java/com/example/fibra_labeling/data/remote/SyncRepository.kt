package com.example.fibra_labeling.data.remote

interface SyncRepository {
    //Get
    suspend fun syncUsers()
    suspend fun syncOitms()

    //Send
    suspend fun syncEtiquetaDetalle()
}