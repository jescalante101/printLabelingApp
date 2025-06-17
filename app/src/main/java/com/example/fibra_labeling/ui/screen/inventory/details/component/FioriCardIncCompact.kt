package com.example.fibra_labeling.ui.screen.inventory.details.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.fibra_labeling.data.local.entity.fibrafil.FibIncEntity

@Composable
fun FioriCardIncCompact(
    dto: FibIncEntity,
    modifier: Modifier = Modifier,
    onClick: (dto: FibIncEntity) -> Unit = {}
) {
    Card(
        modifier = modifier
            .padding(horizontal = 16.dp, vertical = 10.dp)
            .fillMaxWidth(),
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFFF5F8FA)),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
        onClick = { onClick(dto) }
    ) {
        Column(Modifier.padding(18.dp)) {
            // Badges (Código a la izquierda, almacén a la derecha)
            Row(
                Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                FioriBadge(text = dto.U_ItemCode ?: "")
                FioriBadge(text = dto.U_WhsCode ?: "")
            }
            Spacer(Modifier.height(10.dp))

            // Descripción del producto, en negrita y mayor tamaño
            Text(
                text = dto.U_ItemName ?: "",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF004990),
                modifier = Modifier.padding(bottom = 10.dp)
            )

            // Stock y diferencia en una sola fila visual
            Row(
                Modifier
                    .fillMaxWidth()
                    .padding(vertical = 6.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column(horizontalAlignment = androidx.compose.ui.Alignment.CenterHorizontally) {
                    Text("Sistema", style = MaterialTheme.typography.labelSmall, color = Color(0xFF607D8B))
                    Text(dto.U_InWhsQty?.toString() ?: "-", fontWeight = FontWeight.Bold)
                }
                Column(horizontalAlignment = androidx.compose.ui.Alignment.CenterHorizontally) {
                    Text("Contado", style = MaterialTheme.typography.labelSmall, color = Color(0xFF607D8B))
                    Text(dto.U_CountQty?.toString() ?: "-", fontWeight = FontWeight.Bold)
                }
                Column(horizontalAlignment = androidx.compose.ui.Alignment.CenterHorizontally) {
                    Text("Diferencia", style = MaterialTheme.typography.labelSmall, color = Color(0xFF607D8B))
                    Text(
                        text = dto.U_Difference?.toString() ?: "-",
                        fontWeight = FontWeight.Bold,
                        color = when {
                            dto.U_Difference == null -> Color.Gray
                            dto.U_Difference > 0.0 -> Color(0xFF388E3C) // Verde si es positiva
                            dto.U_Difference < 0.0 -> Color(0xFFD32F2F) // Rojo si es negativa
                            else -> Color.Black
                        }
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
