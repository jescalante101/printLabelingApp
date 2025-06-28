package com.example.fibra_labeling.data.local.dao.fibraprint

import androidx.room.*
import com.example.fibra_labeling.data.local.entity.fibraprint.PesajeEntity

@Dao
interface PesajeDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(pesaje: PesajeEntity): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(pesajes: List<PesajeEntity>)

    @Update
    suspend fun update(pesaje: PesajeEntity)

    @Delete
    suspend fun delete(pesaje: PesajeEntity)

    @Query("DELETE FROM p_fib_pesaje")
    suspend fun deleteAll()

    @Query("SELECT * FROM p_fib_pesaje")
    suspend fun getAll(): List<PesajeEntity>

    @Query("SELECT * FROM p_fib_pesaje WHERE isSynced = 0")
    suspend fun getPendingSync(): List<PesajeEntity>

    @Query("SELECT * FROM p_fib_pesaje WHERE id = :id")
    suspend fun getById(id: Long): PesajeEntity?

    @Query("SELECT * FROM p_fib_pesaje WHERE codigoBarra = :codeBar")
    suspend fun getById(codeBar: String): PesajeEntity?
}
