package com.example.fibra_labeling.ui.util

import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import java.math.BigDecimal
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.format.DateTimeFormatter
import java.util.Locale

val gradientBrush = Brush.verticalGradient(
    colors = listOf(
        Color(0xFF4D9BB0), // Azul SAP Fiori
        Color(0xFF0F3F5B)  // Gris oscuro SAP Fiori
    )
)


// Fecha actual
private val fechaActual = LocalDate.now()
val getLocalDateNow: String? = fechaActual.format(DateTimeFormatter.ofPattern("dd-MM-yyyy"))



// Hora actual
private val horaActual = LocalTime.now()
val getLocalTimeNow: String? = horaActual.format(DateTimeFormatter.ofPattern("HH:mm"))


// generate CodeBars
fun generateStringCodeBar(pesoDecimal: BigDecimal): String {
    val now = LocalDateTime.now()

    // Fecha en formato yyMMdd (ejemplo: "250610" para 2025-06-10)
    val fecha = now.format(DateTimeFormatter.ofPattern("yyMMdd"))

    // Hora en formato HHmmss (ejemplo: "142010" para 14:20:10)
    val hora = now.format(DateTimeFormatter.ofPattern("HHmmss"))

    // Peso: convertir a string sin punto y asegurar 6 dígitos (ejemplo: "012500" para 12.50)
    val pesoStr = String.format(Locale.US, "%.2f", pesoDecimal) // "12.50"
    val pesoSinPunto = pesoStr.replace(".", "") // "1250"
    val pesoFormateado = ("000000" + pesoSinPunto).takeLast(6) // "012500"

    // Concatenar todo
    return fecha + hora + pesoFormateado
}