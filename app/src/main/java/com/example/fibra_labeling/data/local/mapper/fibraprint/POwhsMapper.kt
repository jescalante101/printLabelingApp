package com.example.fibra_labeling.data.local.mapper.fibraprint

import com.example.fibra_labeling.data.local.entity.fibraprint.POwhsEntity
import com.example.fibra_labeling.data.model.AlmacenResponse

fun AlmacenResponse.toEntity(): POwhsEntity=
    POwhsEntity(
        whsCode = whsCode,
        whsName = whsName
    )

fun POwhsEntity.toResponse(): AlmacenResponse=
    AlmacenResponse(
        whsCode = whsCode,
        whsName = whsName
    )