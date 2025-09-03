package com.example.fibra_labeling.fibrafil.ui.screen.etiqueta.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.example.fibra_labeling.data.local.entity.fibrafil.ZplLabel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CopiesAndTemplateDialog(
    show: Boolean,
    templates: List<ZplLabel>,
    selectedTemplateId: Long?,
    onDismiss: () -> Unit,
    onConfirm: (copies: Int, template: String) -> Unit
) {
    if (!show) return

    var copiesText by remember { mutableStateOf("1") }
    var expanded by remember { mutableStateOf(false) }
    // Estado local para selección del template
    var selectedTemplateIdLocal by remember {
        mutableStateOf(selectedTemplateId ?: templates.firstOrNull()?.id)
    }
    val selectedTemplate = templates.firstOrNull { it.id == selectedTemplateIdLocal }
    val isValid = copiesText.toIntOrNull()?.let { it > 0 } == true

    Dialog(onDismissRequest = onDismiss) {
        Surface(
            shape = RoundedCornerShape(18.dp),
            tonalElevation = 6.dp,
            color = Color.White,
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 18.dp)
        ) {
            Column(
                modifier = Modifier
                    .padding(26.dp)
                    .widthIn(min = 300.dp, max = 390.dp)
            ) {
                Text(
                    "Configurar impresión",
                    color = Color(0xFF0A6ED1),
                    fontWeight = FontWeight.Bold,
                    style = MaterialTheme.typography.titleMedium
                )
                Spacer(Modifier.height(12.dp))

                // Combo para plantilla ZPL
                Text("Selecciona plantilla", fontWeight = FontWeight.Medium, color = Color(0xFF0A6ED1))
                ExposedDropdownMenuBox(
                    expanded = expanded,
                    onExpandedChange = { expanded = !expanded },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    OutlinedTextField(
                        readOnly = true,
                        value = selectedTemplate?.name ?: "Selecciona plantilla",
                        onValueChange = {},
                        label = { Text("Plantilla ZPL") },
                        trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded) },
                        modifier = Modifier
                            .menuAnchor()
                            .fillMaxWidth(),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = Color(0xFF0A6ED1),
                            unfocusedBorderColor = Color(0xFFB0BEC5),
                            cursorColor = Color(0xFF0A6ED1)
                        )
                    )
                    ExposedDropdownMenu(
                        expanded = expanded,
                        onDismissRequest = { expanded = false }
                    ) {
                        templates.forEach { label ->
                            DropdownMenuItem(
                                text = { Text(label.name) },
                                onClick = {
                                    selectedTemplateIdLocal = label.id
                                    expanded = false
                                },
                                contentPadding = ExposedDropdownMenuDefaults.ItemContentPadding
                            )
                        }
                    }
                }

                Spacer(Modifier.height(20.dp))

                // Campo de copias
                Text("Número de copias", fontWeight = FontWeight.Medium, color = Color(0xFF0A6ED1))
                OutlinedTextField(
                    value = copiesText,
                    onValueChange = { value ->
                        if (value.all { it.isDigit() } && value.length <= 3) {
                            copiesText = value
                        } else if (value.isEmpty()) {
                            copiesText = ""
                        }
                    },
                    label = { Text("Copias") },
                    singleLine = true,
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(Modifier.height(28.dp))

                Row(
                    Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.End
                ) {
                    TextButton(
                        onClick = onDismiss,
                    ) {
                        Text("Cancelar", color = Color.Gray)
                    }
                    Spacer(Modifier.width(12.dp))
                    Button(
                        onClick = {
                            val copias = copiesText.toIntOrNull() ?: 1
                            onConfirm(copias, selectedTemplate?.zplFile ?: "")
                        },
                        enabled = isValid && selectedTemplate != null,
                        shape = RoundedCornerShape(12.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color(0xFF0A6ED1),
                            contentColor = Color.White
                        )
                    ) {
                        Text("Aceptar", fontWeight = FontWeight.Bold)
                    }
                }
            }
        }
    }
}
