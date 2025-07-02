import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.material3.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.fibra_labeling.ui.screen.setting.zpl.ZplTemplateViewModel
import com.example.fibra_labeling.ui.screen.setting.zpl.component.ZplLabelFioriCard
import org.koin.androidx.compose.koinViewModel
import com.example.fibra_labeling.R
import com.example.fibra_labeling.data.local.entity.fibrafil.ZplLabel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ZplTemplateScreen(
    viewModel: ZplTemplateViewModel = koinViewModel(),
    onBack: () -> Unit,
) {
    val zplLabels = viewModel.labels.collectAsState().value // Tu lista real aquí

    // SAP Fiori style colors
    val background = Color(0xFFF7F7F7)
    val sectionCard = Color.White
    val accentBlue = Color(0xFF0A6ED1)


    var showSheet by remember { mutableStateOf(false) }
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)

    var labelToEdit by remember { mutableStateOf<ZplLabel?>(null) }

    val onFabClick = {
        labelToEdit = null
        showSheet = true
    }

    val onEditClick: (ZplLabel) -> Unit = { label ->
        labelToEdit = label
        showSheet = true
    }


    Scaffold(
        containerColor = background,
        topBar = {
            CenterAlignedTopAppBar(
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(
                            painterResource(com.example.fibra_labeling.R.drawable.ic_arrow_left),
                            contentDescription = "Back",
                            tint = accentBlue
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(background),
                title = { Text("Configuración de impresora", color = Color.Black, fontWeight = FontWeight.Bold) },
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = {
                    onFabClick()
                },
                shape = RoundedCornerShape(14.dp),
                containerColor = Color(0xFF0070F2),
                contentColor = Color.White
            ) {
                Icon(Icons.Default.Add, contentDescription = "Nueva plantilla")
            }
        }

    ) { padding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
        ) {
            if (zplLabels.isEmpty()) {
                // Mensaje SAP Fiori de "sin datos"
                Column(
                    Modifier
                        .fillMaxWidth()
                        .padding(top = 120.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        "No hay plantillas registradas.",
                        color = Color(0xFF8A9299),
                        style = MaterialTheme.typography.titleMedium
                    )
                    Spacer(Modifier.height(8.dp))
                    Text(
                        "Presiona el botón '+' para agregar una nueva.",
                        color = Color(0xFFA2B1C0),
                        style = MaterialTheme.typography.bodyMedium
                    )
                }
            } else {
                LazyColumn(
                    contentPadding = PaddingValues(
                        top = 18.dp,
                        bottom = 86.dp,
                        start = 8.dp,
                        end = 8.dp
                    ),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    items(
                        items = zplLabels,
                        key = { it.id } // Importante para animación fluida
                    ) { label ->
                        // Animación de visibilidad para cada item
                        AnimatedVisibility (
                            visible = true,
                            enter = fadeIn(animationSpec = tween(420)) + slideInVertically(
                                animationSpec = tween(400),
                                initialOffsetY = { fullHeight -> fullHeight / 2 }
                            ),
                            exit = fadeOut(animationSpec = tween(300)) + slideOutVertically(
                                animationSpec = tween(280),
                                targetOffsetY = { fullHeight -> fullHeight / 2 }
                            ),
                        ) {
                            // Animación de reposicionamiento suave si reordenas (opcional)
                            Box(Modifier.animateItem()) {
                                ZplLabelFioriCard(
                                    label = label,
                                    onEdit = { onEditClick(label) },
                                    onDelete = {
                                        viewModel.deleteLabel(label)
                                        labelToEdit = null
                                    },
                                    onSelected = {
                                        viewModel.setSelectedLabel(label)
                                        onBack()
                                    }
                                )
                            }
                        }
                    }
                }
            }
        }
    }

    // Aquí el bottomsheet, sirve para registrar y editar
    if (showSheet) {
        ModalBottomSheet(
            onDismissRequest = { showSheet = false },
            sheetState = sheetState,
            dragHandle = { BottomSheetDefaults.DragHandle() }
        ) {
            ZplRegister(
                label = labelToEdit, // si es null, es nuevo; si no, es editar
                onSave = { label ->
                    if (labelToEdit == null) {
                        viewModel.addLabel(label)
                    } else {
                        viewModel.updateLabel(label)
                    }
                    showSheet = false
                },
                onCancel = {
                    showSheet = false
                }
            )
        }
    }
}
