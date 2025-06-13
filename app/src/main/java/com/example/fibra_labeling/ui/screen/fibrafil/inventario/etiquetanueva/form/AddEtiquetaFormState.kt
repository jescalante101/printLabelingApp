package com.example.fibra_labeling.ui.screen.fibrafil.inventario.etiquetanueva.form

import com.example.fibra_labeling.data.model.AlmacenResponse

data class AddEtiquetaFormState(
    val codigo: String = "",
    val producto: String = "",
    val lote: String = "",
    val almacen: AlmacenResponse?=null,
    val codigoReferencia: String = "",
    val maquina: String = "",
    val ubicacion: String = ""
)
