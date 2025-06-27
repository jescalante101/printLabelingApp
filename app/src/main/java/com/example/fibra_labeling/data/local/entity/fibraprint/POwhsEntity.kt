package com.example.fibra_labeling.data.local.entity.fibraprint

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "p_owhs")
data class POwhsEntity(
    @PrimaryKey val whsCode: String,
    val whsName: String
)
