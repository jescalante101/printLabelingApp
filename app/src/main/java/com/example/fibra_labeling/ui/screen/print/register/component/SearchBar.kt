package com.example.fibra_labeling.ui.screen.print.register.component

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.shape.RoundedCornerShape
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

@Composable
fun SearchBar(
    searchText: String,
    onSearchTextChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    onSearch: (String) -> Unit,

) {
    OutlinedTextField(
        value = searchText,
        onValueChange = onSearchTextChange,
        placeholder = {
            Text(
                text = "Buscar por nombre o código...",
                color = Color(0xFF95A5A6),
                style = MaterialTheme.typography.bodySmall
            )
        },
        leadingIcon = {
            IconButton(
                onClick = {
                    onSearch(searchText)
                }
            ) {
                Icon(
                    imageVector = Icons.Default.Search,
                    contentDescription = "Buscar",
                    tint = Color(0xFF7F8C8D)
                )
            }

        },
        trailingIcon = {
            if (searchText.isNotEmpty()) {
                IconButton(
                    onClick = { onSearchTextChange("") }
                ) {
                    Icon(
                        imageVector = Icons.Default.Clear,
                        contentDescription = "Limpiar",
                        tint = Color(0xFF7F8C8D)
                    )
                }
            }
        },

        singleLine = true,
        shape = RoundedCornerShape(12.dp),
        colors = OutlinedTextFieldDefaults.colors(
            focusedBorderColor = Color(0xFF3498DB),
            unfocusedBorderColor = Color(0xFFE0E0E0),
            focusedContainerColor = Color.White,
            unfocusedContainerColor = Color.White,

        ),
        textStyle = MaterialTheme.typography.bodyMedium.copy(
            color = Color.Black,
            fontWeight = FontWeight.Medium
        ),
        modifier = modifier.fillMaxWidth()
    )
}
