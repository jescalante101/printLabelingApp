import android.widget.Button
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
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FilledIconButton
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.fibra_labeling.data.local.entity.fibrafil.FibOincEntity
import com.example.fibra_labeling.data.remote.FibOinResquet
import com.example.fibra_labeling.ui.theme.Fibra_labelingTheme

@Composable
fun FioriCardConteoCompact(
    dto: FibOincEntity,
    modifier: Modifier = Modifier,
    onClick: (dto: FibOincEntity) -> Unit = {},
    onDetailsClick: (docEntry: Int) -> Unit = {}
) {
    Card(
        modifier = modifier
            .padding(horizontal = 16.dp, vertical = 10.dp)
            .fillMaxWidth(),
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFFF5F8FA)),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
        onClick = {
            if (dto.u_EndTime == null) {
                onClick(dto)
            }
        }
    ) {
        Column(Modifier.padding(18.dp)) {
            // 1. Header con badges
            Row (
                Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Absolute.SpaceBetween
            ) {
                FioriBadge(text = dto.u_CountDate.toString())
                if (dto.u_EndTime == null) {
                    FioriBadge(text = "${dto.u_StartTime} - Abierto")
                }else{
                    FioriBadge(text = "${dto.u_StartTime} - ${dto.u_EndTime}")
                }

            }
            Spacer(Modifier.height(8.dp))
            // 2. Usuario
            LabelAndValueFiori("Usuario", dto.u_UserNameCount?:"")
            // 3. Referencia
            LabelAndValueFiori("Referencia", dto.u_Ref ?:"")
            // 4. Observaciones
            LabelAndValueFiori("Observaciones", dto.u_Remarks ?: "")
            //
            Row {
                Spacer(Modifier.weight(1f))
                FilledTonalButton(
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFF004990).copy(alpha = 0.5f)
                    ),
                    onClick = {
                        onDetailsClick(dto.docEntry.toInt())
                    },
                    modifier = Modifier.padding(end = 8.dp)
                ) {
                    Text("Detalle")
                }
            }
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
            dto = FibOincEntity(
                u_CountDate = "2023-08-01",
                u_userCodeCount = "Juan Pérez",
                u_StartTime = "08:00:00",
                u_EndTime = "16:00:00",
                u_UserNameCount = "Ref123",
                u_Remarks = "Observaciones adicionales",
                u_Ref = ""
            )
        )
    }
}
