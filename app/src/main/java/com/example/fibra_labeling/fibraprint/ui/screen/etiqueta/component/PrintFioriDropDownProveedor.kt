package com.example.fibra_labeling.fibraprint.ui.screen.etiqueta.component

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.onFocusEvent
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.example.fibra_labeling.data.local.entity.fibraprint.POcrdEntity
import com.example.fibra_labeling.data.local.entity.fibraprint.POusrEntity
import com.example.fibra_labeling.shared.ui.screen.component.CustomFioriTextField

@Composable
fun PrintFioriDropdownProveedor(
    label: String,
    options: List<POcrdEntity>,
    selected: POusrEntity?,
    onSelectedChange: (POcrdEntity) -> Unit,
    onFilterChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    isError: Boolean = false,
    supportingText: @Composable (() -> Unit)? = null
) {
    var text by rememberSaveable(selected) { mutableStateOf(selected?.uNAME ?: "") }
    var filterText by rememberSaveable { mutableStateOf("") }
    var isDropdownVisible by remember { mutableStateOf(false) }
    val focusRequester = remember { FocusRequester() }

    Column(
        modifier = modifier,
    ) {
        // Campo principal para mostrar la selección
        CustomFioriTextField(
            value = text,
            singleLine = true,
            onValueChange = { newText ->
                text = newText
                isDropdownVisible = true
                onFilterChange(newText)
            },
            label = label,
            trailingIcon = {
                IconButton(
                    onClick = {
                        isDropdownVisible = !isDropdownVisible
                        focusRequester.requestFocus()
                    }
                ) {
                    Icon(
                        if (isDropdownVisible) Icons.Filled.KeyboardArrowUp else Icons.Filled.KeyboardArrowDown,
                        contentDescription = "Mostrar opciones"
                    )
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .onFocusEvent { isDropdownVisible = it.isFocused }
                .focusRequester(focusRequester),
            isError = isError,
            supportingText = supportingText,
            readOnly = true
        )

        AnimatedVisibility (visible = isDropdownVisible) {
            Column(
                Modifier
                    .fillMaxWidth()
                    .border(
                        width = 1.dp,
                        color = MaterialTheme.colorScheme.outline,
                        shape = MaterialTheme.shapes.medium
                    )
                    .background(MaterialTheme.colorScheme.surface)
            ) {
                // Campo de búsqueda extra
                OutlinedTextField(
                    value = filterText,
                    onValueChange = {
                        filterText = it
                        onFilterChange(it)
                    },
                    label = { Text("Filtrar usuarios") },
                    singleLine = true,
                    trailingIcon = {
                        if (filterText.isNotEmpty()) {
                            IconButton(onClick = {
                                filterText = ""
                                onFilterChange("")
                            }) {
                                Icon(Icons.Filled.Close, contentDescription = "Limpiar filtro")
                            }
                        }
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .onFocusEvent{isDropdownVisible = true}
                        .padding(4.dp)
                )

                HorizontalDivider()

                if (options.isEmpty()) {
                    Text(
                        text = "Sin resultados",
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        style = MaterialTheme.typography.bodyMedium
                    )
                } else {
                    // LazyColumn con altura adaptativa
                    LazyColumn(
                        modifier = Modifier
                            .fillMaxWidth()
                            .heightIn(
                                min = 100.dp,
                                max = if (options.size > 5) 300.dp else Dp.Unspecified
                            )
                    ) {
                        items(options) { option ->
                            Text(
                                text = "${option.cardName.orEmpty()} (${option.cardCode.orEmpty()})",
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clickable {
                                        text = option.cardName.orEmpty()
                                        filterText = ""
                                        isDropdownVisible = false
                                        onSelectedChange(option)
                                    }
                                    .padding(horizontal = 16.dp, vertical = 12.dp)
                            )
                        }
                    }
                }
            }
        }
    }
}