package com.example.fibra_labeling.ui.screen.home.component

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.Alignment
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CustomBottomSheetMenu(
    onFibraFilClick: () -> Unit,
    onFibraPrintClick: () -> Unit,
    showSheet: Boolean,
    onDismiss: () -> Unit
) {
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    val scope = rememberCoroutineScope()

    if (showSheet) {
        ModalBottomSheet(
            onDismissRequest = onDismiss,
            sheetState = sheetState
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text("Selecciona empresa", style = MaterialTheme.typography.titleMedium)
                Spacer(Modifier.height(24.dp))
                Button(
                    onClick = {
                        onFibraFilClick()
                        scope.launch { sheetState.hide(); onDismiss() }
                    },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("FibraFill")
                }
                Spacer(Modifier.height(16.dp))
                Button(
                    onClick = {
                        onFibraPrintClick()
                        scope.launch { sheetState.hide(); onDismiss() }
                    },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("FibraPrint")
                }
                Spacer(Modifier.height(16.dp))
            }
        }
    }
}
