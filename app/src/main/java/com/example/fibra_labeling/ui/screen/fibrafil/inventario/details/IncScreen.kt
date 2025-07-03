package com.example.fibra_labeling.ui.screen.fibrafil.inventario.details


import SwipeableItem
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SnackbarResult
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.example.fibra_labeling.R
import com.example.fibra_labeling.ui.component.CustomAppBar
import com.example.fibra_labeling.ui.screen.component.CustomSearch
import com.example.fibra_labeling.ui.screen.fibrafil.inventario.details.component.FioriCardIncCompact
import org.koin.androidx.compose.koinViewModel
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.navigation.NavController
import com.example.fibra_labeling.ui.navigation.Screen
import com.example.fibra_labeling.ui.screen.fibrafil.inventario.register.stock.ICountingScreen
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun IncScreen(
    onNavigateBack: () -> Unit,
    viewModel: IncViewModel = koinViewModel(),
    navController: NavController,
    docEntry: Int
){
    var search by remember { mutableStateOf("") }

    val incData by viewModel.incData.collectAsState()
    var showSheet by remember { mutableStateOf(false) }
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)

    val itemSelected by viewModel.itemSelected.collectAsState()

    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()

    LaunchedEffect(Unit) {
        viewModel.setDocEntry(docEntry)
        viewModel.setSearch("")

    }

    LaunchedEffect(Unit) {
        viewModel.eventoNavegacion.collect { destino ->
            when (destino) {
                "printSetting"->navController.navigate(Screen.PrintSetting.route)
            }
        }
    }

    LaunchedEffect(Unit) {
        viewModel.message.collect { error ->
            when(error){
                "successPrint"->snackbarHostState.showSnackbar("Etiqueta Impresa")
                else->snackbarHostState.showSnackbar(error)
            }
        }
    }


    Box {
        Scaffold(
            snackbarHost = { SnackbarHost(snackbarHostState) }
        ) {
            LazyColumn (
                modifier = Modifier
                    .fillMaxSize().padding(it).padding(top = 56.dp)
            ) {
                item {
                    Spacer(modifier = Modifier.height(16.dp))
                }
                item {
                    CustomSearch(
                        value = search,
                        onChangeValue = {
                            search = it
                            viewModel.setSearch(it)
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
                        enabled = true
                    )
                }
                item {
                    Spacer(
                        modifier = Modifier.height(8.dp)
                    )
                }

                items(incData.size) {index->
                    AnimatedVisibility(
                        visible = true,
                        enter = fadeIn(),
                        exit = fadeOut()
                    ) {
                        val inc = incData[index]

                        SwipeableItem (
                            item = inc,
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
                            }
                        ) {
                            FioriCardIncCompact(
                                dto = inc,
                                onClick = { item ->
                                    viewModel.onItemSelectedChange(inc)

                                    showSheet = true
                                },
                                enabled = !inc.isSynced,
                                onPrintClick = {
                                    viewModel.getEtiquetaBYWhsAndItemCode(
                                        it.U_WhsCode.toString(),
                                        it.U_ItemCode.toString()
                                    )
                                }
                            )
                        }

                    }
                }

            }
        }
        Box(
            modifier = Modifier.padding(top= 32.dp)
        ){
            CustomAppBar(
                title = { Text("Toma de Inventario", color = Color.Black, style = MaterialTheme.typography.titleMedium) },
                leadingIcon = {
                    IconButton(
                        onClick = {
                            onNavigateBack()
                        }
                    ) {
                        Icon(
                            painter = painterResource(R.drawable.ic_arrow_left),
                            contentDescription = "Back",
                            tint = Color.Black
                        )
                    }
                },
                trailingIcon ={
                    IconButton(
                        onClick = {

                        }
                    ){
                        Icon(
                            imageVector = Icons.Default.AccountCircle,
                            contentDescription = "user",
                            tint = Color.Black
                        )
                    }
                }
            )
        }
    }

    if (showSheet) {
        ModalBottomSheet(
            onDismissRequest = { showSheet = false },
            sheetState = sheetState,
        ) {
            ICountingScreen(
                onSave = { cantidad, stock ->
                    viewModel.onConteoChange(cantidad.toString()) // ← Aquí obtienes el valor cuando el usuario confirma
                    viewModel.updateConteo()
                    showSheet = false
                },
                product = itemSelected.U_ItemName.toString(),
                itemCode = itemSelected.U_ItemCode.toString(),
                whsCode = "CH3-RE",
                conteo = itemSelected.U_CountQty?.toInt() ?: 0
            )
        }
    }
}


