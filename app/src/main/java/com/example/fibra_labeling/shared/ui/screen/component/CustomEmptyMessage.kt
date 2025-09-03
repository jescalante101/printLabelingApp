package com.example.fibra_labeling.shared.ui.screen.component

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.fibra_labeling.ui.theme.Fibra_labelingTheme

@Composable
fun CustomEmptyMessage(
    title: String = "Sin resultados",
    message: String = "No se encontraron resultados para la búsqueda"
){
    Column(
        Modifier
            .fillMaxWidth()
            .padding(top = 120.dp, end = 20.dp, start = 20.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            title,
            color = Color(0xFF8A9299),
            style = MaterialTheme.typography.titleMedium
        )
        Spacer(Modifier.height(8.dp))
        Text(
            message,
            color = Color(0xFFA2B1C0),
            style = MaterialTheme.typography.bodyMedium
        )
    }
}


@Preview(showBackground = true)
@Composable
fun CustomPreview(){

    Fibra_labelingTheme {
        CustomEmptyMessage()
    }
}