package com.example.fibra_labeling.fibraprint.ui.screen.etiqueta.register.form

data class FormErrorState(
    val almacenError: String? = null,
    val ubicacionError: String? = null,
    val motivoError: String? = null,
    val pisoError: String? = null,
    val pesoBrutoError: String? = null,
    val metroLinealError: String? = null,
    val loteError: String? = null,
)