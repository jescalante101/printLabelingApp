package com.example.fibra_labeling.data.local.dao.fibraprint

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.fibra_labeling.data.local.entity.fibraprint.POusrEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface PrintOusrDao {
    // Insertar un solo registro
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertar(usuario: POusrEntity)

    // Insertar varios registros
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertarLista(usuarios: List<POusrEntity>)

    // Obtener todos los registros
    @Query("SELECT * FROM p_ousr")
    suspend fun obtenerTodos(): List<POusrEntity>

    // Buscar por nombre o código de usuario con LIKE
    @Query("""
        SELECT * FROM p_ousr
        WHERE uNAME LIKE '%' || :filtro || '%'
           OR useRCODE LIKE '%' || :filtro || '%'
    """)
    fun buscarPorNombreOCodigo(filtro: String): Flow<List<POusrEntity>>

    // Obtener un usuario por ID
    @Query("SELECT * FROM p_ousr WHERE userid = :id")
    suspend fun obtenerPorId(id: Int): POusrEntity?

    // Actualizar un usuario
    @Update
    suspend fun actualizar(usuario: POusrEntity)

    // Eliminar un usuario
    @Delete
    suspend fun eliminar(usuario: POusrEntity)

    // Eliminar todos los registros
    @Query("DELETE FROM p_ousr")
    suspend fun eliminarTodos()
}