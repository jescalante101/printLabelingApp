package com.example.fibra_labeling.data.local.entity.fibraprint

import androidx.room.Embedded
import androidx.room.Relation

data class POincWithDetails(
    @Embedded val header: POincEntity,

    @Relation(
        parentColumn = "docEntry",
        entityColumn = "doc_entry"
    )
    val details: List<PIncEntity>

)

