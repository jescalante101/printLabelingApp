package com.example.fibra_labeling.data.remote

import com.example.fibra_labeling.data.local.dao.FilUserDao
import com.example.fibra_labeling.data.local.mapper.toEntity
import com.example.fibra_labeling.data.network.ApiService

class SyncRepositoryImpl(private val api: ApiService,private val dao: FilUserDao): SyncRepository {
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
}