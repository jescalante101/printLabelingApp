package com.example.fibra_labeling.ui.screen.fibrafil.inventario.component

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
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
import com.example.fibra_labeling.data.model.ProductoDetalleUi
import com.example.fibra_labeling.ui.screen.component.CodigoBarrasImage
import com.example.fibra_labeling.ui.screen.print.component.GroupRowFiori

@Composable
fun ProductoDetalleCard(
    item: ProductoDetalleUi,
    modifier: Modifier = Modifier
) {
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
            // Header: Nombre del producto
            Text(
                text = item.productoName,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = Color.Black
            )
            Spacer(Modifier.height(8.dp))

            // Código
            GroupRowFiori("Código", item.codigo)

            // Referencia
            GroupRowFiori("Ubicación", item.ubicacion ?: "-")

            // Referencia
            GroupRowFiori("Referencia", item.referencia ?: "-")

            // Máquina
            GroupRowFiori("Máquina", item.maquina ?: "-")

            // Lote
            GroupRowFiori("Lote", item.lote?: "-")

            Spacer(Modifier.height(16.dp))
            HorizontalDivider(Modifier.padding(vertical = 8.dp))

            // Código de barras visual
            Row(verticalAlignment = Alignment.CenterVertically) {
                CodigoBarrasImage(item.codBar)

            }
        }
    }
}

@Preview
@Composable
fun PreviewProductoDetalleCard() {
    ProductoDetalleCard(
        item = ProductoDetalleUi(
            codigo = "12345",
            productoName = "Lámina PVC",
            lote = "L20240612",
            referencia = "REF-ABC-01",
            maquina = "Cortadora A",
            codBar = "1234567890123",
            ubicacion = "Estante A"
        )
    )
}
