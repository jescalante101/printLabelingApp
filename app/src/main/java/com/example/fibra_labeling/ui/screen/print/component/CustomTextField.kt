package com.example.fibra_labeling.ui.screen.print.component

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.fibra_labeling.ui.theme.Fibra_labelingTheme

@Composable
fun CustomTextField(
    value: String,
    onValueChange: (String) -> Unit,
    label: String,
    leadingIcon: @Composable (() -> Unit)? = null,
    trailingIcon: @Composable (() -> Unit)? = null,
    maxLines: Int = Int.MAX_VALUE
){
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        label = { Text(label) },
        maxLines = maxLines,
    )

}

@Preview(showBackground = true)
@Composable
fun PreviewTextfield(){
    Fibra_labelingTheme {
        Column (modifier = Modifier.fillMaxSize()){
            CustomTextField(value = "", onValueChange = {}, label = "Nombre")
        }

    }
}