package com.example.fibra_labeling.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.fibra_labeling.data.local.entity.fibrafil.ZplLabel
import kotlinx.coroutines.flow.Flow

@Dao
interface ZplLabelDao {

    @Query("SELECT * FROM zpl_labels ORDER BY name ASC")
    fun getAllLabels(): Flow<List<ZplLabel>>

    @Query("SELECT * FROM zpl_labels WHERE id = :id")
    suspend fun getLabelById(id: Long): ZplLabel?

    @Query("SELECT * FROM zpl_labels WHERE code_name = :codeName")
    suspend fun getLabelByCodeName(codeName: String): ZplLabel?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertLabel(label: ZplLabel): Long

    @Update
    suspend fun updateLabel(label: ZplLabel)

    @Delete
    suspend fun deleteLabel(label: ZplLabel)

    @Query("DELETE FROM zpl_labels WHERE id = :id")
    suspend fun deleteLabelById(id: Long)

    @Query("SELECT COUNT(*) FROM zpl_labels")
    suspend fun getLabelsCount(): Int
}