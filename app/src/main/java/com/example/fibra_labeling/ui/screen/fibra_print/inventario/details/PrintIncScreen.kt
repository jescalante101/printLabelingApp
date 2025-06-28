package com.example.fibra_labeling.ui.screen.fibra_print.inventario.details

import SwipeableItem
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SnackbarResult
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.fibra_labeling.R
import com.example.fibra_labeling.ui.screen.component.CustomSearch
import com.example.fibra_labeling.ui.screen.component.CustomSwipeableItem
import com.example.fibra_labeling.ui.screen.fibra_print.inventario.details.component.PrintFioriCardIncCompact
import com.example.fibra_labeling.ui.theme.FioriBackground
import kotlinx.coroutines.launch
import org.koin.androidx.compose.koinViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PrintIncScreen(
    onNavigateBack: () -> Unit = {},
    viewModel:PrintIncViewModel = koinViewModel(),
    navController: NavController,
    docEntry: Int

) {

    val incData by  viewModel.incData.collectAsState()
    val scope = rememberCoroutineScope()
    val snackbarHostState = remember { SnackbarHostState() }
    var showSheet by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        viewModel.setDocEntry(docEntry)
        viewModel.setSearch("")

    }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        text = "Detalle de Inventario",
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold,
                    )
                },
                navigationIcon = {
                    IconButton (onClick = onNavigateBack) {
                        Icon(
                            painter = painterResource(R.drawable.ic_arrow_left),
                            contentDescription = "Volver",
                            tint = MaterialTheme.colorScheme.primary
                        )
                    }
                }
            )
        },
        containerColor = FioriBackground
    ) { paddingValues ->
        LazyColumn  (
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(),
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.Start
        ) {
            item {
                Spacer(modifier = Modifier.height( 16.dp))
            }

            item {
                CustomSearch(
                    value = "search",
                    onChangeValue = {
                        //viewModel.setSearch(it)
                    },
                    placeholder = "Buscar",
                    focusRequester = FocusRequester.Default,
                    leadingIcon = {
                        Icon(
                            painter = painterResource(R.drawable.barcode_scan),
                            contentDescription = "Barcode Icon",
                        )
                    },
                    trailingIcon = {
                        IconButton(
                            onClick = {
                                //onNavigateToCounting()
                            }
                        ) {
                            Icon(
                                painter = painterResource(R.drawable.ic_search),
                                contentDescription = "Search",
                            )
                        }
                    },

                    isReadOnly = false,
                    enabled = true,
                )
            }
            item {
                Spacer(modifier = Modifier.height( 8.dp))
            }
            items(incData.size) {index->
                AnimatedVisibility(
                    visible = true,
                    enter = fadeIn(),
                    exit = fadeOut()
                ) {
                    val inc = incData[index]

                    CustomSwipeableItem(
                        item = inc,
                        itemId = inc,
                        canSwipe = {true},

                        onDelete = {
                            viewModel.deleteItem(inc)
                            scope.launch {
                                val result = snackbarHostState.showSnackbar(
                                    message = "Eliminado",
                                    actionLabel = "Deshacer",
                                    duration = SnackbarDuration.Short
                                )
                                if (result == SnackbarResult.ActionPerformed) {
                                    viewModel.restaurarUltimoEliminado()
                                }
                            }
                        },

                    ) {
                        PrintFioriCardIncCompact(
                            dto = inc,
                            onClick = { item ->
//                                viewModel.onItemSelectedChange(inc)
//
//                                showSheet = true
                            },
                            enabled = !inc.isSynced,
                            onPrintClick = {
//                                viewModel.getEtiquetaBYWhsAndItemCode(
//                                    it.U_WhsCode.toString(),
//                                    it.U_ItemCode.toString()
//                                )
                            }
                        )
                    }

                }
            }


        }
    }
}
