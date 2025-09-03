package com.example.fibra_labeling.shared.ui.screen.component

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.fibra_labeling.R
import com.example.fibra_labeling.ui.theme.Fibra_labelingTheme

@Composable
fun BarcodeInputField(
    barcodeValue: String,
    onValueChange: (String) -> Unit,
    onScanClick: () -> Unit,
    modifier: Modifier = Modifier,
    focusRequester: FocusRequester = remember { FocusRequester() },
    editable: Boolean = false,
    readOnly: Boolean = false,
    scanComplete: ()->Unit ={},
    keyboardOptions: KeyboardOptions= KeyboardOptions.Default
) {

    OutlinedTextField(
        keyboardOptions = keyboardOptions,
        value = barcodeValue,
        onValueChange = {

            if (editable) onValueChange(it)
        },
        placeholder = {
            Text(
                text = "Use el dispositivo o cámara para escanear el código de barras...",
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f),
                style = MaterialTheme.typography.bodySmall
            )
        },
        leadingIcon = {
            IconButton (onClick = onScanClick) {
                Icon(
                    painter = painterResource(R.drawable.camera),
                    contentDescription = "Escanear",
                    tint = MaterialTheme.colorScheme.primary.copy(alpha = 0.7f)
                )
            }
        },
        trailingIcon = {
            IconButton (onClick = scanComplete) {
                Icon(
                    imageVector = Icons.Default.Close,
                    contentDescription = "clear",
                    tint = MaterialTheme.colorScheme.primary.copy(alpha = 0.7f)
                )
            }
        },
        readOnly = readOnly,
        maxLines = 1,
        enabled = true,
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
        modifier = modifier
            .fillMaxWidth()
            .focusRequester(focusRequester)
            .padding(horizontal = 20.dp)
    )

}

@Preview(showBackground = true)
@Composable
fun PreviewBar(){
    Fibra_labelingTheme {
        Column(modifier = Modifier.fillMaxSize()) {
            BarcodeInputField(
                barcodeValue = "",
                onValueChange = {},
                onScanClick = {}
            )
        }
    }

}