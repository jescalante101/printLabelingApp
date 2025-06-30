package com.example.fibra_labeling.data.local.dao.fibraprint

import androidx.room.*
import com.example.fibra_labeling.data.local.entity.fibraprint.POITMEntity
import com.example.fibra_labeling.data.local.entity.fibraprint.PesajeEntity
import kotlinx.coroutines.flow.Flow

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

    // update sync status to true
    @Query("UPDATE p_fib_pesaje SET isSynced = 1 WHERE id in (:ids)")
    suspend fun updateSyncStatus(ids: List<Long>)

    @Query("""
    SELECT * FROM p_fib_pesaje
    WHERE peso is not null  and (codigo LIKE :filter OR nombre LIKE :filter OR codigoBarra LIKE :filter)
    """)
    fun searchPesaje(filter:String): Flow<List<PesajeEntity>>
}
