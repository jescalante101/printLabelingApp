package com.example.fibra_labeling.ui.screen.setting.printer.form

fun validatePrintForm(state: PrintFormState): PrintFormErrorState {
    val ipValid = state.ip.split(".").run {
        size == 4 && all { it.toIntOrNull() in 0..255 }
    }
    val portValid = state.port.toIntOrNull()?.let { it in 1..65535 } ?: false

    return PrintFormErrorState(
        printerNameError = if (state.printerName.isBlank()) "Nombre requerido" else null,
        ipError = if (state.ip.isBlank()) "IP requerida"
        else if (!ipValid) "Formato de IP inválido" else null,
        portError = if (state.port.isBlank()) "Puerto requerido"
        else if (!portValid) "Puerto inválido (1-65535)" else null
    )
}

fun isFormValid(errors: PrintFormErrorState): Boolean {
    return errors.printerNameError == null && errors.ipError == null && errors.portError == null
}
