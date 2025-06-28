package com.example.fibra_labeling.ui.screen.fibra_print.inventario.component

import androidx.compose.foundation.BorderStroke
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
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.fibra_labeling.data.local.entity.fibraprint.POincEntity

@Composable
fun PrintFioriCardConteoCompact(
    dto: POincEntity,
    modifier: Modifier = Modifier,
    onClick: (dto: POincEntity) -> Unit = {},
    onDetailsClick: (docEntry: Int) -> Unit = {},
    onSyncClick: (dto: POincEntity) -> Unit = {},
    isSyncing: Boolean = true, // Nuevo parámetro,
    detailsEnabled: Boolean = true
) {
    // para controlar el estado del botón de envío
    var isSyncPress = remember { mutableStateOf(false) }

    Card(
        enabled = !dto.isSynced,
        modifier = modifier
            .padding(horizontal = 16.dp, vertical = 10.dp)
            .fillMaxWidth(),
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFFF5F8FA)),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
        onClick = {
            if (dto.u_EndTime == null && !isSyncing) {
                onClick(dto)
            }
        },
        border = BorderStroke(width = 1.dp, color = Color(0xFF004990))
    ) {
        Column(Modifier.padding(18.dp)) {
            // Header
            Row(
                Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Absolute.SpaceBetween
            ) {
                FioriBadge(text = dto.u_CountDate.toString())
                if (dto.u_EndTime == null) {
                    FioriBadge(text = "${dto.u_StartTime} - Abierto")
                } else {
                    FioriBadge(text = "${dto.u_StartTime} - ${dto.u_EndTime}")
                }
            }
            Spacer(Modifier.height(8.dp))
            LabelAndValueFiori("Usuario", dto.u_UserNameCount ?: "")
            LabelAndValueFiori("Referencia", dto.u_Ref ?: "")
            LabelAndValueFiori("Observaciones", dto.u_Remarks ?: "")
            LabelAndValueFiori("N° Doc", dto.docEntry.toString())

            // ⬇️ Mostrar el docNumber solo si está sincronizando
            if (dto.isSynced) {
                Spacer(Modifier.height(8.dp))
                LabelAndValueFiori(
                    label = "N° Doc SAP:",
                    value = dto.docNum.toString()
                )
            }

            Row (
                verticalAlignment = Alignment.CenterVertically
            ){
                Spacer(Modifier.weight(1f))

                FilledTonalButton(
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFF004990).copy(alpha = 0.5f)
                    ),
                    onClick = {
                        onDetailsClick(dto.docEntry.toInt())
                    },
                    modifier = Modifier.padding(end = 8.dp),
                    enabled = detailsEnabled
                ) {
                    Text("Detalle")
                }
                if (detailsEnabled){
                    if (!dto.isSynced) {
                        FilledTonalButton(
                            colors = ButtonDefaults.buttonColors(
                                containerColor = Color(0xEE1D7AEE).copy(alpha = 0.8f)
                            ),
                            onClick = {
                                isSyncPress.value=true
                                onSyncClick(dto)
                            },
                            enabled = !isSyncing
                        ) {
                            if (isSyncing) {
                                CircularProgressIndicator(
                                    modifier = Modifier.size(18.dp),
                                    strokeWidth = 2.dp
                                )
                                Spacer(Modifier.width(6.dp))
                                Text("Enviando...")
                            } else {
                                Text("Enviar a Sap")
                            }
                        }
                    } else {
                        Icon(
                            imageVector = Icons.Default.CheckCircle, // Ícono de verificación
                            contentDescription = "Sincronizado",
                            tint = Color(0xBE214CEF), // Verde
                            modifier = Modifier.size(24.dp)
                        )
                    }
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