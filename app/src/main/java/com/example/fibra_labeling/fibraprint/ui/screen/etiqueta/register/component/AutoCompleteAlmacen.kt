package com.example.fibra_labeling.fibraprint.ui.screen.etiqueta.register.component

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
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
import androidx.compose.ui.unit.dp
import com.example.fibra_labeling.data.model.AlmacenResponse

@Composable
fun FioriDropdownAlmacen(
    label: String,
    options: List<AlmacenResponse>,
    selected: AlmacenResponse?,
    onSelectedChange: (AlmacenResponse) -> Unit,
    modifier: Modifier = Modifier,
    isError: Boolean = false,
    supportingText: @Composable (() -> Unit)? = null
) {
    // Sincroniza el texto con el seleccionado o en blanco si nulo
    var text by rememberSaveable(selected) { mutableStateOf(selected?.whsName ?: "") }
    var isDropdownVisible by remember { mutableStateOf(false) }
    val focusRequester = remember { FocusRequester() }

    // Filtrado dinámico
    val filteredSuggestions = remember(text, options) {
        if (text.isEmpty()) options
        else options.filter { it.whsName.contains(text, ignoreCase = true) || it.whsCode.contains(text, ignoreCase = true) }
    }

    Column(modifier = modifier) {
        OutlinedTextField(
            value = text,
            onValueChange = { newText ->
                text = newText
                isDropdownVisible = filteredSuggestions.isNotEmpty() || newText.isEmpty()
            },
            label = { Text(label) },
            trailingIcon = {
                IconButton(
                    onClick = {
                        isDropdownVisible = !isDropdownVisible
                        focusRequester.requestFocus()
                    }
                ) {
                    Icon(
                        if (isDropdownVisible) Icons.Filled.KeyboardArrowUp else Icons.Filled.KeyboardArrowDown,
                        contentDescription = "Buscar"
                    )
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .onFocusEvent {
                    isDropdownVisible = it.isFocused
                }
                .focusRequester(focusRequester),
            isError = isError,
            supportingText = supportingText
        )

        if (isDropdownVisible) {
            LazyColumn(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 4.dp)
                    .heightIn(max = 200.dp)
                    .background(MaterialTheme.colorScheme.onTertiary)
            ) {
                items(filteredSuggestions.size) { idx ->
                    val option = filteredSuggestions[idx]
                    Text(
                        text = "${option.whsName} (${option.whsCode})",
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp)
                            .clickable {
                                text = option.whsName
                                isDropdownVisible = false
                                onSelectedChange(option)
                            },
                        style = MaterialTheme.typography.labelLarge,
                    )
                }
            }
        }
    }
}
