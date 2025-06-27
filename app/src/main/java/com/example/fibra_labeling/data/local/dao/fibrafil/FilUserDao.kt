package com.example.fibra_labeling.data.local.dao.fibrafil

import androidx.room.*
import com.example.fibra_labeling.data.local.entity.fibrafil.FilUserEntity

@Dao
interface FilUserDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(users: List<FilUserEntity>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(user: FilUserEntity)

    @Query("SELECT * FROM fil_user")
    suspend fun getAll(): List<FilUserEntity>

    @Query("SELECT * FROM fil_user WHERE userid = :userid")
    suspend fun getById(userid: Int): FilUserEntity?

    @Query("SELECT * FROM fil_user WHERE uNAME LIKE '%' || :name || '%' OR useRCODE LIKE '%' || :name || '%'")
    suspend fun searchByName(name: String): List<FilUserEntity>

    @Query("DELETE FROM fil_user")
    suspend fun deleteAll()
}
