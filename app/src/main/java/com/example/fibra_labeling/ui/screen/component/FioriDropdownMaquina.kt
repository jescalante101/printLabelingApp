package com.example.fibra_labeling.ui.screen.component

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
import com.example.fibra_labeling.data.model.MaquinaData

@Composable
fun FioriDropdownMaquina(
    label: String,
    options: List<MaquinaData>,
    selected: MaquinaData?,
    onSelectedChange: (MaquinaData) -> Unit,
    onFilterChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    isError: Boolean = false,
    supportingText: @Composable (() -> Unit)? = null
) {
    var text by rememberSaveable(selected) { mutableStateOf(selected?.name ?: "") }
    var isDropdownVisible by remember { mutableStateOf(false) }
    val focusRequester = remember { FocusRequester() }

    Column(modifier = modifier) {
        OutlinedTextField(
            value = text,
            singleLine = true,
            onValueChange = { newText ->
                text = newText
                isDropdownVisible = true
                onFilterChange(newText) // <-- Aquí filtras en Room
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
                .onFocusEvent { isDropdownVisible = it.isFocused }
                .focusRequester(focusRequester),
            isError = isError,

            supportingText = supportingText
        )

        if (isDropdownVisible) {
            LazyColumn(
                modifier = Modifier
                    .fillMaxWidth()
                    .heightIn(max = 200.dp)
            ) {
                items(options.size) { idx ->
                    val option = options[idx]
                    Text(
                        text = "${option.name} (${option.code})",
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp)
                            .clickable {
                                text = option.name.orEmpty()
                                isDropdownVisible = false
                                onSelectedChange(option)
                            }
                    )
                }
            }
        }
    }
}
