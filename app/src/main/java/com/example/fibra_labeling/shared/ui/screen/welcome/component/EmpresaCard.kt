package com.example.fibra_labeling.shared.ui.screen.welcome.component

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
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
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.unit.dp

@Composable
fun EmpresaCard(
    nombre: String,
    descripcion: String,
    onClick: () -> Unit,
    backgroundColor: Color,
    textColor: Color,
    icon: Painter? = null,
    modifier: Modifier
) {
    Card(
        modifier = modifier
            .aspectRatio(1f)
            .clickable { onClick() },
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = backgroundColor,
            contentColor = textColor
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            icon?.let {
                Icon(
                    painter = it,
                    contentDescription = null,
                    modifier = Modifier
                        .size(48.dp)
                        .align(Alignment.CenterHorizontally),
                    tint = textColor
                )
                Spacer(modifier = Modifier.height(12.dp))
            }
            Text(
                text = nombre,
                style = MaterialTheme.typography.titleLarge,
                color = textColor
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = descripcion,
                style = MaterialTheme.typography.bodyMedium,
                color = textColor
            )
        }
    }
}



