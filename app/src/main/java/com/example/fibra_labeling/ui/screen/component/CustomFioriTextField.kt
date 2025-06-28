package com.example.fibra_labeling.ui.screen.component

import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType

@Composable
fun CustomFioriTextField(
    value: String,
    onValueChange: (String) -> Unit,
    label: String,
    modifier: Modifier = Modifier,
    isError: Boolean = false,
    supportingText: @Composable (() -> Unit)? = null,
    enabled: Boolean = true,
    placeholder: String? = null,
    style: TextStyle= MaterialTheme.typography.labelLarge,
    fontWeight: FontWeight = FontWeight.Bold,
    singleLine: Boolean = true,
    leadingIcon: @Composable (() -> Unit)? = null,
    trailingIcon: @Composable (() -> Unit)? = null,
    readOnly: Boolean = false,
    isOnlyNumber: Boolean = false
) {
    OutlinedTextField(

        value = value,
        onValueChange = onValueChange,
        label = { Text(label, style = style, fontWeight = fontWeight) },
        placeholder = placeholder?.let { { Text(it) } },
        modifier = modifier,
        singleLine = singleLine,
        isError = isError,
        enabled = enabled,
        supportingText = supportingText,
        colors = OutlinedTextFieldDefaults.colors(
            focusedBorderColor = MaterialTheme.colorScheme.primary,
            unfocusedBorderColor = MaterialTheme.colorScheme.outline,
            errorBorderColor = MaterialTheme.colorScheme.error,
            focusedLabelColor = MaterialTheme.colorScheme.primary,
            unfocusedLabelColor = MaterialTheme.colorScheme.onSurfaceVariant,
            cursorColor = MaterialTheme.colorScheme.primary,
            focusedContainerColor = MaterialTheme.colorScheme.surface,
            unfocusedContainerColor = MaterialTheme.colorScheme.surface,
            errorContainerColor = MaterialTheme.colorScheme.surface
        ),
        textStyle = style,
        leadingIcon = leadingIcon,
        trailingIcon= trailingIcon,
        readOnly=readOnly,
        keyboardOptions = if (isOnlyNumber) KeyboardOptions(keyboardType = KeyboardType.Decimal) else KeyboardOptions.Default,

        )
}
