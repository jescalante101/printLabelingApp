package com.example.fibra_labeling.ui.screen.home

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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Card
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
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
import com.example.fibra_labeling.ui.screen.home.component.CustomButtonCard
import com.example.fibra_labeling.ui.screen.home.component.HomeCategories
import com.example.fibra_labeling.ui.screen.home.component.HomeHeader
import org.koin.androidx.compose.koinViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    viewModel: HomeViewModel= koinViewModel(),
    onNavigateToPrint: () -> Unit ,
    onNavigateToReception: () -> Unit,
    onNavigateToTransfer: () -> Unit,
    onNavigateToInventory: () -> Unit,
    onNavigateToPackingList: () -> Unit,
){
    val categories = listOf(
        HomeCategories(R.drawable.ic_scan, "Generar etiquetas",Screen.Print.route),
        HomeCategories(R.drawable.ic_report,"Recepción de compras",Screen.Reception.route),
        HomeCategories(R.drawable.ic_transfer, "Tranferencias",Screen.Transfer.route),
        HomeCategories(R.drawable.ic_inventory, "Toma de inventario",Screen.Inventory.route),
        HomeCategories(R.drawable.ic_packing, "PackingList",Screen.PackingList.route)
    )

    val gradientBrush = Brush.verticalGradient(
        colors = listOf(
            Color(0xFF4D9BB0), // Azul SAP Fiori
            Color(0xFF0F3F5B)  // Gris oscuro SAP Fiori
        )
    )

    Box(
        modifier = Modifier.fillMaxSize()
            .background(gradientBrush)
    ) {
        Scaffold(
            containerColor = Color.Transparent,
        ) { padding ->
            LazyVerticalStaggeredGrid(
                columns = StaggeredGridCells.Adaptive(150.dp),
                verticalItemSpacing = 24.dp,
//                horizontalArrangement = Arrangement.spacedBy(24.dp),
                modifier = Modifier.padding(padding)
            ) {
                item(span = StaggeredGridItemSpan.FullLine) {
                    Spacer(modifier = Modifier.height(1.dp))
                }

                item(span = StaggeredGridItemSpan.FullLine) {
                    CustomAppBar(
                        title = {Text(
                            "Fibra App",
                            color = Color.Black,
                        )},
                        leadingIcon = {
                            IconButton(
                                onClick = {}
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
                                onClick = {}
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

                item(span = StaggeredGridItemSpan.FullLine) {
                    Text(
                        text = "Warehouse Management",
                        style = MaterialTheme.typography.headlineMedium,
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
                                "print"  -> onNavigateToPrint()
                                "reception" -> onNavigateToReception()
                                "transfer" -> onNavigateToTransfer()
                                "inventory" -> onNavigateToInventory()
                                "packingList"-> onNavigateToPackingList()
                            }
                        },
                    )
                }


            }
        }
    }


}

