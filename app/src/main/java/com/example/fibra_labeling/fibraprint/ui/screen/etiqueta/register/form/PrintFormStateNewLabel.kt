package com.example.fibra_labeling.fibraprint.ui.screen.etiqueta.register.form

import com.example.fibra_labeling.data.local.entity.fibraprint.POcrdEntity
import com.example.fibra_labeling.data.local.entity.fibraprint.POwhsEntity

data class PrintFormStateNewLabel(
    val codigo:String="",
    val name: String="",
    val proveedor: POcrdEntity?=null,
    val lote: String="",
    val almacen: POwhsEntity?=null,
    val ubicacion: String="",
    val piso: String="",
    val metroLineal: String="",
    val zona: String="",
    val pesoBruto: String="",
    val usuario:String="jtevez",
    val isValid: Boolean=false,
    val unidad: String="",
    val observacion: String="",

)
