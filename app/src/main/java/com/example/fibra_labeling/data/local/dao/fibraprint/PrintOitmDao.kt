package com.example.fibra_labeling.data.local.dao.fibraprint

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.fibra_labeling.data.local.entity.fibraprint.POITMEntity
import kotlinx.coroutines.flow.Flow


@Dao
interface PrintOitmDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(items: List<POITMEntity>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(item: POITMEntity)

    @Query("SELECT * FROM p_oitm")
    suspend fun getAll(): List<POITMEntity>

    @Query("SELECT * FROM p_oitm WHERE codesap = :code")
    suspend fun getByCode(code: String): POITMEntity?

    @Query("DELETE FROM p_oitm")
    suspend fun clearAll()

    @Query("""
    SELECT * FROM p_oitm
    WHERE 
        (:first IS NULL OR codesap LIKE :first OR `desc` LIKE :first)
    AND (:second IS NULL OR codesap LIKE :second OR `desc` LIKE :second)
    AND (:tercero IS NULL OR codesap LIKE :tercero OR `desc` LIKE :tercero)
    """)
    fun searchProduct(first: String?, second: String?, tercero: String?): Flow<List<POITMEntity>>
}