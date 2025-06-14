package com.example.fibra_labeling.data.local.repository.fibrafil.user

import com.example.fibra_labeling.data.local.dao.FilUserDao
import com.example.fibra_labeling.data.local.entity.fibrafil.FilUserEntity

class FUserRepositoryImpl(private val dao: FilUserDao): FUserRepository {
    override suspend fun insertAll(users: List<FilUserEntity>) {
       dao.insertAll(users)
    }
    override suspend fun insert(user: FilUserEntity) {
        dao.insert(user)
    }
    override suspend fun getAll(): List<FilUserEntity> = dao.getAll()
    override suspend fun getById(userid: Int): FilUserEntity? =dao.getById(userid)
    override suspend fun searchByName(name: String): List<FilUserEntity> =dao.searchByName(name)

    override suspend fun deleteAll()= dao.deleteAll()
}