package com.example.fibra_labeling.data.local.entity.fibrafil

import androidx.room.Embedded
import androidx.room.Relation

data class FibOincWithDetalles(
    @Embedded val cabecera: FibOincEntity,
    @Relation(
        parentColumn = "docEntry",
        entityColumn = "docEntry"
    )
    val detalles: List<FibIncEntity>
)
