package com.example.fibra_labeling.shared.ui.screen.setting.general

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.fibra_labeling.R
import com.example.fibra_labeling.datastore.ThemeManager
import com.example.fibra_labeling.datastore.ThemeMode
import com.example.fibra_labeling.ui.theme.Fibra_labelingTheme
import org.koin.androidx.compose.koinViewModel
import org.koin.compose.getKoin

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GeneralSettingScreen(
    onBack: () -> Unit,
    viewModel: GeneralSettingScreenViewModel = koinViewModel(),
    themeManager: ThemeManager = getKoin().get()
) {
    val snackbarHostState = remember { SnackbarHostState() }
    var themeExpanded by remember { mutableStateOf(false) }
    val useWidgetCounter by viewModel.conteoMode.collectAsState()

    val currentTheme by themeManager.themeMode
    val themes = listOf(ThemeMode.LIGHT, ThemeMode.DARK, ThemeMode.SYSTEM)

    // SAP Fiori style colors
    val background = Color(0xFFF7F7F7)
    val sectionCard = Color.White
    val accentBlue = Color(0xFF0A6ED1)




    Scaffold(
        topBar = {
            TopAppBar(
                colors = TopAppBarDefaults.topAppBarColors(background),
                title = { Text("Configuración general", color = Color.Black, fontWeight = FontWeight.Bold) },
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
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(18.dp)
                .padding(innerPadding),
            verticalArrangement = Arrangement.Top
        ) {
            // Header con información de la empresa
            item {
                Card(
                    shape = RoundedCornerShape(18.dp),
                    colors = CardDefaults.cardColors(containerColor = sectionCard),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp)
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(24.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        // Icono de la empresa (puedes cambiar por un logo real)
                        Icon(
                            painter = painterResource(R.drawable.ic_logofibra_print), // Cambia por tu logo
                            contentDescription = "FibraPrint Logo",
                            modifier = Modifier.size(48.dp)
                        )

                        Spacer(Modifier.height(12.dp))

                        Text(
                            text = "FibraPrint",
                            style = MaterialTheme.typography.headlineSmall,
                            fontWeight = FontWeight.Bold,
                            textAlign = TextAlign.Center
                        )

                        Text(
                            text = "Sistema de etiquetado y gestión de inventario",
                            style = MaterialTheme.typography.bodyMedium,
                            color = Color(0xFF6C757D),
                            textAlign = TextAlign.Center
                        )
                    }
                }
            }

            item {
                Spacer(Modifier.height(18.dp))
            }

            // Card para configuración de conteo
            item {
                Card(
                    shape = RoundedCornerShape(18.dp),
                    colors = CardDefaults.cardColors(containerColor = sectionCard),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp)
                ) {
                    Column(Modifier.padding(24.dp)) {
                        Text("Método de conteo", color = accentBlue, fontWeight = FontWeight.Bold)
                        Spacer(Modifier.height(12.dp))

                        // Switch para método de conteo
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column(modifier = Modifier.weight(1f)) {
                                Text(
                                    text = if (useWidgetCounter) "Usar widget contador" else "Usar teclado",
                                    style = MaterialTheme.typography.bodyMedium,
                                    fontWeight = FontWeight.Medium
                                )
                                Text(
                                    text = if (useWidgetCounter)
                                        "Utilizar otra interfaz para contar"
                                    else
                                        "Introducir cantidad manualmente por teclado",
                                    style = MaterialTheme.typography.bodySmall,
                                    color = Color(0xFF6C757D)
                                )
                            }

                            Switch(
                                checked = useWidgetCounter,
                                onCheckedChange = {
                                    viewModel.changeConteoMode(it)
                                },
                                colors = SwitchDefaults.colors(
                                    checkedThumbColor = accentBlue,
                                    checkedTrackColor = accentBlue.copy(alpha = 0.3f)
                                )
                            )
                        }
                    }
                }
            }

            item {
                Spacer(Modifier.height(8.dp))
            }

            // Card para configuración de tema
//            item {
//                Card(
//                    shape = RoundedCornerShape(18.dp),
//                    colors = CardDefaults.cardColors(containerColor = sectionCard),
//                    modifier = Modifier
//                        .fillMaxWidth()
//                        .padding(vertical = 8.dp)
//                ) {
//                    Column(Modifier.padding(24.dp)) {
//                        Text("Apariencia", color = accentBlue, fontWeight = FontWeight.Bold)
//                        Spacer(Modifier.height(12.dp))
//
//                        // Selección de tema
//                        ExposedDropdownMenuBox(
//                            expanded = themeExpanded,
//                            onExpandedChange = { themeExpanded = !themeExpanded }
//                        ) {
//                            OutlinedTextField(
//                                readOnly = true,
//                                value = themeManager.getThemeDisplayName(currentTheme),
//                                onValueChange = {
//
//                                },
//                                label = { Text("Tema") },
//                                trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(themeExpanded) },
//                                modifier = Modifier
//                                    .menuAnchor()
//                                    .fillMaxWidth(),
//                                colors = OutlinedTextFieldDefaults.colors(
//                                    focusedBorderColor = accentBlue,
//                                    unfocusedBorderColor = Color(0xFFB0BEC5),
//                                    cursorColor = accentBlue
//                                )
//                            )
//                            ExposedDropdownMenu(
//                                expanded = themeExpanded,
//                                onDismissRequest = { themeExpanded = false }
//                            ) {
//                                themes.forEach { theme ->
//                                    DropdownMenuItem(
//                                        text = { Text(themeManager.getThemeDisplayName(theme)) },
//                                        onClick = {
//
//                                            themeManager.setTheme(theme)
//                                            themeExpanded = false
//                                        },
//                                        contentPadding = ExposedDropdownMenuDefaults.ItemContentPadding
//                                    )
//                                }
//                            }
//                        }
//
//                        Spacer(Modifier.height(8.dp))
//
//                        // Descripción del tema seleccionado
//                        Text(
//                            text = when (currentTheme) {
//                                ThemeMode.LIGHT -> "Interfaz con colores claros y brillantes"
//                                ThemeMode.DARK -> "Interfaz con colores oscuros para reducir el cansancio visual"
//                                ThemeMode.SYSTEM -> "Cambia automáticamente según la configuración del sistema"
//                            },
//                            style = MaterialTheme.typography.bodySmall,
//                            color = Color(0xFF6C757D)
//                        )
//                    }
//                }
//            }
//
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewGeneralSetting() {
    Fibra_labelingTheme {
        GeneralSettingScreen(
            onBack = {}
        )
    }
}