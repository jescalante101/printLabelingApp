package com.example.fibra_labeling.ui.screen.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp

@Composable
fun CustomCountingWidget(
    onSave: (value: String) -> Unit = { _ -> },
    product: String = "Pernos",
    itemCode: String = "123456789",
    conteo: Double = 0.0,
    operation: String="Cantidad Contada",
    unidad: String=""
) {
    var cantidad by remember { mutableStateOf("0") }
    val configuration = LocalConfiguration.current
    val screenHeight = configuration.screenHeightDp.dp

    // Determinar si es una pantalla pequeña
    val isSmallScreen = screenHeight < 600.dp

    // Ajustar espaciado y tamaños según el tamaño de pantalla
    val verticalSpacing = if (isSmallScreen) 16.dp else 32.dp
    val outerPadding = if (isSmallScreen) 16.dp else 24.dp
    val cardPadding = if (isSmallScreen) 12.dp else 20.dp
    val counterPadding = if (isSmallScreen) 20.dp else 32.dp
    val buttonSize = if (isSmallScreen) 52.dp else 64.dp

    LaunchedEffect(Unit) {
        cantidad = if(conteo>0){
            if(conteo.toString().contains(".")){
                conteo.toString()
            }else{
                conteo.toInt().toString()
            }
        }else{
            "0"
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(outerPadding),
            verticalArrangement = Arrangement.spacedBy(verticalSpacing)
        ) {
            // Header Section
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surface
                ),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
            ) {
                Column(
                    modifier = Modifier.padding(cardPadding),
                    verticalArrangement = Arrangement.spacedBy(if (isSmallScreen) 4.dp else 8.dp)
                ) {
                    Text(
                        text = product,
                        style = if (isSmallScreen) MaterialTheme.typography.headlineSmall else MaterialTheme.typography.headlineMedium,
                        color = MaterialTheme.colorScheme.onSurface,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = "Código: $itemCode",
                        style = if (isSmallScreen) MaterialTheme.typography.bodySmall else MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
                    )
                }
            }

            // Counter Display
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer
                ),
                elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(counterPadding),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(if (isSmallScreen) 4.dp else 8.dp)
                ) {
                    Text(
                        text = operation,
                        style = if (isSmallScreen) MaterialTheme.typography.titleSmall else MaterialTheme.typography.titleMedium,
                        color = MaterialTheme.colorScheme.onPrimaryContainer
                    )
                    Text(
                        text = "$cantidad $unidad",
                        style = if (isSmallScreen) MaterialTheme.typography.headlineMedium else MaterialTheme.typography.headlineLarge,
                        color = MaterialTheme.colorScheme.primary,
                        fontWeight = FontWeight.Bold
                    )
                }
            }

            // Numeric Keypad
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surface
                ),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
            ) {
                Column(
                    modifier = Modifier.padding(if (isSmallScreen) 12.dp else 16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(if (isSmallScreen) 8.dp else 12.dp)
                ) {
                    val numbers = listOf(
                        listOf("1", "2", "3"),
                        listOf("4", "5", "6"),
                        listOf("7", "8", "9"),
                        listOf("⌫", "0", "."),
                        listOf("", "💾", "")
                    )

                    numbers.forEach { row ->
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceEvenly
                        ) {
                            row.forEach { label ->
                                if (label.isNotEmpty()) {
                                    val isSpecial = label == "⌫" || label == "💾"
                                    val backgroundColor = when (label) {
                                        "⌫" -> MaterialTheme.colorScheme.secondary
                                        "💾" -> MaterialTheme.colorScheme.primary
                                        else -> MaterialTheme.colorScheme.surface
                                    }
                                    val contentColor = when (label) {
                                        "⌫" -> MaterialTheme.colorScheme.onSecondary
                                        "💾" -> MaterialTheme.colorScheme.onPrimary
                                        else -> MaterialTheme.colorScheme.onSurface
                                    }

                                    Card(
                                        modifier = Modifier
                                            .size(buttonSize)
                                            .weight(1f, false),
                                        colors = CardDefaults.cardColors(
                                            containerColor = backgroundColor
                                        ),
                                        elevation = CardDefaults.cardElevation(
                                            defaultElevation = if (isSpecial) 6.dp else 2.dp
                                        ),
                                        onClick = {
                                            when (label) {
                                                "⌫" -> {
                                                    cantidad = if (cantidad.length == 1) {
                                                        "0"
                                                    } else {
                                                        cantidad.dropLast(1)
                                                    }
                                                }
                                                "💾" -> {
                                                    onSave(cantidad)
                                                }
                                                "." -> {
                                                    // Solo agregar punto si no hay uno ya
                                                    if (!cantidad.contains(".")) {
                                                        cantidad = if (cantidad == "0") {
                                                            "0."
                                                        } else {
                                                            cantidad + "."
                                                        }
                                                    }
                                                }
                                                else -> {
                                                    cantidad = if (cantidad == "0") {
                                                        label
                                                    } else {
                                                        cantidad + label
                                                    }
                                                }
                                            }
                                        }
                                    ) {
                                        Box(
                                            modifier = Modifier.fillMaxSize(),
                                            contentAlignment = Alignment.Center
                                        ) {
                                            Text(
                                                text = label,
                                                style = if (isSmallScreen) MaterialTheme.typography.headlineSmall else MaterialTheme.typography.headlineLarge,
                                                color = contentColor,
                                                fontWeight = FontWeight.Medium,
                                                textAlign = TextAlign.Center
                                            )
                                        }
                                    }
                                } else {
                                    // Spacer para botones vacíos
                                    Spacer(
                                        modifier = Modifier
                                            .size(buttonSize)
                                            .weight(1f, false)
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}