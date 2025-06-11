package com.example.fibra_labeling.ui.screen.print.register.form

import com.example.fibra_labeling.data.model.AlmacenResponse

data class FormState(
    val codigo:String="",
    val name: String="",
    val proveedor: String="",
    val lote: String="",
    val almacen: AlmacenResponse?=null,
    val motivo: String="",
    val ubicacion: String="",
    val piso: String="",
    val metroLineal: String="",
    val equivalente: String="",
    val pesoBruto: String="",
    val usuario:String="jtevez",
    val stepd:String="",
    val isValid: Boolean=false
)
