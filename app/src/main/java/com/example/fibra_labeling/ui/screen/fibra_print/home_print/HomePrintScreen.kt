package com.example.fibra_labeling.ui.screen.fibra_print.home_print


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
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.FilledTonalIconButton
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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.fibra_labeling.R
import com.example.fibra_labeling.ui.navigation.Screen
import com.example.fibra_labeling.ui.screen.fibra_print.home_print.component.CustomPrintCard
import com.example.fibra_labeling.ui.screen.fibrafil.home.component.FioriMenuDrawerSheet
import com.example.fibra_labeling.ui.screen.fibrafil.home.component.HomeCategories
import com.example.fibra_labeling.ui.theme.FioriBackground
import kotlinx.coroutines.launch
import org.koin.androidx.compose.koinViewModel

@OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)
@Composable
fun HomePrintScreen(
    viewModel: HomePrintViewModel = koinViewModel(),
    onNavigateToPrint: () -> Unit,
    onNavigateToReception: () -> Unit,
    onNavigateToTransfer: () -> Unit,
    onNavigateToInventory: () -> Unit,
    onNavigateToPackingList: () -> Unit,
    onNavigateToProduction: () -> Unit,
    onNavigateToSetting: () -> Unit,
    onNavigateToZplScreen: () -> Unit
) {
    val categories = listOf(
        HomeCategories(R.drawable.ic_scan, "Generar etiquetas", Screen.Print.route),
        HomeCategories(R.drawable.ic_report, "Recepción de compras", Screen.Reception.route),
        HomeCategories(R.drawable.ic_transfer, "Tranferencias", Screen.Transfer.route),
        HomeCategories(R.drawable.ic_inventory, "Toma de inventario", Screen.Inventory.route),
        HomeCategories(R.drawable.ic_packing, "PackingList", Screen.PackingList.route),
        HomeCategories(R.drawable.ic_report, "Recibo Producción", Screen.Production.route),
        HomeCategories(R.drawable.sync_svgrepo_com, "Sincronizar", "sync")
    )

    val isSyncing by viewModel.isSyncing.collectAsState()
    val syncMessage by viewModel.syncMessage.collectAsState()

    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()

    val snackbarHostState = remember { SnackbarHostState() }

    var menuExpanded by remember { mutableStateOf(false) }
    var selectedMenu by remember { mutableStateOf("Impresora") }

    val configState by viewModel.configState.collectAsState()

//    if(configState){
//        AlertDialog(
//            onDismissRequest = {},
//            title = { Box(Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
//                Text("Aviso", fontWeight = FontWeight.Bold) } },
//            text = {
//                Column(
//                    modifier = Modifier.fillMaxWidth(),
//                    horizontalAlignment = Alignment.CenterHorizontally
//                ) {
//                    Text(
//                        "configure una impresora antes de continuar",
//                        textAlign = TextAlign.Center
//                    )
//                    Spacer(Modifier.height(16.dp))
//                    FilledTonalButton(
//                        onClick = { onNavigateToSetting() },
//                        colors = ButtonDefaults.filledTonalButtonColors(
//                            containerColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.8f),
//                        ),
//                    ) {
//                        Text("Ir", fontWeight = FontWeight.Bold,color = Color.White)
//                    }
//                }
//            },
//            confirmButton = {}
//        )
//    }

    if (isSyncing) {
        AlertDialog(
            onDismissRequest = {},
            title = { Box(Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
                Text("Sincronizando...", fontWeight = FontWeight.Bold) } },
            text = {
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    CircularProgressIndicator()
                    Spacer(Modifier.height(16.dp))
                    Text(syncMessage)
                }
            },
            confirmButton = {}
        )
    }

    LaunchedEffect(Unit) {
        viewModel.syncError.collect { error ->
            when (error) {
                "errorSync" -> {
                    scope.launch {
                        snackbarHostState.showSnackbar("No se pudo sincronizar. Verifica tu conexión.")
                    }
                }
                "success" -> {
                    scope.launch {
                        snackbarHostState.showSnackbar("Sincronización completada con éxito.")
                    }
                    viewModel.vericarConfiguracion()
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
                    onNavigateToSetting = { onNavigateToSetting() },
                    onNavigateToZplScreen = { onNavigateToZplScreen() }
                )
            }
        },
        drawerState = drawerState
    ) {
        Scaffold(
            topBar = {
                TopAppBar(

                    navigationIcon = {
                        IconButton (
                            onClick = {
                                scope.launch {
                                    if (drawerState.isClosed) drawerState.open()
                                    else drawerState.close()
                                }
                            }
                        ){
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
                                    //viewModel.getDataFromApiManual()
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
                    style = MaterialTheme.typography.headlineSmall.copy(
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.primary
                    ),
                    modifier = Modifier.padding(start = 24.dp, top = 32.dp, bottom = 16.dp)
                )

                LazyVerticalStaggeredGrid(
                    columns = StaggeredGridCells.Adaptive(160.dp),
                    verticalItemSpacing = 20.dp,
                    horizontalArrangement = Arrangement.spacedBy(16.dp),
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
                                    "print" -> onNavigateToPrint()
                                    "reception" -> onNavigateToReception()
                                    "transfer" -> onNavigateToTransfer()
                                    "inventory" -> onNavigateToInventory()
                                    "packingList" -> onNavigateToPackingList()
                                    "production" -> onNavigateToProduction()
                                    "sync" -> {viewModel.getDataFromApiManual()}
                                }
                            },
                        )
                    }
                }
            }

        }


    }
}

@Preview(showBackground = true)
@Composable
fun PreviewHomePrintScreen(){
    HomePrintScreen(
        onNavigateToPrint = {},
        onNavigateToReception = {},
        onNavigateToTransfer = {},
        onNavigateToInventory = {},
        onNavigateToPackingList = {},
        onNavigateToProduction = {},
        onNavigateToSetting = {},
        onNavigateToZplScreen = {}
    )
}