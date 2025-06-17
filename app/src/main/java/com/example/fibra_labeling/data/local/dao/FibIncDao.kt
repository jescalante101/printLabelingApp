package com.example.fibra_labeling.data.local.dao

import androidx.room.*
import com.example.fibra_labeling.data.local.entity.fibrafil.FibIncEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface FibIncDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(entity: FibIncEntity): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(items: List<FibIncEntity>): List<Long>

    @Query("SELECT * FROM fib_inc")
    fun getAll(): Flow<List<FibIncEntity>>

    @Query("SELECT * FROM fib_inc WHERE id = :id")
    suspend fun getById(id: Int): FibIncEntity?

    @Query("SELECT * FROM fib_inc WHERE isSynced = 0")
    suspend fun getNotSynced(): List<FibIncEntity>

    @Query("""
    SELECT * FROM fib_inc 
    WHERE docEntry = :docEntry
      AND (U_ItemName LIKE '%' || :filter || '%' OR U_ItemCode LIKE '%' || :filter || '%')
    ORDER BY U_ItemCode
""")
    fun getByDocEntry(
        docEntry: Int,
        filter: String
    ): Flow<List<FibIncEntity>>

    @Update
    suspend fun update(entity: FibIncEntity)

    @Delete
    suspend fun delete(entity: FibIncEntity)

    @Query("DELETE FROM fib_inc")
    suspend fun deleteAll()

    @Query("UPDATE fib_inc SET isSynced = 1  WHERE docEntry = :docEntry")
    suspend fun markIncAsSyncedByDocEntry(docEntry: Long)

}
