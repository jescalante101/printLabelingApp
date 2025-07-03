package com.example.fibra_labeling.ui.screen.component

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
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

@Composable
fun <T> CustomFioriDropDown(
    label: String,
    options: List<T>,
    selected: T?,
    onSelectedChange: (T) -> Unit,
    onFilterChange: (String) -> Unit,
    itemLabel: (T) -> String,
    modifier: Modifier = Modifier,
    isError: Boolean = false,
    supportingText: @Composable (() -> Unit)? = null,
    enabled: Boolean = true
) {
    var text by rememberSaveable(selected) { mutableStateOf(selected?.let { itemLabel(it) } ?: "") }
    var filterText by rememberSaveable { mutableStateOf("") }
    var isDropdownVisible by remember { mutableStateOf(false) }
    val fieldFocusRequester = remember { FocusRequester() }
    // El flag para evitar reapertura por focus
    var suppressDropdownOnNextFocus by remember { mutableStateOf(false) }

    Column(modifier = modifier) {
        // Campo principal (solo muestra selección, no editable)
        CustomFioriTextField(
            value = text,
            singleLine = true,
            enabled = enabled,
            onValueChange = {}, // No editable desde aquí
            label = label,
            trailingIcon = {
                IconButton(
                    onClick = {
                        if (enabled) {
                            isDropdownVisible = !isDropdownVisible
                            if (isDropdownVisible) fieldFocusRequester.requestFocus()
                        }
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
                .onFocusChanged { focusState ->
                    if (focusState.isFocused && !suppressDropdownOnNextFocus) {
                        isDropdownVisible = true
                    }
                    if (!focusState.isFocused) {
                        suppressDropdownOnNextFocus = false
                    }
                }
                .focusRequester(fieldFocusRequester),
            isError = isError,
            supportingText = supportingText,
            readOnly = true
        )

        AnimatedVisibility(visible = isDropdownVisible) {
            Column(
                Modifier
                    .fillMaxWidth()
                    .heightIn(max = 300.dp)
                    .border(
                        width = 1.dp,
                        color = MaterialTheme.colorScheme.outline,
                        shape = MaterialTheme.shapes.medium
                    )
                    .background(MaterialTheme.colorScheme.surface)
            ) {
                // Campo de búsqueda (sin focusRequester aquí)
                OutlinedTextField(
                    value = filterText,
                    onValueChange = {
                        filterText = it
                        onFilterChange(it)
                    },
                    label = { Text("Filtrar") },
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
                        .padding(4.dp)
                )

                HorizontalDivider()

                if (options.isEmpty()) {
                    Text(
                        text = "Sin resultados",
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        style = MaterialTheme.typography.bodyMedium,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                } else {
                    LazyColumn(
                        modifier = Modifier
                            .fillMaxWidth()
                            .heightIn(
                                min = 100.dp,
                                max = if (options.size > 5) 250.dp else Dp.Unspecified
                            )
                    ) {
                        items(options) { option ->
                            Text(
                                text = itemLabel(option),
                                style = MaterialTheme.typography.bodyMedium,
                                color = MaterialTheme.colorScheme.onSurface,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clickable {
                                        text = itemLabel(option)
                                        filterText = ""
                                        isDropdownVisible = false
                                        suppressDropdownOnNextFocus = true // Evita reapertura
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
