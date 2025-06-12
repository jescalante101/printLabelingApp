package com.example.fibra_labeling.ui.screen.component

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CustomTextFormField(
    label: String,
    value: String,
    onValueChange: (String) -> Unit,
    enabled: Boolean,
    modifier: Modifier = Modifier,
    onlyNumbers: Boolean = false,
    isError: Boolean = false,
    supportingText: @Composable (() -> Unit)? = null,
) {
    OutlinedTextField(
        value = value,
        onValueChange = { newValue->
            if (onlyNumbers) {
                val filteredValue = newValue.filter { it.isDigit() || it == '.' }
                if (filteredValue.count { it == '.' } <= 1) {
                    onValueChange(filteredValue)
                }
            } else {
                onValueChange(newValue)
            }
        },
        enabled = enabled,
        readOnly = !enabled,
        modifier = modifier.fillMaxWidth(),
        label = { Text(label, fontSize = 13.sp) },
        singleLine = true,
        shape = RoundedCornerShape(8.dp),
        colors = TextFieldDefaults.colors(
            focusedContainerColor = Color(0xFFF7F7F7),
            unfocusedContainerColor = Color(0xFFF7F7F7),
            disabledContainerColor = Color(0xFFF7F7F7),
            focusedIndicatorColor = Color(0xFF0070F2),
            unfocusedIndicatorColor = Color(0xFFE0E0E0)
        ),
        keyboardOptions = if (onlyNumbers) KeyboardOptions(keyboardType = KeyboardType.Decimal) else KeyboardOptions.Default,
        isError = isError,
        supportingText = supportingText
    )
}

