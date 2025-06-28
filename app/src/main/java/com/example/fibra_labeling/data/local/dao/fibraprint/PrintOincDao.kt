package com.example.fibra_labeling.data.local.dao.fibraprint

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update
import com.example.fibra_labeling.data.local.entity.fibraprint.POincEntity
import com.example.fibra_labeling.data.local.entity.fibraprint.POincWithDetails
import kotlinx.coroutines.flow.Flow

@Dao
interface PrintOincDao {

    // Insertar un registro
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertar(conteo: POincEntity): Long

    // Insertar varios registros
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertarLista(lista: List<POincEntity>)

    // Obtener todos los registros de forma reactiva
    @Query("SELECT * FROM p_oinc")
    fun listarTodo(): Flow<List<POincEntity>>

    // Buscar por referencia o nombre de usuario con LIKE
    @Transaction
    @Query("""
        SELECT * FROM p_oinc
        WHERE u_Ref LIKE '%' || :filtro || '%'
           OR u_UserNameCount LIKE '%' || :filtro || '%'
    """)
    fun buscarPorReferenciaONombre(filtro: String): Flow<List<POincWithDetails>>

    // Obtener uno por ID
    @Query("SELECT * FROM p_oinc WHERE docEntry = :id")
    suspend fun obtenerPorId(id: Long): POincEntity?

    // Actualizar un registro
    @Update
    suspend fun actualizar(conteo: POincEntity)

    // Eliminar un registro
    @Delete
    suspend fun eliminar(conteo: POincEntity)

    // Eliminar todos los registros
    @Query("DELETE FROM p_oinc")
    suspend fun eliminarTodos()

    @Transaction
    @Query("SELECT * FROM p_oinc")
    suspend fun getAllOincWithDetails(): List<POincWithDetails>
}