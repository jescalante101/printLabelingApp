package com.example.fibra_labeling.fibrafil.ui.screen.inventario.details.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.fibra_labeling.R
import com.example.fibra_labeling.data.local.entity.fibrafil.FibIncEntity

@Composable
fun FioriCardIncCompact(
    dto: FibIncEntity,
    modifier: Modifier = Modifier,
    onClick: (dto: FibIncEntity) -> Unit = {},
    enabled: Boolean = true,
    onPrintClick: (dto: FibIncEntity) -> Unit = {} // <-- Nuevo parámetro
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
                FioriBadge(text = dto.U_ItemCode ?: "")
                FioriBadge(text = dto.U_WhsCode ?: "")
            }
            Spacer(Modifier.height(10.dp))

            // 2. Descripción del producto
            Text(
                text = dto.U_ItemName ?: "",
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
                    Text(dto.U_InWhsQty?.toString() ?: "-", fontWeight = FontWeight.Bold)
                }
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text("Contado", style = MaterialTheme.typography.labelSmall, color = Color(0xFF607D8B))
                    Text(dto.U_CountQty?.toString() ?: "-", fontWeight = FontWeight.Bold)
                }
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text("Diferencia", style = MaterialTheme.typography.labelSmall, color = Color(0xFF607D8B))
                    Text(
                        text = dto.U_Difference?.toString() ?: "-",
                        fontWeight = FontWeight.Bold,
                        color = when {
                            dto.U_Difference == null -> Color.Gray
                            dto.U_Difference > 0.0 -> Color(0xFF388E3C)
                            dto.U_Difference < 0.0 -> Color(0xFFD32F2F)
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

@Composable
private fun LabelAndValueFiori(label: String, value: String) {
    Text(
        text = label,
        color = Color(0xFF607D8B),
        style = MaterialTheme.typography.labelSmall
    )
    Text(
        text = value,
        style = MaterialTheme.typography.bodyLarge,
        fontWeight = FontWeight.Bold,
        color = Color.Black,
        modifier = Modifier.padding(bottom = 8.dp)
    )
}

@Preview(showBackground = true)
@Composable
fun PreviewFioriCardIncCompact() {
    FioriCardIncCompact(
        dto = FibIncEntity(
            id = 1,
            U_CountQty = 5.0,
            U_Difference = -2.0,
            U_InWhsQty = 7.0,
            U_ItemCode = "ARE01",
            U_ItemName = "Abrazadera Plana",
            U_WhsCode = "ALM-01",
            isSynced = false,
            docEntry = 123
        )
    )
}

