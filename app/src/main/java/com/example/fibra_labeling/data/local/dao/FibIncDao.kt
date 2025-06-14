package com.example.fibra_labeling.data.local.dao

import androidx.room.*
import com.example.fibra_labeling.data.local.entity.fibrafil.FibIncEntity

@Dao
interface FibIncDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(entity: FibIncEntity): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(items: List<FibIncEntity>): List<Long>

    @Query("SELECT * FROM fib_inc")
    suspend fun getAll(): List<FibIncEntity>

    @Query("SELECT * FROM fib_inc WHERE id = :id")
    suspend fun getById(id: Int): FibIncEntity?

    @Query("SELECT * FROM fib_inc WHERE isSynced = 0")
    suspend fun getNotSynced(): List<FibIncEntity>

    @Update
    suspend fun update(entity: FibIncEntity)

    @Delete
    suspend fun delete(entity: FibIncEntity)

    @Query("DELETE FROM fib_inc")
    suspend fun deleteAll()
}
