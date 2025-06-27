package com.example.fibra_labeling.data.local.dao.fibrafil

import androidx.room.*
import com.example.fibra_labeling.data.local.entity.fibrafil.FibIncEntity
import com.example.fibra_labeling.data.local.entity.fibrafil.FibOincEntity
import com.example.fibra_labeling.data.local.entity.fibrafil.FibOincWithDetalles
import kotlinx.coroutines.flow.Flow

@Dao
interface FibOincDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(oinc: FibOincEntity): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(items: List<FibOincEntity>): List<Long>

    // Aquí está el cambio: ahora es Flow, no suspend
    @Query("SELECT * FROM fib_oinc where u_usernameCount like '%' || :filter || '%' or u_Ref like '%' || :filter || '%' or docNum like '%' || :filter || '%' order by docEntry desc ")
    fun getAllFlow(filter:String): Flow<List<FibOincEntity>>

    @Query("SELECT * FROM fib_oinc WHERE docEntry = :id")
    suspend fun getById(id: Long): FibOincEntity?

    @Query("SELECT * FROM fib_oinc WHERE isSynced = 0")
    fun getNotSynced(): List<FibOincEntity>

    @Update
    suspend fun update(oinc: FibOincEntity)

    @Delete
    suspend fun delete(oinc: FibOincEntity)

    @Query("DELETE FROM fib_oinc")
    suspend fun deleteAll()

    @Transaction
    suspend fun insertCabeceraConDetalles(
        cabecera: FibOincEntity,
        detalles: List<FibIncEntity>,
        detalleDao: FibIncDao // Pasas el DAO del detalle aquí, ver comentario abajo
    ) {
        val docEntry = insert(cabecera)
        detalles.forEach { detalle ->
            detalleDao.insert(detalle.copy(docEntry = docEntry.toInt()))
        }
    }

    @Transaction
    @Query("SELECT * FROM fib_oinc where u_usernameCount like '%' || :filter || '%' or u_Ref like '%' || :filter || '%' or docNum like '%' || :filter || '%' order by docEntry desc ")
    fun  getOincWithDetalles(filter: String): Flow<List<FibOincWithDetalles>>

    @Transaction
    @Query("SELECT * FROM fib_oinc WHERE isSynced = 0")
    suspend fun getAllNotSyncedWithDetalles(): List<FibOincWithDetalles>

    @Transaction
    @Query("SELECT * FROM fib_oinc WHERE isSynced = 0 AND docEntry = :docEntry")
    suspend fun getOincNotSyncedWithDetalles(docEntry: Long): FibOincWithDetalles



    @Query("""
    UPDATE fib_oinc 
    SET isSynced = 1, 
        u_EndTime = :fechaSync,
        docNum = :docNum,
        fechaSync = :fechaSync
    WHERE docEntry = :docEntry
""")
    suspend fun markOincAsSynced(
        docEntry: Long,
        docNum: String,
        fechaSync: String // El formato de la fecha lo puedes hacer con un simple `DateFormat`
    )


}
