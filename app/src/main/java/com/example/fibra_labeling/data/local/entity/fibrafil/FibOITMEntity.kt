package com.example.fibra_labeling.data.local.entity.fibrafil

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "fib_oitm")
data class FibOITMEntity(
    @PrimaryKey val codesap: String, // Usamos el código SAP como PK
    val desc: String?,
    val unida: String?,
    val codebars: String? = ""
)
