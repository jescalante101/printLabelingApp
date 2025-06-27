package com.example.fibra_labeling.ui.screen.fibrafil.home

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.staggeredgrid.LazyVerticalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridItemSpan
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDrawerState
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.fibra_labeling.R
import com.example.fibra_labeling.ui.component.CustomAppBar
import com.example.fibra_labeling.ui.navigation.Screen
import com.example.fibra_labeling.ui.screen.fibrafil.home.component.CustomBottomSheetMenu
import com.example.fibra_labeling.ui.screen.fibrafil.home.component.CustomButtonCard
import com.example.fibra_labeling.ui.screen.fibrafil.home.component.FioriMenuDrawerSheet
import com.example.fibra_labeling.ui.screen.fibrafil.home.component.HomeCategories
import com.example.fibra_labeling.ui.util.gradientBrush
import kotlinx.coroutines.launch
import org.koin.androidx.compose.koinViewModel

@OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)
@Composable
fun HomeScreen(
    viewModel: HomeViewModel= koinViewModel(),
    onNavigateToFill: () -> Unit,
    onNavigateToReception: () -> Unit,
    onNavigateToTransfer: () -> Unit,
    onNavigateToInventory: () -> Unit,
    onNavigateToPackingList: () -> Unit,
    onNavigateToProduction: () -> Unit,
    onNavigateToSetting: () -> Unit,
    onNavigateToZplScreen: () -> Unit
){
    val categories = listOf(
        HomeCategories(R.drawable.ic_scan, "Generar etiquetas",Screen.Print.route),
        HomeCategories(R.drawable.ic_report,"Recepción de compras",Screen.Reception.route),
        HomeCategories(R.drawable.ic_transfer, "Tranferencias",Screen.Transfer.route),
        HomeCategories(R.drawable.ic_inventory, "Toma de inventario",Screen.Inventory.route),
        HomeCategories(R.drawable.ic_packing, "PackingList",Screen.PackingList.route),
        HomeCategories(R.drawable.ic_report, "Recibo Producción",Screen.Production.route),
        HomeCategories(R.drawable.sync_svgrepo_com,"Sincronizar","sync")
    )

    val isSyncing by viewModel.isSyncing.collectAsState()
    val syncMessage by viewModel.syncMessage.collectAsState()

    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()

    var showSheet by remember { mutableStateOf(false) }
    var showTopSnackbar by remember { mutableStateOf(false) }

    val snackbarHostState = remember { SnackbarHostState() }

    var menuExpanded by remember { mutableStateOf(false) }

    var selectedMenu by remember { mutableStateOf("Impresora") }


    //Dialog
    if (isSyncing) {
        AlertDialog(
            onDismissRequest = {}, // Así no se puede cerrar tocando fuera ni con back
            title = {
                Box(Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
                    Text("Sincronización")
                }
            },
            text = {
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    CircularProgressIndicator()
                    Spacer(Modifier.height(16.dp))
                    Text(syncMessage)
                }
            },
            confirmButton = {} // Sin botón de cierre
        )
    }

    LaunchedEffect(Unit) {
        viewModel.syncError.collect { error->
           when (error){
               "errorSync"->{
                   scope.launch {
                       snackbarHostState.showSnackbar(
                           "No pudimos conectarnos a SAP y, por lo tanto, no se pudieron cargar los datos necesarios. Por favor, revisa tu conexión a internet o inténtalo nuevamente en unos minutos."
                       )
                   }
               }
               "success"->{
                   scope.launch {
                       snackbarHostState.showSnackbar(
                           "Datos obtenidos con éxito de SAP"
                       )
                   }
               }
           }
        }
    }


    ModalNavigationDrawer(
        drawerContent = {
            ModalDrawerSheet {
                FioriMenuDrawerSheet (
                    selectedMenu = selectedMenu,
                    onSelect = { selectedMenu = it },
                    onNavigateToSetting = {
                        onNavigateToSetting()
                    },
                    onNavigateToZplScreen = {
                        onNavigateToZplScreen()
                    }
                )
            }
        },
        drawerState = drawerState

    )
    {


        Box(
            modifier = Modifier.fillMaxSize()
                .background(gradientBrush)
        ) {

            Scaffold(
                containerColor = Color.Transparent,
                snackbarHost = { SnackbarHost(
                    snackbarHostState,
                    modifier = Modifier.align(alignment = Alignment.TopCenter)
                ) },
            ) { padding ->
                LazyVerticalStaggeredGrid(
                    columns = StaggeredGridCells.Adaptive(160.dp),
                    verticalItemSpacing = 16.dp,
                    modifier = Modifier.padding(padding).padding(top = 24.dp)
                ) {
                    item(span = StaggeredGridItemSpan.FullLine) {
                        Spacer(modifier = Modifier.height(12.dp))
                    }
                    item(span = StaggeredGridItemSpan.FullLine) {
                        Text(
                            text = "Warehouse Management",
                            style = MaterialTheme.typography.headlineMedium,
                            fontWeight = FontWeight.Bold,
                            color = Color.White,
                            modifier = Modifier.padding(16.dp)
                        )
                    }

                    items(
                        categories.size,
                        key = { categories[it].name },
                    ) { category ->
                        CustomButtonCard (
                            category = categories[category],
                            onClick = {
                                println("Clicked on ${categories[category].name}")
                                when(categories[category].navigation){
                                    "print"  -> onNavigateToFill()//
                                    "reception" -> onNavigateToReception()
                                    "transfer" -> onNavigateToTransfer()
                                    "inventory" -> onNavigateToInventory()
                                    "packingList"-> onNavigateToPackingList()
                                    "production"-> onNavigateToProduction()
                                    "sync"->{viewModel.getDataFromApiManual()}
                                }
                            },
                        )
                    }


                }
            }

            Box(
                modifier = Modifier.padding(top= 32.dp)
            ) {
                CustomAppBar(
                    title = {
                        Image(
                            painter = painterResource(R.drawable.ic_logo3),
                            contentDescription = "Logo",
                            modifier = Modifier.size(150.dp)
                        )
                    },
                    leadingIcon = {
                        IconButton(
                            onClick = {
                                scope.launch {
                                    if(drawerState.isClosed){
                                        drawerState.open()
                                    }else{
                                        drawerState.close()
                                    }
                                }

                            }
                        ) {
                            Icon(
                                painter = painterResource(R.drawable.ic_menu),
                                contentDescription = "User",
                                tint = Color.Black
                            )
                        }
                    },
                    trailingIcon = {
                        Box { // Envuelve IconButton + DropdownMenu en un Box para el anchor
                            IconButton(
                                onClick = {
                                    menuExpanded = true
                                }
                            ) {
                                Icon(
                                    imageVector = Icons.Filled.AccountCircle,
                                    contentDescription = "User",
                                    tint = Color.Black
                                )
                            }

                            DropdownMenu (
                                expanded = menuExpanded,
                                onDismissRequest = { menuExpanded = false }
                            ) {

                                DropdownMenuItem(
                                    text = { Text("Syncronizar") },
                                    trailingIcon = {
                                        Icon(
                                            painter = painterResource(R.drawable.sync_svgrepo_com),
                                            contentDescription = "sync",
                                            tint = Color.Black
                                        )
                                    },
                                    onClick = {
                                        menuExpanded = false
                                        viewModel.getDataFromApiManual()
                                    }
                                )
                                DropdownMenuItem(
                                    text = { Text("Cerrar sesión") },
                                    onClick = {
                                        menuExpanded = false
                                    }
                                )
                            }
                        }
                    },
                )
            }
        }



    }


}

