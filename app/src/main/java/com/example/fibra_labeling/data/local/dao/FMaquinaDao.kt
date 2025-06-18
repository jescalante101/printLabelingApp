package com.example.fibra_labeling.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.fibra_labeling.data.local.entity.fibrafil.FMaquinaEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface FMaquinaDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(maquinas: List<FMaquinaEntity>)

    @Query("SELECT * FROM f_maquina")
    fun getAllFlow(): Flow<List<FMaquinaEntity>>

    @Query("""
        SELECT * FROM f_maquina
        WHERE (:name IS NULL OR name LIKE '%' || :name || '%')
          OR (:code IS NULL OR code LIKE '%' || :code || '%')
    """)
    fun searchByNameAndCode(name: String?, code: String?): Flow<List<FMaquinaEntity>>

    // get by code
    @Query("SELECT * FROM f_maquina WHERE code = :code")
    suspend fun getByCode(code: String): FMaquinaEntity?
}
