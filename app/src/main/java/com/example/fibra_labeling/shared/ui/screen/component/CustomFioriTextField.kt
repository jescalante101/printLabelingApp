package com.example.fibra_labeling.shared.ui.screen.component

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
    textStyle: TextStyle= MaterialTheme.typography.labelLarge,
    fontWeight: FontWeight = FontWeight.Bold,
    singleLine: Boolean = true,
    leadingIcon: @Composable (() -> Unit)? = null,
    trailingIcon: @Composable (() -> Unit)? = null,
    readOnly: Boolean = false,
    isOnlyNumber: Boolean = false
) {
    OutlinedTextField(

        value = value,
        onValueChange = {newValue ->
            if (isOnlyNumber) {
                // Solo permite números, punto decimal y signo negativo al inicio
                val filteredValue = newValue.filter { char ->
                    char.isDigit() || char == '.' || (char == '-' && newValue.indexOf(char) == 0)
                }

                // Validación adicional para evitar múltiples puntos decimales
                val decimalCount = filteredValue.count { it == '.' }
                if (decimalCount <= 1) {
                    onValueChange(filteredValue)
                }
            } else {
                onValueChange(newValue)
            }
        },
        label = { Text(label, style = textStyle, fontWeight = fontWeight) },
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
        textStyle = textStyle,
        leadingIcon = leadingIcon,
        trailingIcon= trailingIcon,
        readOnly=readOnly,
        keyboardOptions = if (isOnlyNumber) KeyboardOptions(keyboardType = KeyboardType.Decimal) else KeyboardOptions.Default,
        shape = MaterialTheme.shapes.medium
    )
}
