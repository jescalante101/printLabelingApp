package com.example.fibra_labeling.fibrafil.ui.screen.etiqueta.etiquetanueva.form

fun validateAddEtiquetaForm(form: AddEtiquetaFormState): AddEtiquetaFormErrorState {
    return AddEtiquetaFormErrorState(
        almacenError = if (form.almacen?.whsName.isNullOrBlank() || form.almacen.whsCode.isBlank()) "Almacén requerido" else null,
        maquinaError = if (form.maquina?.code?.isBlank() == true) "Máquina requerida" else null,
        cantidadError = if (form.conteo.isBlank()) "Conteo requerido" else null,
    )
}

fun AddEtiquetaFormErrorState.hasError() =
    listOf( almacenError, maquinaError, cantidadError).any { it != null }
