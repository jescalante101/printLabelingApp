package com.example.fibra_labeling.data.local.dao.fibraprint

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.fibra_labeling.data.local.entity.fibraprint.POincEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface PrintOincDao {

    // Insertar un registro
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertar(conteo: POincEntity)

    // Insertar varios registros
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertarLista(lista: List<POincEntity>)

    // Obtener todos los registros de forma reactiva
    @Query("SELECT * FROM p_oinc")
    fun listarTodo(): Flow<List<POincEntity>>

    // Buscar por referencia o nombre de usuario con LIKE
    @Query("""
        SELECT * FROM p_oinc
        WHERE u_Ref LIKE '%' || :filtro || '%'
           OR u_UserNameCount LIKE '%' || :filtro || '%'
    """)
    suspend fun buscarPorReferenciaONombre(filtro: String): List<POincEntity>

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
}