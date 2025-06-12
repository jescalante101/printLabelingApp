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
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.fibra_labeling.data.remote.FibOinResquet
import com.example.fibra_labeling.ui.theme.Fibra_labelingTheme

@Composable
fun FioriCardConteoCompact(
    dto: FibOinResquet,
    modifier: Modifier = Modifier,
    onClick: (dto: FibOinResquet) -> Unit = {}
) {
    Card(
        modifier = modifier
            .padding(horizontal = 16.dp, vertical = 10.dp)
            .fillMaxWidth(),
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFFF5F8FA)),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
        onClick = {
            onClick(dto)
        }
    ) {
        Column(Modifier.padding(18.dp)) {
            // 1. Header con badges
            Row (
                Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Absolute.SpaceBetween
            ) {
                FioriBadge(text = dto.countDate)
                FioriBadge(text = "${dto.startTime} - ${dto.endTime}")

            }
            Spacer(Modifier.height(8.dp))
            // 2. Usuario
            LabelAndValueFiori("Usuario", dto.usuario)
            // 3. Referencia
            LabelAndValueFiori("Referencia", dto.referencia)
            // 4. Observaciones
            LabelAndValueFiori("Observaciones", dto.remarks)
        }
    }
}

@Composable
private fun FioriBadge(text: String) {
    Box(
        Modifier
            .background(color = Color(0xFF004990).copy(alpha = 0.08f), shape = RoundedCornerShape(8.dp))
            .padding(horizontal = 10.dp, vertical = 4.dp)
    ) {
        Text(
            text = text,
            style = MaterialTheme.typography.labelSmall,
            color = Color(0xFF004990)
        )
    }
}

@Composable
private fun LabelAndValueFiori(label: String, value: String) {
    Text(
        text = label,
        color = Color(0xFF607D8B),
        style = MaterialTheme.typography.labelSmall
    )
    Text(
        text = value,
        style = MaterialTheme.typography.bodyLarge,
        fontWeight = FontWeight.Bold,
        color = Color.Black,
        modifier = Modifier.padding(bottom = 8.dp)
    )
}


@Preview(showBackground = true)
@Composable
fun previewCard(){
    Fibra_labelingTheme{
        FioriCardConteoCompact(
            dto = FibOinResquet(
                countDate = "2023-08-01",
                usuario = "Juan Pérez",
                startTime = "08:00:00",
                endTime = "16:00:00",
                referencia = "Ref123",
                remarks = "Observaciones adicionales"
            )
        )
    }
}
