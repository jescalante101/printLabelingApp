package com.example.fibra_labeling.ui.screen.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.SwipeToDismissBox
import androidx.compose.material3.SwipeToDismissBoxState
import androidx.compose.material3.SwipeToDismissBoxValue
import androidx.compose.material3.Text
import androidx.compose.material3.rememberSwipeToDismissBoxState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.key
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.launch


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun <T> CustomSwipeableItem(
    item: T,
    itemId: Any, // Identificador único del item (puede ser String, Int, Long, etc.)
    canSwipe: (T) -> Boolean = { true }, // Función para determinar si se puede hacer swipe
    onDelete: (T) -> Unit,
    cancelButtonText: String = "Cancelar",
    deleteButtonText: String = "Eliminar",
    backgroundContent: @Composable ((T, SwipeToDismissBoxState, () -> Unit, () -> Unit) -> Unit)? = null,
    content: @Composable () -> Unit
) {
    // Forzar recomposición de SwipeToDismissBox después de cancelar/eliminar
    var resetKey by remember { mutableIntStateOf(0) }

    // El key incluye el id del item y un contador de reset
    val itemKey = "${itemId}_$resetKey"

    key(itemKey) {
        val state = rememberSwipeToDismissBoxState (
            positionalThreshold = { it * 0.3f },
            confirmValueChange = { value ->
                // Solo permitimos swipe si la condición se cumple y el swipe es EndToStart
                canSwipe(item) && value == SwipeToDismissBoxValue.EndToStart
            }
        )
        val scope = rememberCoroutineScope ()

        val onCancel: () -> Unit = {
            scope.launch { state.reset() }
            resetKey++
        }

        val onDeleteAction: () -> Unit = {
            onDelete(item)
            scope.launch { state.reset() }
            resetKey++
        }

        SwipeToDismissBox(
            state = state,
            backgroundContent = {
                // Si se proporciona backgroundContent personalizado, usarlo
                if (backgroundContent != null) {
                    backgroundContent(item, state, onCancel, onDeleteAction)
                } else {
                    // Background por defecto
                    if (state.targetValue == SwipeToDismissBoxValue.EndToStart && canSwipe(item)) {
                        DefaultSwipeBackground(
                            onCancel = onCancel,
                            onDelete = onDeleteAction,
                            cancelText = cancelButtonText,
                            deleteText = deleteButtonText
                        )
                    }
                }
            },
            modifier = Modifier.padding(vertical = 2.dp)
        ) {
            content()
        }
    }
}

@Composable
private fun DefaultSwipeBackground(
    onCancel: () -> Unit,
    onDelete: () -> Unit,
    cancelText: String,
    deleteText: String
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Red.copy(0.5f))
            .padding(end = 12.dp)
            .wrapContentWidth(Alignment.End)
    ) {
        Button (
            onClick = onCancel,
            colors = ButtonDefaults.buttonColors(
                containerColor = Color.DarkGray,
                contentColor = Color.White
            ),
            modifier = Modifier.padding(end = 8.dp)
        ) {
            Text(cancelText)
        }
        Button(
            onClick = onDelete,
            colors = ButtonDefaults.buttonColors(
                containerColor = Color.Red,
                contentColor = Color.White
            ),
        ) {
            Text(deleteText)
        }
    }
}