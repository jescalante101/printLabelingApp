package com.example.fibra_labeling.data.local.dao.fibraprint

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.fibra_labeling.data.local.entity.fibraprint.POcrdEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface PrintOcrdDao {
    // Insertar un solo registro
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertar(socio: POcrdEntity)

    // Insertar varios registros
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertarLista(socios: List<POcrdEntity>)

    // Obtener todos los registros
    @Query("SELECT * FROM p_ocrd")
    suspend fun obtenerTodos(): List<POcrdEntity>

    // Buscar por código o nombre con LIKE (estilo SAP)
    @Query("""
        SELECT * FROM p_ocrd 
        WHERE cardCode LIKE '%' || :filtro || '%' 
           OR cardName LIKE '%' || :filtro || '%'
    """)
    fun buscarPorCodigoONombre(filtro: String): Flow<List<POcrdEntity>>

    // Obtener uno por ID
    @Query("SELECT * FROM p_ocrd WHERE id = :id")
    suspend fun obtenerPorId(id: Long): POcrdEntity?

    // Actualizar un registro
    @Update
    suspend fun actualizar(socio: POcrdEntity)

    // Eliminar un registro
    @Delete
    suspend fun eliminar(socio: POcrdEntity)

    // Eliminar todos los registros
    @Query("DELETE FROM p_ocrd")
    suspend fun eliminarTodos()
}