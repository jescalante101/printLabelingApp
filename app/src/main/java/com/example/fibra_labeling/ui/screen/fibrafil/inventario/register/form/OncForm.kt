package com.example.fibra_labeling.ui.screen.fibrafil.inventario.register.form

import com.example.fibra_labeling.data.local.entity.fibrafil.FilUserEntity

data class OncForm(
    val usuario: FilUserEntity?=null,
    val referencia: String = "",
    val remarks: String = "",
)
