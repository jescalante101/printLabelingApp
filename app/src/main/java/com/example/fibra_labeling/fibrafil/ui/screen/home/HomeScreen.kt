package com.example.fibra_labeling.fibrafil.ui.screen.home

import android.annotation.SuppressLint
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
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
import androidx.compose.material3.TopAppBar
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
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.compose.ui.res.stringResource
import com.example.fibra_labeling.R
import com.example.fibra_labeling.fibrafil.navigation.FibraFilDestination
import com.example.fibra_labeling.fibrafil.ui.screen.home.component.FioriMenuDrawerSheet
import com.example.fibra_labeling.core.navigation.SettingsDestination
import com.example.fibra_labeling.fibraprint.ui.screen.home_print.component.CustomPrintCard
import com.example.fibra_labeling.fibrafil.ui.screen.home.component.HomeCategories
import com.example.fibra_labeling.ui.theme.FioriBackground
import kotlinx.coroutines.launch
import org.koin.androidx.compose.koinViewModel

@SuppressLint("ConfigurationScreenWidthHeight")
@OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)
@Composable
fun HomeScreen(
    viewModel: HomeViewModel = koinViewModel(),
    navController: NavController,
    onNavigateToSettings: ((SettingsDestination) -> Unit)? = null
){
    val categories = listOf(
        HomeCategories(R.drawable.ic_scan, stringResource(R.string.category_generate_labels), "print"),
        HomeCategories(R.drawable.ic_report, stringResource(R.string.category_warehouse_entry), "reception"),
        HomeCategories(R.drawable.ic_transfer, stringResource(R.string.category_transfers), "transfer"),
        HomeCategories(R.drawable.ic_inventory, stringResource(R.string.category_inventory), "inventory"),
        HomeCategories(R.drawable.ic_packing, stringResource(R.string.category_packing_list), "packingList"),
        HomeCategories(R.drawable.ic_report, stringResource(R.string.category_production_receipt), "production"),
        HomeCategories(R.drawable.sync_svgrepo_com, stringResource(R.string.category_sync), "sync")
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

    // Opción 1: Usar Configuration para detectar el tamaño de pantalla
    val configuration = LocalConfiguration.current
    val screenWidthDp = configuration.screenWidthDp.dp
    val isSmallScreen = screenWidthDp < 600.dp

    // Calcular el tamaño de celda basado en el ancho de pantalla
    val cellSize = when {
        screenWidthDp < 320.dp -> 100.dp  // Pantallas muy pequeñas
        screenWidthDp < 400.dp -> 140.dp  // Pantallas pequeñas
        else -> 160.dp                    // Pantallas medianas/grandes
    }
    val verticalSpacing = if (isSmallScreen) 12.dp else 20.dp
    val horizontalSpacing = if (isSmallScreen) 8.dp else 16.dp


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
                FioriMenuDrawerSheet(
                    selectedMenu = selectedMenu,
                    onSelect = { selectedMenu = it },
                    navController = navController,
                    onNavigateToSettings = onNavigateToSettings
                )
            }
        },
        drawerState = drawerState
    ) {
        Scaffold(
            topBar = {
                TopAppBar(
                    navigationIcon = {
                        IconButton(
                            onClick = {
                                scope.launch {
                                    if (drawerState.isClosed) drawerState.open()
                                    else drawerState.close()
                                }
                            }
                        ) {
                            Icon(
                                painter = painterResource(R.drawable.ic_menu),
                                contentDescription = "Menu",
                                tint = MaterialTheme.colorScheme.primary
                            )
                        }
                    },
                    title = {
                        Image(
                            painter = painterResource(R.drawable.ic_logo3),
                            contentDescription = "Logo",
                            modifier = Modifier.size(150.dp)
                        )
                    },
                    actions = {
                        IconButton(onClick = { menuExpanded = true }) {
                            Icon(
                                painter = painterResource(R.drawable.ic_user),
                                contentDescription = "user",
                                tint = MaterialTheme.colorScheme.primary
                            )
                        }
                        DropdownMenu(
                            expanded = menuExpanded,
                            onDismissRequest = { menuExpanded = false }
                        ) {
                            DropdownMenuItem(
                                text = { Text("Sincronizar") },
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
                )
            },
            containerColor = FioriBackground,
            snackbarHost = { SnackbarHost(snackbarHostState) }
        ) { padding ->
            Column(
                modifier = Modifier
                    .padding(padding)
                    .fillMaxSize()
            ) {
                Text(
                    text = "Warehouse Management",
                    style = MaterialTheme.typography.headlineMedium.copy(
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.primary
                    ),
                    modifier = Modifier.padding(start = 24.dp, top = 12.dp, bottom = 16.dp).
                    fillMaxWidth(),
                    textAlign = TextAlign.Center
                )

                LazyVerticalStaggeredGrid(
                    columns = StaggeredGridCells.Adaptive(cellSize),
                    verticalItemSpacing = verticalSpacing,
                    horizontalArrangement = Arrangement.spacedBy(horizontalSpacing),
                    modifier = Modifier
                        .padding(horizontal = 20.dp)
                        .fillMaxSize()
                ) {
                    items(
                        categories.size,
                        key = { categories[it].name },
                    ) { index ->
                        CustomPrintCard(
                            category = categories[index],
                            onClick = {
                                when (categories[index].navigation) {
                                    "print" -> navController.navigate(FibraFilDestination.Labels.PrintMain.route)
                                    "reception" -> navController.navigate(FibraFilDestination.Reception.Menu.route)
                                    "transfer" -> {
                                        // Legacy route - could be moved to shared or removed
                                        //navController.navigate("transfer")
                                    }
                                    "inventory" -> navController.navigate(FibraFilDestination.Inventory.RegisterHeader.route)
                                    "packingList" -> {
                                        // Legacy route - could be moved to shared or removed  
                                        //navController.navigate("packingList")
                                    }
                                    "production" -> {
                                        // Legacy route - could be moved to shared or removed
                                        //navController.navigate("production")
                                    }
                                    "sync" -> { viewModel.getDataFromApiManual() }
                                }
                            },
                        )
                    }
                }
            }
        }
    }


}

