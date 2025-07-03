package com.example.fibra_labeling.ui.screen.setting.servidor.form

data class ServerSettingFormState(
    val nombre: String = "",
    val url: String = "",
    val nombreError: String? = null,
    val urlError: String? = null,
    val isValid: Boolean = false
)


fun ServerSettingFormState.validate(): ServerSettingFormState {
    var nombreError: String? = null
    var urlError: String? = null
    var isValid = true

    if (nombre.isBlank()) {
        nombreError = "El nombre es requerido"
        isValid = false
    }

    if (url.isBlank()) {
        urlError = "La URL es requerida"
        isValid = false
    } else if (!isValidUrl(url)) {
        urlError = "La URL no es válida"
        isValid = false
    }

    return this.copy(
        nombreError = nombreError,
        urlError = urlError,
        isValid = isValid
    )
}

// Utilidad para validar URL simple
fun isValidUrl(url: String): Boolean {
    // Puedes hacer una validación más avanzada si quieres
    return try {
        val parsedUrl = java.net.URL(url)
        parsedUrl.toURI()
        true
    }catch (e: Exception) {
        false
    }
}
