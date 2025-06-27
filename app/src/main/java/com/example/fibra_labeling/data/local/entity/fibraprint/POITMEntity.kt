package com.example.fibra_labeling.data.local.entity.fibraprint

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "p_oitm")
data class POITMEntity(
    @PrimaryKey val codesap: String, // Usamos el código SAP como PK
    val desc: String?,
    val unida: String?,
    val codebars: String? = ""
)