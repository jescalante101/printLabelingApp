package com.example.fibra_labeling.data.local.entity.fibrafil

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "fib_almacen")
data class FibAlmacenEntity(
    @PrimaryKey val whsCode: String,
    val whsName: String
)
