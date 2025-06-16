package com.example.fibra_labeling.data.local.mapper

import com.example.fibra_labeling.data.local.entity.fibrafil.FibOITMEntity
import com.example.fibra_labeling.data.model.OITMData

fun OITMData.toEntity(): FibOITMEntity? =
    codesap?.let {
        FibOITMEntity(
            codesap = it,
            desc = desc,
            unida = unida,
            codebars = codebars ?: ""
        )
    }

fun FibOITMEntity.toOitmData(): OITMData?=
    OITMData(
        codesap = codesap,
        desc = desc,
        unida = unida,
        codebars = codebars
    )