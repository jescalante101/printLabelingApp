package com.example.fibra_labeling.data.local.dao.fibraprint

import androidx.room.*
import com.example.fibra_labeling.data.local.entity.fibraprint.ApiConfigEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface ApiConfigDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertConfig(config: ApiConfigEntity): Long

    @Update
    suspend fun updateConfig(config: ApiConfigEntity)

    @Delete
    suspend fun deleteConfig(config: ApiConfigEntity)

    @Query("SELECT * FROM api_config")
    suspend fun getAllConfigs(): List<ApiConfigEntity>

    @Query("SELECT * FROM api_config WHERE empresa = :empresa")
    fun getConfigsByEmpresa(empresa: String): Flow<List<ApiConfigEntity>>

    @Query("SELECT * FROM api_config WHERE id = :id LIMIT 1")
    suspend fun getConfigById(id: Int): ApiConfigEntity?

    @Query("SELECT * FROM api_config WHERE empresa = :empresa AND isSelect = 1 LIMIT 1")
    suspend fun getSelectedConfigByEmpresa(empresa: String): ApiConfigEntity?

    // --------- NUEVAS FUNCIONES ---------
    @Query("UPDATE api_config SET isSelect = 0 WHERE empresa = :empresa")
    suspend fun deseleccionarConfigsPorEmpresa(empresa: String)

    @Query("UPDATE api_config SET isSelect = 1 WHERE id = :id")
    suspend fun seleccionarConfigPorId(id: Int)
}

