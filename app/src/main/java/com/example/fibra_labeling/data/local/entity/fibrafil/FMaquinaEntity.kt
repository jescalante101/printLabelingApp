package com.example.fibra_labeling.data.local.entity.fibrafil

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "f_maquina")
data class FMaquinaEntity(
    @PrimaryKey val code: String,
    val name: String?
)
