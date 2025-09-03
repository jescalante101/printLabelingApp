package com.example.fibra_labeling.fibrafil.ui.screen.reception.registerreception.register.component

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.fibra_labeling.R
import com.example.fibra_labeling.fibrafil.ui.screen.reception.registerreception.model.FilterType

@Composable
fun SearchAndFilter(
    searchQuery: String,
    onSearchQueryChange: (String) -> Unit,
    activeFilter: FilterType,
    onFilterChange: (FilterType) -> Unit
){
    Column (Modifier.padding(16.dp)) {
        OutlinedTextField(
            value = searchQuery,
            onValueChange = onSearchQueryChange,
            modifier = Modifier.fillMaxWidth(),
            placeholder = { Text("Buscar por código o descripción...") },
            leadingIcon = {
                Icon(
                    painter = painterResource(R.drawable.ic_search),
                    "Search"
                )
            },
            shape = RoundedCornerShape(12.dp)
        )
        Spacer(Modifier.height(12.dp))
        SegmentedButtonRow(activeFilter, onFilterChange)
    }
}

@Composable
fun SegmentedButtonRow(
    activerFilter: FilterType,
    onFilterChange: (FilterType) -> Unit
){
    val filters = FilterType.values()
    Row(
        modifier = Modifier.fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))
            .background(MaterialTheme.colorScheme.surfaceVariant)
            .border(1.dp, Color.LightGray,RoundedCornerShape(12.dp))
    ) {
        filters.forEach { filter->
            val isactive = filter == activerFilter
            Button(
                onClick = { onFilterChange(filter) },
                modifier = Modifier.weight(1f),
                shape = RoundedCornerShape(0.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = if (isactive) MaterialTheme.colorScheme.primaryContainer else Color.Transparent,
                    contentColor = if (isactive) MaterialTheme.colorScheme.onPrimaryContainer else MaterialTheme.colorScheme.onSurfaceVariant
                )

            ) {
                Text(
                    text=filter.name.replaceFirstChar { it.uppercase() },
                    fontWeight = if (isactive) FontWeight.Bold else FontWeight.Normal
                )
            }
        }
    }

}