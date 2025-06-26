import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.SwipeToDismissBox
import androidx.compose.material3.SwipeToDismissBoxValue
import androidx.compose.material3.Text
import androidx.compose.material3.rememberSwipeToDismissBoxState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.key
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.fibra_labeling.data.local.entity.fibrafil.FibIncEntity
import kotlinx.coroutines.launch


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SwipeableItem(
    item: FibIncEntity,
    onDelete: (FibIncEntity) -> Unit,
    content: @Composable () -> Unit
) {
    // Forzar recomposición de SwipeToDismissBox después de cancelar/eliminar
    var resetKey by remember { mutableIntStateOf(0) }

    // El key incluye el id del item y un contador de reset
    val itemKey = "${item.id}_$resetKey"

    key (itemKey) {
        val state = rememberSwipeToDismissBoxState(
            positionalThreshold = { it * 0.3f },
            confirmValueChange = { value ->
                // Solo permitimos swipe si NO está sincronizado y el swipe es EndToStart
                if (!item.isSynced && value == SwipeToDismissBoxValue.EndToStart) {
                    true
                } else {
                    false
                }
            }
        )
        val scope = rememberCoroutineScope()

        SwipeToDismissBox(
            state = state,
            backgroundContent = {
                if (state.targetValue == SwipeToDismissBoxValue.EndToStart && !item.isSynced) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier
                            .fillMaxSize()
                            .background(Color.Red.copy(0.5f))
                            .padding(end = 12.dp)
                            .wrapContentWidth(Alignment.End)
                    ) {
                        Button(
                            onClick = {
                                scope.launch { state.reset() }
                                // Aumenta el key, forzando recomposición y reinicio del Swipe
                                resetKey++
                            },
                            colors = ButtonDefaults.buttonColors(
                                containerColor = Color.DarkGray,
                                contentColor = Color.White
                            ),
                            modifier = Modifier.padding(end = 8.dp)
                        ) {
                            Text("Cancelar")
                        }
                        Button(
                            onClick = {
                                onDelete(item)
                                scope.launch { state.reset() }
                                resetKey++
                            },
                            colors = ButtonDefaults.buttonColors(
                                containerColor = Color.Red,
                                contentColor = Color.White
                            ),
                        ) {
                            Text("Eliminar")
                        }
                    }
                }
            },
            modifier = Modifier.padding(vertical = 2.dp)
        ) {
            content()
        }
    }
}

