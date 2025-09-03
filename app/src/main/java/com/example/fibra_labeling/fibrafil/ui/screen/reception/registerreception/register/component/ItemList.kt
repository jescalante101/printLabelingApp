package com.example.fibra_labeling.fibrafil.ui.screen.reception.registerreception.register.component

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.fibra_labeling.R
import com.example.fibra_labeling.fibrafil.ui.screen.reception.registerreception.model.ItemStatus
import com.example.fibra_labeling.fibrafil.ui.screen.reception.registerreception.model.OrderItem
import com.example.fibra_labeling.ui.theme.FioriSuccessText
import com.example.fibra_labeling.ui.theme.FioriWarningBg
import com.example.fibra_labeling.ui.theme.FioriWarningText

@Composable
fun ItemList(
    items: List<OrderItem>,
    onQuantityChange: (String, Int) -> Unit,
    onVerify: (String) -> Unit,
){
    LazyColumn(
        contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        items(
             items = items,
             key = { it.id }
        ){
            OrderItemCard(
                item = it,
                onQuantityChange = { newQuantity ->onQuantityChange(it.id, newQuantity)},
                onVerify = {
                    onVerify(it.id)
                },
                modifier = Modifier.animateItem()
            )
        }
    }
}


@Composable
fun OrderItemCard(
    item: OrderItem,
    onQuantityChange: (Int) -> Unit,
    onVerify: () -> Unit,
    modifier: Modifier = Modifier
){
    val cardColors = when (item.status.value) {
        ItemStatus.PENDING -> CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        )
        ItemStatus.VERIFIED -> CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.primary
        )
        ItemStatus.DISCREPANCY -> CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.error
        )
    }

    val borderColor = when (item.status.value){
        ItemStatus.PENDING -> MaterialTheme.colorScheme.primary
        ItemStatus.VERIFIED -> MaterialTheme.colorScheme.secondary
        ItemStatus.DISCREPANCY -> MaterialTheme.colorScheme.error
    }

    Card(
        modifier = modifier
            .fillMaxWidth()
            .border(1.dp,borderColor, RoundedCornerShape(16.dp)),
        colors = cardColors,
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 2.dp
        )
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Row(
                modifier= Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.Top,
                horizontalArrangement = Arrangement.SpaceBetween
            ){
                Column {
                    Text(item.id, fontWeight = FontWeight.Bold, style = MaterialTheme.typography.titleMedium)
                    Text(item.desc, style = MaterialTheme.typography.bodyLarge)
                }
                StatusBadge(
                    status = item.status.value
                )
            }
            HorizontalDivider(Modifier.padding(vertical=12.dp))
            Row(
                Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column {
                    Text("Pedido:", style = MaterialTheme.typography.labelMedium, color = Color.Gray)
                    Text(item.ordered.toString(), style = MaterialTheme.typography.headlineSmall, fontWeight = FontWeight.Bold)
                }
                QuantitySelector(
                    quantity = item.received.value,
                    onQuantityChange = onQuantityChange
                )
            }
            Spacer(Modifier.height(16.dp))
            Row(Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.SpaceBetween) {
                Text("Almacén: ${item.wh}", style = MaterialTheme.typography.bodySmall, color = Color.Gray)
                AnimatedVisibility (visible = item.status.value == ItemStatus.PENDING) {
                    Button (onClick = onVerify,
                        colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary)) {
                        Icon(Icons.Default.Check, "Verificar", modifier = Modifier.size(18.dp))
                        Spacer(Modifier.width(8.dp))
                        Text("Verificar")
                    }
                }
            }
        }
    }

}

@Composable
fun QuantitySelector(quantity: Int, onQuantityChange: (Int) -> Unit) {
    Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(12.dp)) {
        IconButton (
            onClick = { if (quantity > 0) onQuantityChange(quantity - 1) },
            modifier = Modifier.clip(CircleShape).background(Color.LightGray.copy(alpha=0.5f))
        ) {
            Icon(
                painter = painterResource(R.drawable.ic_close),
                "Decrementar"
            )
        }

        OutlinedTextField(
            value = quantity.toString(),
            onValueChange = { newValue -> onQuantityChange(newValue.toIntOrNull() ?: 0) },
            modifier = Modifier.width(80.dp),
            textStyle = LocalTextStyle.current.copy(textAlign = TextAlign.Center, fontSize = 20.sp, fontWeight = FontWeight.Bold),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            singleLine = true
        )

        IconButton(
            onClick = { onQuantityChange(quantity + 1) },
            modifier = Modifier.clip(CircleShape).background(Color.LightGray.copy(alpha=0.5f))
        ) {
            Icon(
                painter = painterResource(R.drawable.ic_plus),
                "Incrementar")
        }
    }
}

@Composable
fun StatusBadge(
    status: ItemStatus,
){
    val (text, bgColor, textColor) = when (status) {
        ItemStatus.VERIFIED -> Triple("✅ Verificado", FioriSuccessText, Color.White)
        ItemStatus.DISCREPANCY -> Triple("⚠️ Discrepancia", FioriWarningBg, FioriWarningText)
        ItemStatus.PENDING -> Triple(null, null, null)
    }

    AnimatedVisibility(visible = text != null) {
        if (text != null && bgColor != null && textColor != null) {
            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(12.dp))
                    .background(bgColor)
                    .border(1.dp, textColor, RoundedCornerShape(12.dp))
                    .padding(horizontal = 8.dp, vertical = 4.dp)
            ) {
                Text(text, color = textColor, style = MaterialTheme.typography.labelSmall, fontWeight = FontWeight.Bold)
            }
        }
    }
}