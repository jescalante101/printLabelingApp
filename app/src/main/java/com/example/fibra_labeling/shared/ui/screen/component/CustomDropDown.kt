package com.example.fibra_labeling.shared.ui.screen.component

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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

@Composable
fun FioriDropdown(
    label: String,
    options: List<String>,
    selected: String="",
    onSelectedChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    isError: Boolean = false,
    supportingText: @Composable (() -> Unit)? = null,
) {
    var text by rememberSaveable { mutableStateOf(selected) }
    var isDropdownVisible by remember { mutableStateOf(false) }
    val filteredSuggestions = remember(text) {
        options.filter { it.contains(text, ignoreCase = true) }
    }
    val focusRequester = remember { FocusRequester() }

    Column(modifier = modifier) {
        OutlinedTextField(
            value = text,
            onValueChange = { newText ->
                text = newText
                isDropdownVisible = newText.isNotEmpty() && filteredSuggestions.isNotEmpty() || newText.isEmpty()
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
                    .clickable(enabled = false) { /* Para evitar que el clic cierre el dropdown inmediatamente */ }
            ) {
                items(filteredSuggestions.size) { suggestion ->
                    Text(
                        text = filteredSuggestions[suggestion],
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp)
                            .clickable {
                                text = filteredSuggestions[suggestion]
                                isDropdownVisible = false
                                onSelectedChange(filteredSuggestions[suggestion])
                            },
                        style = MaterialTheme.typography.labelLarge,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                }
            }
        }
    }
}
