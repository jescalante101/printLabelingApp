package com.example.fibra_labeling.ui.screen.component

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp


@Composable
fun CustomSearch(
    value: String="",
    onChangeValue: (String) -> Unit,
    placeholder: String="Use el dispositivo o cámara para escanear el código de barras....",
    leadingIcon: @Composable (() -> Unit)? = null,
    trailingIcon: @Composable (() -> Unit)? = null,
    focusRequester: FocusRequester,
    isReadOnly: Boolean = true,
    enabled: Boolean = false,
){
    OutlinedTextField(
        value = value,

        onValueChange = {
            onChangeValue(it)
        },
        placeholder = {
            Text(
                text = placeholder,
                color = Color(0xFF95A5A6),
                style = MaterialTheme.typography.bodySmall
            )
        },
        leadingIcon =leadingIcon,
        trailingIcon = trailingIcon,
        readOnly = isReadOnly,
        enabled = enabled,
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
        modifier = Modifier
            .fillMaxWidth()
            .focusRequester(focusRequester)
            .padding(horizontal = 16.dp),
    )
}

