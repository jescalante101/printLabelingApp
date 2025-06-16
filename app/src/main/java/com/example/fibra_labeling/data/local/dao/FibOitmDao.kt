package com.example.fibra_labeling.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.fibra_labeling.data.local.entity.fibrafil.FibOITMEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface FibOitmDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(items: List<FibOITMEntity>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(entity: FibOITMEntity)

    @Query("SELECT * FROM fib_oitm")
    suspend fun getAll(): List<FibOITMEntity>

    @Query("SELECT * FROM fib_oitm WHERE codesap = :codesap")
    suspend fun getByCode(codesap: String): FibOITMEntity?

    // Búsqueda por desc, codebars o codesap (LIKE)
    @Query("SELECT * FROM fib_oitm WHERE codesap LIKE :first OR fib_oitm.`desc` LIKE :second ")
//    @Query("""
//    SELECT * FROM fib_oitm
//    WHERE
//        (
//            (:first IS NULL OR codesap LIKE :first)
//            AND (:second IS NULL OR `desc` LIKE :second)
//        )
//        OR
//        (
//            (:first IS NULL OR `desc` LIKE :first)
//            AND (:second IS NULL OR codesap LIKE :second)
//        )
//""")
    fun searchFlow(first: String?, second: String?): Flow<List<FibOITMEntity>>

    @Delete
    suspend fun delete(entity: FibOITMEntity)

    @Query("DELETE FROM fib_oitm")
    suspend fun deleteAll()

}