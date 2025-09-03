package com.example.fibra_labeling.fibrafil.ui.screen.reception.registerreception.register.component

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ProgressIndicatorDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.fibra_labeling.fibrafil.ui.screen.reception.registerreception.register.ReceptionFibrafilViewModel

@Composable
fun SummaryBar(viewModel: ReceptionFibrafilViewModel){
    val total by viewModel.totalCount.collectAsState(0)
    val checked by viewModel.checkedCount.collectAsState(0)
    val discrepancies by viewModel.discrepancyCount.collectAsState(0)

    val progress by animateFloatAsState(targetValue = if (total > 0) checked.toFloat() / total.toFloat() else 0f, label = "progress")

    Surface (
        color = MaterialTheme.colorScheme.surface.copy(alpha = 0.8f),
        shadowElevation = 2.dp
    ) {
        Column (Modifier.padding(12.dp)) {
            Row (Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                Text("Progreso de Verificación", style = MaterialTheme.typography.bodyMedium, fontWeight = FontWeight.Bold)
                Text("$checked de $total", style = MaterialTheme.typography.bodyMedium)
            }
            Spacer(Modifier.height(8.dp))
            LinearProgressIndicator(
            progress = { progress },
            modifier = Modifier
                                .fillMaxWidth()
                                .height(8.dp)
                                .clip(CircleShape),
            color = MaterialTheme.colorScheme.primary,
            trackColor = Color.LightGray.copy(alpha = 0.4f),
            strokeCap = ProgressIndicatorDefaults.LinearStrokeCap,
            )
            AnimatedVisibility(visible = discrepancies > 0) {
                Text(
                    "⚠️ $discrepancies artículos con discrepancia",
                    color = MaterialTheme.colorScheme.error,
                    fontWeight = FontWeight.Bold,
                    style = MaterialTheme.typography.bodySmall,
                    modifier = Modifier.padding(top = 8.dp).fillMaxWidth(),
                    textAlign = TextAlign.Center
                )
            }
        }
    }
}