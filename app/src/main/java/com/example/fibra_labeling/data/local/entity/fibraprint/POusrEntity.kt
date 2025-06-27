package com.example.fibra_labeling.data.local.entity.fibraprint

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "p_ousr")
data class POusrEntity(
    @PrimaryKey val userid: Int, // userid será la clave primaria
    val uNAME: String?,
    val useRCODE: String?
)
