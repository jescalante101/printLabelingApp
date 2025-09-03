package com.example.fibra_labeling.fibraprint.ui.screen.etiqueta.register.component

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp

@Composable
fun SearchBar(
    searchText: String,
    onSearchTextChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    onSearch: (String) -> Unit,
    onDone: () -> Unit = {}
) {
    OutlinedTextField(
        value = searchText,
        onValueChange = onSearchTextChange,
        keyboardOptions = KeyboardOptions.Default.copy(
            imeAction = ImeAction.Search
        ),
        keyboardActions = KeyboardActions(
            onSearch = { onSearch(searchText) },
            onDone = { onDone() }
        ),
        placeholder = {
            Text(
                text = "Buscar por nombre o código...",
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f),
                style = MaterialTheme.typography.bodySmall
            )
        },
        leadingIcon = {
            Icon(
                imageVector = Icons.Default.Search,
                contentDescription = "Buscar",
                tint = MaterialTheme.colorScheme.primary.copy(alpha = 0.7f)
            )
        },
        trailingIcon = {
            if (searchText.isNotEmpty()) {
                IconButton(onClick = { onSearchTextChange("") }) {
                    Icon(
                        imageVector = Icons.Default.Clear,
                        contentDescription = "Limpiar",
                        tint = MaterialTheme.colorScheme.primary.copy(alpha = 0.5f)
                    )
                }
            }
        },
        singleLine = true,
        shape = RoundedCornerShape(16.dp),
        colors = OutlinedTextFieldDefaults.colors(
            focusedBorderColor = MaterialTheme.colorScheme.primary,
            unfocusedBorderColor = MaterialTheme.colorScheme.outlineVariant,
            focusedContainerColor = MaterialTheme.colorScheme.background,
            unfocusedContainerColor = MaterialTheme.colorScheme.background
        ),
        textStyle = MaterialTheme.typography.bodyMedium.copy(
            color = MaterialTheme.colorScheme.onSurface,
            fontWeight = FontWeight.Medium
        ),
        modifier = modifier.fillMaxWidth()
    )
}
