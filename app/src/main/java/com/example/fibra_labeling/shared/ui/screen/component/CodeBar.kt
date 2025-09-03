package com.example.fibra_labeling.shared.ui.screen.component

import android.annotation.SuppressLint
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.asImageBitmap
import com.example.fibra_labeling.fibraprint.ui.screen.etiqueta.util.generarCodigoBarras

@SuppressLint("UnusedBoxWithConstraintsScope")
@Composable
fun CodigoBarrasImage(data: String) {
    BoxWithConstraints(modifier = Modifier.fillMaxWidth()) {
        val width = constraints.maxWidth
        val height = 75
        val bitmap = generarCodigoBarras(data, width, height)
        Column(
            modifier = Modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Image(bitmap = bitmap.asImageBitmap(), contentDescription = "Código de barras", modifier = Modifier.fillMaxWidth())
            Text(data, style = MaterialTheme.typography.bodySmall)
        }
    }
}