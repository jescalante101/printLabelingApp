package com.example.fibra_labeling.ui.screen.inventory.component

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.fibra_labeling.R
import com.example.fibra_labeling.ui.screen.component.CodigoBarrasImage
import com.example.fibra_labeling.ui.theme.Fibra_labelingTheme

@Composable
fun CustomItemCard(
    modifier: Modifier=Modifier,
    onClick: () -> Unit = {}
){

    Card(
        modifier= modifier.padding(vertical = 1.dp, ),
        shape = MaterialTheme.shapes.small,
        colors = CardDefaults.cardColors(
            //containerColor = MaterialTheme.colorScheme.onBackground,
        ),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 2.dp
        ),
        onClick = {
            onClick()
        }
    ) {
        Column(
            modifier = Modifier.fillMaxWidth().padding(horizontal = 24.dp, vertical = 12.dp)
        ) {
            Text("1")
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
            ) {
                Text(
                    "Esencias vegetales",
                    style = MaterialTheme.typography.titleMedium)
                Text("1850")
            }
            Text("1",
                modifier = Modifier.fillMaxWidth(),
                textAlign = TextAlign.End
            )
            Text("7791234567898")
        }
    }
}

@Composable
fun InfoRow(label: String, value: String) {
    Row(
        Modifier.padding(vertical = 1.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            "$label:",
            style = MaterialTheme.typography.bodySmall.copy(
                color = Color(0xFFB0B0B0),
                fontWeight = FontWeight.Medium
            )
        )
        Spacer(Modifier.width(6.dp))
        Text(
            value,
            style = MaterialTheme.typography.bodySmall.copy(
                color = Color(0xFF222222),
                fontWeight = FontWeight.SemiBold
            )
        )
    }
}

@Composable
fun BorderAccentCardFull(
    producto: ProductoInfo,
    barcodeContent: @Composable () -> Unit,
    accentColor: Color = Color(0xFF0070F2), // SAP Fiori blue
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp, horizontal = 16.dp)
            .clickable(
                enabled = true,
                onClick = onClick
            )
    ) {
        // Border accent
        Box(
            Modifier
                .width(6.dp)
                .fillMaxHeight()
                .background(accentColor, RoundedCornerShape(topStart = 16.dp, bottomStart = 16.dp))
        )
        Spacer(modifier = Modifier.width(8.dp))
        // Card Content
        Card(
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            elevation = CardDefaults.cardElevation(4.dp),
            modifier = Modifier.weight(1f)
        ) {
            Column(
                Modifier
                    .padding(start = 16.dp, top = 16.dp, end = 16.dp, bottom = 16.dp)
            ) {
                // Código y Producto
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Icon(
                        painter = painterResource(R.drawable.ic_packing), // O tu ícono favorito
                        contentDescription = "Código",
                        tint = accentColor,
                        modifier = Modifier.size(20.dp)
                    )
                    Spacer(Modifier.width(8.dp))
                    Text(
                        text = producto.codigo,
                        style = MaterialTheme.typography.titleMedium.copy(
                            fontWeight = FontWeight.Bold,
                            color = accentColor
                        )
                    )
                }
                Spacer(Modifier.height(6.dp))
                InfoRow("Producto", producto.producto)
                InfoRow("Proveedor", producto.proveedor)
                InfoRow("Almacén", producto.almacen)
                InfoRow("Ubicación", producto.ubicacion)
                InfoRow("Fecha", producto.fecha)
                Spacer(Modifier.height(16.dp))
                // Código de barras gráfico
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(Color(0xFFF7F7F7), RoundedCornerShape(10.dp))
                        .padding(8.dp),
                    contentAlignment = Alignment.Center
                ) {
                    barcodeContent()
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    Fibra_labelingTheme {
        Column(modifier = Modifier.fillMaxSize()) {
            BorderAccentCardFull(
                producto = ProductoInfo(
                    codigo = "1234567890",
                    proveedor = "Proveedor A",
                    producto = "Producto 1",
                    almacen = "Almacen 1",
                    ubicacion = "Ubicación 1",
                    fecha = "01/01/2023",
                    barCode = "1234567890123"
                ),
                onClick = {},
                barcodeContent = {
                    CodigoBarrasImage("1234567890123")
                }
            )
        }

    }
}



data class ProductoInfo(
    val codigo: String,
    val proveedor: String,
    val producto: String,
    val almacen: String,
    val ubicacion: String,
    val fecha: String,
    val barCode: String
)