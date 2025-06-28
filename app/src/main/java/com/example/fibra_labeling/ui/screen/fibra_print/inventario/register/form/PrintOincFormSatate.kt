package com.example.fibra_labeling.ui.screen.fibra_print.inventario.register.form

import com.example.fibra_labeling.data.local.entity.fibraprint.POincEntity
import com.example.fibra_labeling.data.local.entity.fibraprint.POusrEntity

data class PrintOincFormState(
    val selectedOption: TipoRegistro = TipoRegistro.USUARIO,
    val selectedUser: POusrEntity? = null,
    val selectedEmpleado: String = "",
    val referencia: String = "",
    val observaciones: String = "",
    val errorMessage: String? = null
)

enum class TipoRegistro {
    USUARIO,
    EMPLEADO
}

fun PrintOincFormState.validate(): PrintOincFormState {
    return when {
        selectedOption == TipoRegistro.USUARIO && selectedUser == null -> {
            copy(errorMessage = "Debe seleccionar un usuario.")
        }
        selectedOption == TipoRegistro.EMPLEADO && selectedEmpleado.isBlank() -> {
            copy(errorMessage = "Debe ingresar un empleado.")
        }
        referencia.isBlank() -> {
            copy(errorMessage = "La referencia no puede estar vacía.")
        }
        else -> {
            copy(errorMessage = null)
        }
    }
}