package com.example.fibra_labeling.shared.ui.screen.setting.printer

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
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
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.fibra_labeling.R
import com.example.fibra_labeling.shared.ui.screen.component.CustomEmptyMessage
import com.example.fibra_labeling.shared.ui.screen.setting.printer.form.isFormValid
import com.example.fibra_labeling.ui.theme.Fibra_labelingTheme
import org.koin.androidx.compose.koinViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PrintSettingScreen(
    onBack: () -> Unit,
    viewModel: PrinterSettingScreenViewModel = koinViewModel(),
    onNavigateTozplTemplate: () -> Unit,
) {
    val formState = viewModel.formState
    val errorState = viewModel.errorState
    val loading = viewModel.loading
    val resultMsg = viewModel.resultMsg

    val zplLabels by viewModel.zplLabels.collectAsState()
    val selectedLabel = zplLabels.firstOrNull { it.selected }

    val snackbarHostState = remember { SnackbarHostState() }
    var expanded by remember { mutableStateOf(false) }

    // SAP Fiori style colors
    val background = Color(0xFFF7F7F7)
    val sectionCard = Color.White
    val accentBlue = Color(0xFF0A6ED1)

    // Mostrar Snackbar solo cuando resultMsg cambia
    LaunchedEffect(resultMsg) {
        resultMsg?.let {
            snackbarHostState.showSnackbar(it)
            viewModel.clearResultMsg()
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                colors = TopAppBarDefaults.topAppBarColors(background),
                title = { Text("Configuración de impresora", color = Color.Black, fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(
                            painterResource(R.drawable.ic_arrow_left),
                            contentDescription = "Back",
                            tint = accentBlue
                        )
                    }
                }
            )
        },
        snackbarHost = { SnackbarHost(snackbarHostState) },
        containerColor = background
    ) { innerPadding ->
        LazyColumn (
            modifier = Modifier
                .fillMaxSize()
                .padding(18.dp)
                .padding(innerPadding),
                //.verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.Top
        ) {
            // Card para toda la sección de datos (estilo Fiori)
            item {
                Card (
                    shape = RoundedCornerShape(18.dp),
                    colors = CardDefaults.cardColors(containerColor = sectionCard),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp)
                ) {
                    Column(Modifier.padding(24.dp)) {
                        Text("Datos de conexión", color = accentBlue, fontWeight = FontWeight.Bold)
                        Spacer(Modifier.height(12.dp))

                        OutlinedTextField(
                            value = formState.printerName,
                            onValueChange = viewModel::onPrinterNameChange,
                            label = { Text("Nombre de la impresora") },
                            isError = errorState.printerNameError != null,
                            singleLine = true,
                            modifier = Modifier.fillMaxWidth(),
                            placeholder = { Text("Ej: Zebra ZT230") }
                        )
                        errorState.printerNameError?.let {
                            Text(it, color = MaterialTheme.colorScheme.error, style = MaterialTheme.typography.labelMedium)
                        }

                        Spacer(Modifier.height(18.dp))

                        OutlinedTextField(
                            value = formState.ip,
                            onValueChange = viewModel::onIpChange,
                            label = { Text("Dirección IP de la impresora") },
                            isError = errorState.ipError != null,
                            singleLine = true,
                            modifier = Modifier.fillMaxWidth(),
                            placeholder = { Text("Ej: 192.168.1.150") }
                        )
                        errorState.ipError?.let {
                            Text(it, color = MaterialTheme.colorScheme.error, style = MaterialTheme.typography.labelMedium)
                        }

                        Spacer(Modifier.height(18.dp))

                        OutlinedTextField(
                            value = formState.port,
                            onValueChange = viewModel::onPortChange,
                            label = { Text("Puerto") },
                            isError = errorState.portError != null,
                            singleLine = true,
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                            modifier = Modifier.fillMaxWidth(),
                            placeholder = { Text("9100") }
                        )
                        errorState.portError?.let {
                            Text(it, color = MaterialTheme.colorScheme.error, style = MaterialTheme.typography.labelMedium)
                        }
                    }
                }
            }

            item {
                Spacer(Modifier.height(18.dp))
            }

            // Card separada para sección de plantilla ZPL (al estilo Fiori)
            item {
                Card(
                    shape = RoundedCornerShape(18.dp),
                    colors = CardDefaults.cardColors(containerColor = sectionCard),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp)
                ) {
                    Column(Modifier.padding(24.dp)) {
                        Text("Plantilla de etiqueta", color = accentBlue, fontWeight = FontWeight.Bold)
                        Spacer(Modifier.height(10.dp))

                        // Combo Fiori style
                        if (zplLabels.isNotEmpty()){
                            ExposedDropdownMenuBox(
                                expanded = expanded,
                                onExpandedChange = { expanded = !expanded }
                            ) {
                                OutlinedTextField(
                                    readOnly = true,
                                    value = selectedLabel?.name ?: "Selecciona una plantilla",
                                    onValueChange = {},
                                    label = { Text("Plantilla ZPL") },
                                    trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded) },
                                    modifier = Modifier
                                        .menuAnchor()
                                        .fillMaxWidth(),
                                    colors = OutlinedTextFieldDefaults.colors(
                                        focusedBorderColor = accentBlue,
                                        unfocusedBorderColor = Color(0xFFB0BEC5),
                                        cursorColor = accentBlue
                                    )
                                )
                                ExposedDropdownMenu(
                                    expanded = expanded,
                                    onDismissRequest = { expanded = false }
                                ) {
                                    zplLabels.forEach { label ->
                                        DropdownMenuItem(
                                            text = { Text(label.name) },
                                            onClick = {
                                                viewModel.onLabelSelected(label.id)
                                                expanded = false
                                            },
                                            contentPadding = ExposedDropdownMenuDefaults.ItemContentPadding
                                        )
                                    }
                                }
                            }
                            // Preview SAP Fiori style
                            selectedLabel?.let {
                                Spacer(Modifier.height(10.dp))
                                Text(
                                    "Vista previa:",
                                    color = Color(0xFF374151),
                                    style = MaterialTheme.typography.labelMedium,
                                    fontWeight = FontWeight.SemiBold
                                )
                                Surface(
                                    color = Color(0xFFF5F7FA),
                                    shape = RoundedCornerShape(12.dp),
                                    tonalElevation = 1.dp,
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(top = 4.dp)
                                ) {
                                    Text(
                                        it.zplFile.take(200) + if (it.zplFile.length > 200) "..." else "",
                                        color = Color(0xFF5E6D7A),
                                        style = MaterialTheme.typography.bodySmall,
                                        modifier = Modifier.padding(12.dp)
                                    )
                                }
                            }
                        }else{
                            Column(
                                horizontalAlignment = Alignment.CenterHorizontally,
                                modifier = Modifier.fillMaxWidth().
                                heightIn(min = 100.dp, max = 250.dp),
                            ) {
                                CustomEmptyMessage(
                                    title = "No hay plantillas configuradas",
                                    message = "Crea una plantilla en la sección de plantillas"
                                )
                                Spacer(Modifier.height(10.dp))
                                // tonal icon button para navegar a la sección de plantillas
                                FilledTonalButton(
                                    onClick = { onNavigateTozplTemplate() },
                                    colors = ButtonDefaults.filledTonalButtonColors(
                                        containerColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.3f),
                                    )
                                ) {
                                    Text("Ir", fontWeight = FontWeight.Bold)
                                }
                            }
                        }

                    }
                }
            }

           item {
               Spacer(Modifier.height(16.dp))
           }

            // Botones SAP Fiori style
            item {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 6.dp),
                    horizontalArrangement = Arrangement.End
                ) {
                    Button(
                        onClick = { viewModel.probarConexion() },
                        enabled = !loading,
                        colors = ButtonDefaults.buttonColors(containerColor = accentBlue)
                    ) { Text("Probar conexión", color = Color.White) }

                    Spacer(Modifier.width(12.dp))

                    Button(
                        onClick = { viewModel.guardar() },
                        enabled = isFormValid(errorState) && !loading,
                        colors = ButtonDefaults.buttonColors(containerColor = accentBlue)
                    ) { Text("Guardar", color = Color.White) }
                }
            }

            item {
                if (loading) {
                    Spacer(Modifier.height(18.dp))
                    LinearProgressIndicator(
                        modifier = Modifier.fillMaxWidth(),
                        color = accentBlue
                    )
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewSetting(){
    Fibra_labelingTheme {
        PrintSettingScreen(
            onBack = {},
            onNavigateTozplTemplate = {}
        )
    }
}