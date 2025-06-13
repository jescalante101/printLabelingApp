package com.example.fibra_labeling.ui.screen.home

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.staggeredgrid.LazyVerticalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridItemSpan
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.List
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material3.Card
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.DrawerDefaults
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.fibra_labeling.R
import com.example.fibra_labeling.ui.component.CustomAppBar
import com.example.fibra_labeling.ui.navigation.Screen
import com.example.fibra_labeling.ui.screen.home.component.CustomBottomSheetMenu
import com.example.fibra_labeling.ui.screen.home.component.CustomButtonCard
import com.example.fibra_labeling.ui.screen.home.component.HomeCategories
import com.example.fibra_labeling.ui.screen.home.component.HomeHeader
import com.example.fibra_labeling.ui.util.gradientBrush
import kotlinx.coroutines.launch
import org.koin.androidx.compose.koinViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    viewModel: HomeViewModel= koinViewModel(),
    onNavigateToPrint: () -> Unit ,
    onNavigateToFill: () -> Unit,
    onNavigateToReception: () -> Unit,
    onNavigateToTransfer: () -> Unit,
    onNavigateToInventory: () -> Unit,
    onNavigateToPackingList: () -> Unit,
    onNavigateToProduction: () -> Unit,
    onNavigateToSetting: () -> Unit
){
    val categories = listOf(
        HomeCategories(R.drawable.ic_scan, "Generar etiquetas",Screen.Print.route),
        HomeCategories(R.drawable.ic_report,"Recepción de compras",Screen.Reception.route),
        HomeCategories(R.drawable.ic_transfer, "Tranferencias",Screen.Transfer.route),
        HomeCategories(R.drawable.ic_inventory, "Toma de inventario",Screen.Inventory.route),
        HomeCategories(R.drawable.ic_packing, "PackingList",Screen.PackingList.route),
        HomeCategories(R.drawable.ic_report, "Recibo Producción",Screen.Production.route)
    )



    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()

    var showSheet by remember { mutableStateOf(false) }

    ModalNavigationDrawer(
        drawerContent = {
            ModalDrawerSheet(
                modifier = Modifier
                    .background(gradientBrush),
                drawerContainerColor = Color.Transparent
            ){

                Column(
                    modifier = Modifier.padding(horizontal = 16.dp)
                        .verticalScroll(rememberScrollState())
                ) {
                    Spacer(Modifier.height(12.dp))
                    Text("Opciones",
                        modifier = Modifier.padding(16.dp),
                        style = MaterialTheme.typography.titleLarge,
                        color = Color.White
                    )
                    HorizontalDivider()

                    Text("Section 1", modifier = Modifier.padding(16.dp), style = MaterialTheme.typography.titleMedium)
                    NavigationDrawerItem(
                        label = { Text("Impresora") },
                        selected = false,
                        onClick = {
                            onNavigateToSetting()
                        }
                    )
                    NavigationDrawerItem(
                        label = { Text("Item 2") },
                        selected = false,
                        onClick = { /* Handle click */ }
                    )

                    HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp))

                    Text("Section 2", modifier = Modifier.padding(16.dp), style = MaterialTheme.typography.titleMedium)
                    NavigationDrawerItem(
                        label = { Text("Settings") },
                        selected = false,
                        icon = { Icon(Icons.Outlined.Settings, contentDescription = null) },
                        badge = { Text("20") }, // Placeholder
                        onClick = { /* Handle click */ }
                    )
                    NavigationDrawerItem(
                        label = { Text("Help and feedback") },
                        selected = false,
                        icon = { Icon(Icons.AutoMirrored.Outlined.List, contentDescription = null) },
                        onClick = { /* Handle click */ },
                    )
                    Spacer(Modifier.height(12.dp))
                }
            }
        },
        drawerState = drawerState

    ) {


        Box(
            modifier = Modifier.fillMaxSize()
                .background(gradientBrush)
        ) {

            Scaffold(
                containerColor = Color.Transparent,
            ) { padding ->
                LazyVerticalStaggeredGrid(
                    columns = StaggeredGridCells.Adaptive(160.dp),
                    verticalItemSpacing = 16.dp,
//                horizontalArrangement = Arrangement.spacedBy(24.dp),
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
                        IconButton(
                            onClick = {

                            }
                        ) {
                            Icon(
                                imageVector = Icons.Filled.AccountCircle,
                                contentDescription = "User",
                                tint = Color.Black
                            )
                        }
                    },

                    )
            }


        }


        CustomBottomSheetMenu (
            onFibraFilClick = { onNavigateToFill() },
            onFibraPrintClick = { onNavigateToPrint() },
            showSheet = showSheet,
            onDismiss = { showSheet = false }
        )

    }



}

