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
    WHERE codesap LIKE :filter OR `desc` LIKE :filter
    """)
    fun searchProduct(filter:String): Flow<List<POITMEntity>>

}