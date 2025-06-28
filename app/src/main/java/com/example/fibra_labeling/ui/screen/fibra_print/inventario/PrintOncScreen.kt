package com.example.fibra_labeling.ui.screen.fibra_print.inventario

import android.util.Log
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.fibra_labeling.R
import com.example.fibra_labeling.ui.screen.component.CustomEmptyMessage
import com.example.fibra_labeling.ui.screen.component.CustomSearch
import com.example.fibra_labeling.ui.screen.component.CustomSwipeableItem
import com.example.fibra_labeling.ui.screen.fibra_print.inventario.component.PrintFioriCardConteoCompact
import com.example.fibra_labeling.ui.screen.fibra_print.inventario.register.PrintOncRegister
import com.example.fibra_labeling.ui.theme.FioriBackground
import org.koin.androidx.compose.koinViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PrintOncScreen(
    onBack: () -> Unit,
    onNavigateToProduct: () -> Unit,
    viewModel: PrintOncViewModel= koinViewModel(),
    onNavigateToDetails: (Int) -> Unit
){

    val snackbarHostState = remember { SnackbarHostState() }
    var showSheet by remember { mutableStateOf(false) }
    var searchQuery by remember { mutableStateOf("") }

    val allUser by viewModel.allUsers.collectAsState()

    Scaffold(
        containerColor = FioriBackground,
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        "Inventario",
                        style = MaterialTheme.typography.titleLarge,
                        color = Color.Black,
                        fontWeight = FontWeight.Bold
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(
                            painter = painterResource(R.drawable.ic_arrow_left),
                            contentDescription = "Back",
                            tint = MaterialTheme.colorScheme.primary
                        )
                    }
                }
            )
        },

        snackbarHost = { SnackbarHost(snackbarHostState) },
        floatingActionButton = {
            // Floating button para crear un nuevo registro
            ExtendedFloatingActionButton(
                onClick = {
                    showSheet = true
                },
                containerColor = Color(0xFF2C3E50),
            ) {
                Text("Nuevo", color = Color.White)
            }
        }

    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(it)
                .padding(top = 16.dp)
        ) {
            CustomSearch(
                value = searchQuery,
                onChangeValue = {
                        text ->
                    viewModel.onSearch(text)
                    //viewModel.updateSearchQuery(text)
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
                        onClick = {}
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
            Spacer(modifier = Modifier.height(8.dp))

            LazyColumn {

                item {
                    if (allUser.isEmpty()){
                        CustomEmptyMessage(
                            title = "No hay Inventario",
                            message = "Presione el boton flotante para crear un nuevo registro"
                        )
                    }else  {
                        Spacer(modifier = Modifier.height(16.dp))
                    }
                }

                items(allUser.size) { index ->

                    AnimatedVisibility(
                        visible = true,
                        enter = fadeIn(),
                        exit = fadeOut()
                    ) {
                        val oinc=allUser[index]

                        CustomSwipeableItem(
                            item = oinc,
                            itemId = oinc.header.docEntry,
                            canSwipe = { !oinc.header.isSynced },
                            onDelete = {
                                viewModel.deleteOinc(oinc)
                            },
                        ) {
                            PrintFioriCardConteoCompact(
                                dto = oinc.header,
                                onDetailsClick = {
                                    onNavigateToDetails(oinc.header.docEntry.toInt())
                                },

                                onSyncClick = {
                                    //showDialog = true
                                    //itemToSync = inc.cabecera
                                },

                                onClick = { oinc->
                                    Log.e("TAG", "PrintOncScreen: ${oinc.u_UserNameCount}")

                                    onNavigateToProduct()
                                    viewModel.saveUserLogin(
                                        userLogin = oinc.u_UserNameCount ?: "",
                                        code = oinc.u_Ref ?: "",
                                        docEntry = oinc.docEntry.toString()
                                    )

                                },
                                isSyncing = oinc.header.isSynced,
                                detailsEnabled = oinc.details.isNotEmpty()
                            )

                        }

                    }
                }
            }
        }

    }

    // Bottom sheet para el registro
    PrintOncRegister (
        showSheet = showSheet,
        onDismiss = { showSheet = false },
        onSave = {}
    )

}