package com.example.fibra_labeling.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.fibra_labeling.data.local.entity.fibrafil.EtiquetaDetalleEntity

@Dao
interface EtiquetaDetalleDao {
    @Query("SELECT * FROM etiqueta_detalle")
    suspend fun getAll(): List<EtiquetaDetalleEntity>

    @Query("SELECT * FROM etiqueta_detalle WHERE isSynced = 0")
    suspend fun getNoSynced(): List<EtiquetaDetalleEntity>

    @Query("SELECT * FROM etiqueta_detalle WHERE whsCode = :whsCode AND itemCode = :itemCode")
    suspend fun getDetailsByWhsAndItemCode(whsCode: String, itemCode: String): EtiquetaDetalleEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(etiqueta: EtiquetaDetalleEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(etiquetas: List<EtiquetaDetalleEntity>)

    @Update
    suspend fun update(etiqueta: EtiquetaDetalleEntity)

    @Delete
    suspend fun delete(etiqueta: EtiquetaDetalleEntity)

    @Query("DELETE FROM etiqueta_detalle")
    suspend fun deleteAll()


    @Query("SELECT * FROM etiqueta_detalle WHERE isSynced = 0 LIMIT :limit OFFSET :offset")
    suspend fun getNoSyncedPaged(limit: Int, offset: Int): List<EtiquetaDetalleEntity>

}