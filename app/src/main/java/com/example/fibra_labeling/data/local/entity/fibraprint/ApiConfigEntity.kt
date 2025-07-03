package com.example.fibra_labeling.data.local.entity.fibraprint

import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity(tableName = "api_config")
data class ApiConfigEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,

    val empresa: String,  // Ejemplo: "Fibrafil" o "Fibraprint"
    val nombre: String,   // Ejemplo: "Producción Lima"
    val urlBase: String,   // Ejemplo: "https://api.fibraprint.com/"
    val isSelect: Boolean = false
)