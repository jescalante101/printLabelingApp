package com.example.fibra_labeling.data.local.dao.fibraprint

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.fibra_labeling.data.local.entity.fibraprint.POwhsEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface PrintOwhsDao {


    // Insertar un solo registro
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertar(almacen: POwhsEntity)

    // Insertar una lista de registros
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAllPowsh(lista: List<POwhsEntity>)

    // Obtener todos los registros
    @Query("SELECT * FROM p_owhs")
    suspend fun obtenerTodos(): List<POwhsEntity>

    // Buscar por código o nombre de almacén (LIKE)
    @Query("""
        SELECT * FROM p_owhs
        WHERE whsCode LIKE '%' || :filtro || '%'
           OR whsName LIKE '%' || :filtro || '%'
    """)
    fun buscarPorCodigoONombre(filtro: String): Flow<List<POwhsEntity>>

    // Obtener uno por código (clave primaria)
    @Query("SELECT * FROM p_owhs WHERE whsCode = :codigo")
    suspend fun obtenerPorCodigo(codigo: String): POwhsEntity?

    // Obtener uno por nombre (clave primaria)
    @Query("SELECT * FROM p_owhs WHERE whsName = :nombre or whsCode = :nombre")
    suspend fun findByName(nombre: String): POwhsEntity?

    // Actualizar un registro
    @Update
    suspend fun actualizar(almacen: POwhsEntity)

    // Eliminar un registro
    @Delete
    suspend fun eliminar(almacen: POwhsEntity)

    // Eliminar todos los registros
    @Query("DELETE FROM p_owhs")
    suspend fun eliminarTodos()
}