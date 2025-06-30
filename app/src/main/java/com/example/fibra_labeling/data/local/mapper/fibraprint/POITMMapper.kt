package com.example.fibra_labeling.data.local.mapper.fibraprint

import com.example.fibra_labeling.data.local.entity.fibraprint.POITMEntity
import com.example.fibra_labeling.data.model.OITMData

fun OITMData.toEntity(): POITMEntity? =
    codesap?.let {
        POITMEntity(
            codesap = it,
            desc = desc,
            unida = unida,
            codebars = codebars ?: "",
            cardCode=cardCode ?:""

        )
    }

fun POITMEntity.toOitmData(): OITMData?=
    OITMData(
        codesap = codesap,
        desc = desc,
        unida = unida,
        codebars = codebars,
        cardCode=cardCode
    )