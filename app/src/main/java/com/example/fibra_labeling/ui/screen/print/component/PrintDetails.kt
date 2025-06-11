package com.example.fibra_labeling.ui.screen.print.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.fibra_labeling.data.model.ImobPasaje
import com.example.fibra_labeling.ui.screen.component.CodigoBarrasImage

@Composable
fun SapFioriDetailCard(item: ImobPasaje, modifier: Modifier = Modifier) {
    Card (
        modifier = modifier
            .padding(16.dp)
            .fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 2.dp,
            pressedElevation = 6.dp
        )
    ) {
        Column (modifier = Modifier.padding(20.dp)) {
            // Header: Título y Estado
            Row (
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = item.name,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = Color.Black
                )
                Spacer(Modifier.weight(1f))
                if (item.status.toString()!="null"){
                    StatusBadgeFiori(item.status.toString())
                }
                //StatusBadgeFiori(item.status.toString())
            }
            Spacer(Modifier.height(8.dp))
            Text(
                text = "Código: ${item.itemCode}",
                color = Color(0xFF425563),
                style = MaterialTheme.typography.bodyMedium
            )
            Text(
                text = "Lote: ${item.lote}",
                color = Color(0xFF425563),
                style = MaterialTheme.typography.bodyMedium
            )
            HorizontalDivider(Modifier.padding(vertical = 12.dp))
            // Info principal agrupada
            GroupRowFiori("Proveedor", item.proveedor)
            GroupRowFiori("Almacén", item.almacen)
            GroupRowFiori("Motivo", item.motivo)
            GroupRowFiori("Ubicación", item.ubicacion)
            GroupRowFiori("Piso", item.piso)
            HorizontalDivider(Modifier.padding(vertical = 12.dp))
            // Medidas
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                GroupInfoFiori("Metro Lineal", item.metroLineal)
                GroupInfoFiori("Equivalente", item.equivalente)
                GroupInfoFiori("Peso", "${item.peso ?: 0.0} kg")
            }
            HorizontalDivider(Modifier.padding(vertical = 12.dp))
            // Auditoría
            GroupRowFiori("Creado por", item.userCreate)
            GroupRowFiori("Fecha creación", item.createDate)
            item.userUpdate?.let {
                GroupRowFiori("Actualizado por", it)
            }
            item.updateDate?.let {
                GroupRowFiori("Fecha actualización", it)
            }
            Spacer(Modifier.height(12.dp))
            // Código de barras visual

            Row {
                CodigoBarrasImage(item.codeBar)
            }

//            Row(verticalAlignment = Alignment.CenterVertically) {
//                Icon(painter = painterResource(R.drawable.barcode_scan), contentDescription = "Código de barras", tint = Color(0xFF425563))
//                Spacer(Modifier.width(8.dp))
//                Text(item.codeBar, style = MaterialTheme.typography.bodyMedium,color = Color(0xFF425563))
//            }
        }
    }
}

@Composable
fun StatusBadgeFiori(status: String) {
    val (color, label) = when (status.lowercase()) {
        "activo" -> Color(0xFF009C43) to "Activo"
        "inactivo" -> Color(0xFFF44336) to "Inactivo"
        else -> Color(0xFF607D8B) to status
    }
    Box(
        Modifier
            .background(color, shape = RoundedCornerShape(8.dp))
            .padding(horizontal = 12.dp, vertical = 4.dp)
    ) {
        Text(label, color = Color.White, style = MaterialTheme.typography.labelMedium)
    }
}

@Composable
fun GroupRowFiori(label: String, value: String) {
    Row(
        Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(label, color = Color(0xFF425563), fontWeight = FontWeight.SemiBold, style = MaterialTheme.typography.bodySmall)
        Text(value, color = Color.Black, fontWeight = FontWeight.Normal, style = MaterialTheme.typography.bodySmall)
    }
}

@Composable
fun GroupInfoFiori(label: String, value: String) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(label, color = Color(0xFF607D8B), fontSize = 12.sp)
        Text(value, fontWeight = FontWeight.Bold, fontSize = 15.sp, color = Color.Black)
    }
}



@Preview
@Composable
fun PreviewFiori(){
    SapFioriDetailCard(
        item = ImobPasaje(
            itemCode = "12345",
            almacen = "dd",
            lote = "dd",
            name = "dd",
            proveedor = "dd",
            motivo = "dd",
            ubicacion = "Lorem",
            piso = "dd",
            metroLineal = "dd",
            equivalente = "dd",
            peso = 1.0,
            codeBar = "dd",
            userCreate = "dd",
            createDate = "dd",
            status = "dd",
            userUpdate = "dd",
            updateDate = "dd",
            docEntry = 0
        )
    )
}
