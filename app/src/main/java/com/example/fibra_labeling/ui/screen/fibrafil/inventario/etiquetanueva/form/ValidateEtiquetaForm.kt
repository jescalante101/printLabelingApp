package com.example.fibra_labeling.ui.screen.fibrafil.inventario.etiquetanueva.form

fun validateAddEtiquetaForm(form: AddEtiquetaFormState): AddEtiquetaFormErrorState {
    return AddEtiquetaFormErrorState(
        loteError = if (form.lote.isBlank()) "Lote requerido" else null,
        almacenError = if (form.almacen?.whsName.isNullOrBlank() || form.almacen.whsCode.isBlank()) "Almacén requerido" else null,
        codigoReferenciaError = if (form.codigoReferencia.isBlank()) "Referencia requerida" else null,
        maquinaError = if (form.maquina?.code?.isBlank() == true ) "Máquina requerida" else null,
        ubicacionError = if (form.ubicacion.isBlank()) "Ubicación requerida" else null,
    )
}

fun AddEtiquetaFormErrorState.hasError() =
    listOf(loteError, almacenError, codigoReferenciaError, maquinaError, ubicacionError).any { it != null }
