package com.example.fibra_labeling.data.local.dao

import androidx.room.*
import com.example.fibra_labeling.data.local.entity.fibrafil.FibOincEntity

@Dao
interface FibOincDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(oinc: FibOincEntity): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(items: List<FibOincEntity>): List<Long>

    // Aquí está el cambio: ahora es Flow, no suspend
    @Query("SELECT * FROM fib_oinc")
    fun getAllFlow(): kotlinx.coroutines.flow.Flow<List<FibOincEntity>>

    @Query("SELECT * FROM fib_oinc WHERE id = :id")
    suspend fun getById(id: Long): FibOincEntity?

    @Query("SELECT * FROM fib_oinc WHERE isSynced = 0")
    fun getNotSynced(): List<FibOincEntity>

    @Update
    suspend fun update(oinc: FibOincEntity)

    @Delete
    suspend fun delete(oinc: FibOincEntity)

    @Query("DELETE FROM fib_oinc")
    suspend fun deleteAll()
}
