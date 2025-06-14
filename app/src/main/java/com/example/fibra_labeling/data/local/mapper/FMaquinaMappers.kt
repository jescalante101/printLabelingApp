package com.example.fibra_labeling.data.local.mapper

import com.example.fibra_labeling.data.local.entity.fibrafil.FMaquinaEntity
import com.example.fibra_labeling.data.model.MaquinaData

fun MaquinaData.toEntity(): FMaquinaEntity =
    FMaquinaEntity(
        code = this.code ?: "",  // Si viene null, ponle un string vacío (puedes customizar esto)
        name = this.name
    )
fun FMaquinaEntity.toApiData(): MaquinaData=
    MaquinaData(
        code = this.code,
        name = this.name
    )