package com.example.fibra_labeling.data.local.dao.fibrafil

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

    @Query("SELECT * FROM zpl_labels WHERE compania_id= :idCompany ORDER BY name ASC")
    fun getAllLabels(idCompany:String): Flow<List<ZplLabel>>

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

    @Query("SELECT * FROM zpl_labels WHERE selected = 1")
    suspend fun getSelectedLabel(): ZplLabel?

    // actualizar selected a true
    @Query("UPDATE zpl_labels SET selected = 1 WHERE id = :id")
    suspend fun setSelectedLabel(id: Long)

    @Query("UPDATE zpl_labels SET selected = 0")
    suspend fun unselectAllLabels()

}