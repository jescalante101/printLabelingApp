package com.example.fibra_labeling.data.local.entity.fibraprint

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "p_ocrd")
data class POcrdEntity(

    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,

    val cardCode: String,

    val cardName: String
)
