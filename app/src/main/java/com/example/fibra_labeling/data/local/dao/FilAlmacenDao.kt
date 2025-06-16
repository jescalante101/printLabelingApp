package com.example.fibra_labeling.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.fibra_labeling.data.local.entity.fibrafil.FibAlmacenEntity

@Dao
interface FilAlmacenDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(items: List<FibAlmacenEntity>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(entity: FibAlmacenEntity)

    @Query("SELECT * FROM fib_almacen")
    suspend fun getAll(): List<FibAlmacenEntity>

    @Query("SELECT * FROM fib_almacen WHERE whsCode = :whsCode")
    suspend fun getByCode(whsCode: String): FibAlmacenEntity?

    // like query by name and code
    @Query("SELECT * FROM fib_almacen WHERE whsCode LIKE '%' || :query || '%' OR whsName LIKE '%' || :query || '%'")
    suspend fun search(query: String): List<FibAlmacenEntity>

    @Delete
    suspend fun delete(entity: FibAlmacenEntity)

    @Query("DELETE FROM fib_almacen")
    suspend fun deleteAll()
}