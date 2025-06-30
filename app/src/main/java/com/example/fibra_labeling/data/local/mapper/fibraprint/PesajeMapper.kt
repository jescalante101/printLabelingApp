package com.example.fibra_labeling.data.local.mapper.fibraprint

import com.example.fibra_labeling.data.local.entity.fibraprint.PesajeEntity
import com.example.fibra_labeling.data.model.ImobPesaje
import com.example.fibra_labeling.data.model.OITMData
import org.koin.core.qualifier.named

fun PesajeEntity.toOitmData() : OITMData =
    OITMData(
        codesap = codigo,
        desc = nombre,
        unida = unidad,
        codebars = codigoBarra
    )

fun PesajeEntity.toImobPesaje() : ImobPesaje =
    ImobPesaje(
        itemCode = codigo ?: "",
        name = nombre ?: "",
        proveedor = proveedor ?: "",
        lote = lote ?: "",
        almacen = almacen ?: "",
        motivo = "",
        ubicacion = ubicacion ?: "",
        piso = piso ?: "",
        metroLineal = metroLineal ?: "",
        equivalente = "",
        peso = peso,
        codeBar = codigoBarra,
        createDate = fecha,
        docEntry = id.toInt(),
        userCreate = usuario ?: "",
    )