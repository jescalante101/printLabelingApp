package com.example.fibra_labeling.fibrafil.ui.screen.home.component

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun TopSnackbar(message: String, show: Boolean, onDismiss: () -> Unit) {
    Box(modifier = Modifier.fillMaxSize()) {
        // Top Snackbar
        AnimatedVisibility (
            visible = show,
            enter = slideInVertically(initialOffsetY = { -it }),
            exit = slideOutVertically(targetOffsetY = { -it })
        ) {
            Card (
                modifier = Modifier
                    .align(Alignment.TopCenter)
                    .padding(top = 32.dp, start = 16.dp, end = 16.dp),
                elevation = CardDefaults.elevatedCardElevation(8.dp),
                //backgroundColor = Color(0xFF323232)
                colors = CardDefaults.cardColors(
                    containerColor = Color(0xFF323232)
                )
            ) {
                Row(
                    modifier = Modifier
                        .padding(16.dp)
                        .fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = message,
                        color = Color.White,
                        modifier = Modifier.weight(1f)
                    )
                    IconButton (onClick = onDismiss) {
                        Icon(Icons.Default.Close, contentDescription = "Cerrar", tint = Color.White)
                    }
                }
            }
        }
        // ... aquí va el resto del contenido de tu pantalla ...
    }
}
