package com.example.fibra_labeling.data.local.entity.fibraprint

import android.R
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "p_inc")
data class PIncEntity(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    val id: Int = 0,

    @ColumnInfo(name = "doc_entry")
    val docEntry: Long,

    @ColumnInfo(name = "item_code")
    val itemCode: String,

    @ColumnInfo(name = "item_name")
    val itemName: String,

    @ColumnInfo(name = "whs_code")
    val whsCode: String,

    @ColumnInfo(name = "in_whs_qty")
    val inWhsQty: Double?, // decimal? en C# -> Double?

    @ColumnInfo(name = "count_qty")
    val countQty: Double?,

    @ColumnInfo(name = "difference")
    val difference: Double?,

    @ColumnInfo(name = "bin_location")
    val binLocation: String?=null,

    @ColumnInfo(name = "ref1")
    val ref1: String?=null,

    @ColumnInfo(name = "ref2")
    val ref2: String?=null,

    @ColumnInfo(name = "machine_code")
    val machineCode: String?=null,

    @ColumnInfo(name = "u_area")
    val uarea: String?=null,

    @ColumnInfo(name = "u_codeBar")
    val codeBar: String?=null,




    val isSynced: Boolean = false
)