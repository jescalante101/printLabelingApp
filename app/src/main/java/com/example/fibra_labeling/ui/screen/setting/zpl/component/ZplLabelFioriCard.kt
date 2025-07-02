package com.example.fibra_labeling.ui.screen.setting.zpl.component

import androidx.compose.material3.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.clickable
import androidx.compose.foundation.selection.selectable
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

import com.example.fibra_labeling.data.local.entity.fibrafil.ZplLabel

@Composable
fun ZplLabelFioriCard(
    label: ZplLabel,
    onEdit: (ZplLabel) -> Unit,
    onDelete: (ZplLabel) -> Unit,
    modifier: Modifier = Modifier,
    onSelected: (ZplLabel) -> Unit
) {
    Card(
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 6.dp),
        modifier = modifier
            .padding(vertical = 6.dp, horizontal = 12.dp)
            .fillMaxWidth()
            .clickable { onEdit(label) }
            .selectable(
                selected = label.selected,
                onClick = { onSelected(label) }
            )
        ,

    ) {
        Column(
            Modifier
                .padding(18.dp)
                .fillMaxWidth()
        ) {
            Row(
                Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column(Modifier.weight(1f)) {
                    Text(
                        text = label.name,
                        fontWeight = FontWeight.Bold,
                        fontSize = 18.sp,
                        color = Color(0xFF0A6ED1) // Azul SAP Fiori
                    )
                    Spacer(Modifier.height(2.dp))
                    Text(
                        text = "Código: ${label.codeName}",
                        style = MaterialTheme.typography.labelMedium,
                        color = Color(0xFF7A7A7A)
                    )
                }
                Row {
                    IconButton(onClick = { onEdit(label) }) {
                        Icon(
                            imageVector = Icons.Filled.Edit,
                            contentDescription = "Editar",
                            tint = Color(0xFF0070F2)
                        )
                    }
                    IconButton(onClick = { onDelete(label) }) {
                        Icon(
                            imageVector = Icons.Filled.Delete,
                            contentDescription = "Eliminar",
                            tint = Color(0xFFBB2222)
                        )
                    }
                }
            }
            Spacer(Modifier.height(8.dp))
            Text(
                text = label.description,
                color = Color(0xFF444444),
                style = MaterialTheme.typography.bodyMedium,
                maxLines = 2,
            )
            Spacer(Modifier.height(4.dp))
            // Mostrar solo una vista previa del ZPL para no ocupar demasiado
            if (label.zplFile.isNotBlank()) {
                Text(
                    text = label.zplFile.take(80) + if (label.zplFile.length > 80) "..." else "",
                    color = Color(0xFF5E6D7A),
                    style = MaterialTheme.typography.bodySmall,
                    fontFamily = androidx.compose.ui.text.font.FontFamily.Monospace,
                    lineHeight = 16.sp
                )
            }
        }
    }
}


@Preview(showBackground = true)
@Composable
fun PreviewCardZpl(){
    ZplLabelFioriCard(
        label = ZplLabel(
            id = 1,
            codeName = "bdbadbasbdsabjkd",
            name = "Nombre de la etiqueta",
            description = "Descripción de la etiqueta",
            zplFile = "Código ZPL de la etiqueta"
        ),
        onEdit = {},
        onDelete = {},
        onSelected = {}
    )
}