package com.example.fibra_labeling.fibrafil.ui.screen.reception.registerreception.register

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.fibra_labeling.fibrafil.ui.screen.reception.registerreception.register.component.ItemList
import com.example.fibra_labeling.fibrafil.ui.screen.reception.registerreception.register.component.SearchAndFilter
import com.example.fibra_labeling.fibrafil.ui.screen.reception.registerreception.register.component.SummaryBar
import com.example.fibra_labeling.ui.theme.Fibra_labelingTheme
import org.koin.androidx.compose.koinViewModel


@Composable
fun ReceptionFibrafilScreen(
    viewModel:ReceptionFibrafilViewModel = koinViewModel()
){
    val filteredItems = viewModel.filteredItems.collectAsState(initial = emptyList())
    val searchQuery = viewModel.searchQuery.collectAsState()
    val activeFilter = viewModel.activeFilter.collectAsState()

    Scaffold { paddingValues ->
        Column(
            modifier = Modifier.padding(paddingValues)
        ) {
            SummaryBar(viewModel)
            SearchAndFilter(
                searchQuery = searchQuery.value,
                onSearchQueryChange = viewModel::onSearchQueryChange,
                activeFilter = activeFilter.value,
                onFilterChange = viewModel::onFilterChange
            )
            ItemList(
                items = filteredItems.value,
                onQuantityChange = viewModel::updateQuantity,
                onVerify = viewModel::verifyItem
            )
        }
    }
}

// --- 4. Vista Previa ---
@Preview(showBackground = true)
@Composable
fun ReceptionScreenPreview() {
    Fibra_labelingTheme {
        ReceptionFibrafilScreen()
    }
}