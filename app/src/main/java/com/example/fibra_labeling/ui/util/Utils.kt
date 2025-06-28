package com.example.fibra_labeling.ui.util

import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import java.time.LocalDate
import java.time.LocalTime
import java.time.format.DateTimeFormatter

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