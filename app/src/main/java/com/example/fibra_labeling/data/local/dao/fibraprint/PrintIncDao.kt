package com.example.fibra_labeling.data.local.dao.fibraprint
import androidx.room.*
import com.example.fibra_labeling.data.local.entity.fibraprint.PIncEntity

@Dao
interface PrintIncDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(printInc: PIncEntity): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(items: List<PIncEntity>)

    @Query("SELECT * FROM p_inc")
    suspend fun getAll(): List<PIncEntity>

    @Query("SELECT * FROM p_inc WHERE doc_entry = :docEntry")
    suspend fun getByDocEntry(docEntry: Int): List<PIncEntity>

    @Query("""
        SELECT * FROM p_inc
        WHERE item_code LIKE '%' || :filter || '%'
           OR item_name LIKE '%' || :filter || '%'
           OR ref1 LIKE '%' || :filter || '%'
    """)
    suspend fun filterByText(filter: String): List<PIncEntity>

    @Delete
    suspend fun delete(printInc: PIncEntity)

    @Query("DELETE FROM p_inc")
    suspend fun deleteAll()
}
