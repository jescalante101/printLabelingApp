package com.example.fibra_labeling.data.local.entity.fibrafil

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "zpl_labels")
data class ZplLabel(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,

    @ColumnInfo(name = "code_name")
    val codeName: String,

    @ColumnInfo(name = "name")
    val name: String,

    @ColumnInfo(name = "description")
    val description: String,

    @ColumnInfo(name = "zpl_file")
    val zplFile: String,

    //selected
    @ColumnInfo(name = "selected")
    val selected: Boolean = false,

    @ColumnInfo(name = "compania_id", defaultValue = "01") // Añadimos un default por seguridad
    val companiaId: String="01"

)