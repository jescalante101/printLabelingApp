package com.example.fibra_labeling.ui.screen.fibra_print.inventario.details.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.fibra_labeling.R
import com.example.fibra_labeling.data.local.entity.fibraprint.PIncEntity

@Composable
fun PrintFioriCardIncCompact(
    dto: PIncEntity,
    modifier: Modifier = Modifier,
    onClick: (dto: PIncEntity) -> Unit = {},
    enabled: Boolean = true,
    onPrintClick: (dto: PIncEntity) -> Unit = {} // <-- Nuevo parámetro
) {
    Card(
        enabled = enabled,
        modifier = modifier
            .padding(horizontal = 16.dp, vertical = 10.dp)
            .fillMaxWidth(),
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFFF5F8FA)),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
        onClick = { onClick(dto) }
    ) {
        Column(Modifier.padding(18.dp)) {
            // 1. Header con badges
            Row(
                Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                FioriBadge(text = dto.itemCode)
                FioriBadge(text = dto.whsCode)
            }
            Spacer(Modifier.height(10.dp))

            // 2. Descripción del producto
            Text(
                text = dto.itemName,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF004990),
                modifier = Modifier.padding(bottom = 10.dp)
            )

            // 3. Stock y diferencia
            Row(
                Modifier
                    .fillMaxWidth()
                    .padding(vertical = 6.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text("Sistema", style = MaterialTheme.typography.labelSmall, color = Color(0xFF607D8B))
                    Text(dto.inWhsQty?.toString() ?: "-", fontWeight = FontWeight.Bold)
                }
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text("Contado", style = MaterialTheme.typography.labelSmall, color = Color(0xFF607D8B))
                    Text("${dto.countQty?.toString()} ${dto.ref2}" , fontWeight = FontWeight.Bold)
                }
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text("Diferencia", style = MaterialTheme.typography.labelSmall, color = Color(0xFF607D8B))
                    Text(
                        text = dto.difference?.toString() ?: "-",
                        fontWeight = FontWeight.Bold,
                        color = when {
                            dto.difference == null -> Color.Gray
                            dto.difference > 0.0 -> Color(0xFF388E3C)
                            dto.difference < 0.0 -> Color(0xFFD32F2F)
                            else -> Color.Black
                        }
                    )
                }
            }

            // 4. Sincronización + Botón de impresión
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.End,
                verticalAlignment = Alignment.CenterVertically
            ) {
                if (dto.isSynced) {
                    Icon(
                        imageVector = Icons.Default.CheckCircle,
                        contentDescription = "Sincronizado",
                        tint = Color(0xBE214CEF),
                        modifier = Modifier.size(24.dp)
                    )
                } else {
                    Icon(
                        imageVector = Icons.Default.Warning,
                        contentDescription = "No sincronizado",
                        tint = Color(0xFFE74E4E),
                        modifier = Modifier.size(24.dp)
                    )
                }

                // Espacio pequeño entre ícono de sincronización y botón
                Spacer(modifier = Modifier.width(14.dp))

                // Botón de impresión con icono personalizado
                IconButton(
                    onClick = { onPrintClick(dto) },
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_print),
                        contentDescription = "Imprimir",
                        tint = Color(0xFF004990),
                        modifier = Modifier.size(24.dp)
                    )
                }
            }
        }
    }
}

@Composable
private fun FioriBadge(text: String) {
    Box(
        Modifier
            .background(color = Color(0xFF004990).copy(alpha = 0.08f), shape = RoundedCornerShape(8.dp))
            .padding(horizontal = 10.dp, vertical = 4.dp)
    ) {
        Text(
            text = text,
            style = MaterialTheme.typography.labelSmall,
            color = Color(0xFF004990)
        )
    }
}