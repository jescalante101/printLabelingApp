package com.example.fibra_labeling.data.local.entity.fibrafil

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "fil_user")
data class FilUserEntity(
    @PrimaryKey val userid: Int, // userid será la clave primaria
    val uNAME: String?,
    val useRCODE: String?
)