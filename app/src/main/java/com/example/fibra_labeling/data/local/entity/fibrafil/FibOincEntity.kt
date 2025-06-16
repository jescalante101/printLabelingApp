package com.example.fibra_labeling.data.local.entity.fibrafil

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "fib_oinc")
data class FibOincEntity(
    @PrimaryKey(autoGenerate = true) val docEntry: Long = 0,
    val u_CountDate: String?,
    val u_EndTime: String?=null,
    val u_Ref: String?,
    val u_Remarks: String?,
    val u_StartTime: String?,
    val u_UserNameCount: String?,
    val u_userCodeCount: String?,
    val isSynced: Boolean = false
)