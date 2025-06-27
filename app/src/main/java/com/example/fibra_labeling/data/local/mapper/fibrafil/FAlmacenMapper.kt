package com.example.fibra_labeling.data.local.mapper.fibrafil

import com.example.fibra_labeling.data.local.entity.fibrafil.FibAlmacenEntity
import com.example.fibra_labeling.data.model.AlmacenResponse

fun AlmacenResponse.toEntity() = FibAlmacenEntity(
    whsCode = whsCode,
    whsName = whsName
)

fun FibAlmacenEntity.toResponse() = AlmacenResponse(
    whsCode = whsCode,
    whsName = whsName
)