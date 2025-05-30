package com.example.fibra_labeling.ui.screen.inventory.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
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
            containerColor = MaterialTheme.colorScheme.onBackground,
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

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    Fibra_labelingTheme {
        CustomItemCard(
        )
    }
}