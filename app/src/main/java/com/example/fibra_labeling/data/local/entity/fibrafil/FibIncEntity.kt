package com.example.fibra_labeling.data.local.entity.fibrafil

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "fib_inc")
data class FibIncEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,

    val U_CountQty: Double?,
    val U_Difference: Double?,
    val U_InWhsQty: Double?,
    val U_ItemCode: String?,
    val U_ItemName: String?,
    val U_WhsCode: String?,
    val isSynced: Boolean = false
)