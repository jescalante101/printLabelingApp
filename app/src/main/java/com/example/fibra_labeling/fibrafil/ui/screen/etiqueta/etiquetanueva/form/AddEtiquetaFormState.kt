package com.example.fibra_labeling.fibrafil.ui.screen.etiqueta.etiquetanueva.form

import com.example.fibra_labeling.data.model.AlmacenResponse
import com.example.fibra_labeling.data.model.MaquinaData

data class AddEtiquetaFormState(
    val codigo: String = "",
    val producto: String = "",
    val lote: String = "",
    val almacen: AlmacenResponse?=null,
    val codigoReferencia: String = "",
    val maquina: MaquinaData?=null,
    val ubicacion: String = "",
    val cantidad: String = "",
    val conteo: String = "0.0"
)
