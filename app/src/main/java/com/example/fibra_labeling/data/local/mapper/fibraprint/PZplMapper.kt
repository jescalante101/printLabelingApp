package com.example.fibra_labeling.data.local.mapper.fibraprint

import com.example.fibra_labeling.data.local.entity.fibraprint.PesajeEntity

fun PesajeEntity.toMap(): Map<String, String> {
    return mapOf(
        "Peso" to peso.toString(),
        "CreateDate" to fecha,
        "CodeBar" to codigoBarra,
        "Almacen" to (almacen ?: ""),
        "Proveedor" to (proveedor ?: ""),
        "Lote" to (lote ?: ""),
        "Ubicacion" to ("$ubicacion - $u_area"),
        "MetroLineal" to (metroLineal ?: ""),
        "name" to (nombre ?: ""),
        "Motivo" to ("-"),
        "ItemCode" to ( codigo?: "")
    )
}