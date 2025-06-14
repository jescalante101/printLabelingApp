package com.example.fibra_labeling.data.local.repository.fibrafil.user

import com.example.fibra_labeling.data.local.entity.fibrafil.FilUserEntity

interface FUserRepository{
    suspend fun insertAll(users: List<FilUserEntity>)
    suspend fun insert(user: FilUserEntity)
    suspend fun getAll(): List<FilUserEntity>
    suspend fun getById(userid: Int): FilUserEntity?
    suspend fun searchByName(name: String): List<FilUserEntity>
    suspend fun deleteAll()


}