package com.example.fibra_labeling.data.remote

interface SyncRepository {
    suspend fun syncUsers()
    suspend fun syncOitms()
}