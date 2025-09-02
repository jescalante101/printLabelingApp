package com.example.fibra_labeling.ui.screen.fibra_print.reception

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.staggeredgrid.LazyVerticalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.fibra_labeling.R
import com.example.fibra_labeling.ui.navigation.Screen
import com.example.fibra_labeling.ui.screen.fibra_print.home_print.component.CustomPrintCard
import com.example.fibra_labeling.ui.screen.fibrafil.home.component.HomeCategories
import com.example.fibra_labeling.ui.theme.FioriBackground

@OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)
@Composable
fun ReceptionMenuPrintScreen(
    navController: NavController,
    onNavigateBack: () -> Unit
) {
    val categories = listOf(
        HomeCategories(R.drawable.ic_report, stringResource(R.string.reception_purchase), "purchase"),
        HomeCategories(R.drawable.warehouse_svgrepo, stringResource(R.string.reception_warehouse_entry), "warehouse"),
        HomeCategories(R.drawable.ic_receipt, stringResource(R.string.reception_return), "return")
    )

    val configuration = LocalConfiguration.current
    val screenWidthDp = configuration.screenWidthDp.dp
    val isSmallScreen = screenWidthDp < 600.dp

    val cellSize = when {
        screenWidthDp < 320.dp -> 100.dp
        screenWidthDp < 400.dp -> 140.dp
        else -> 160.dp
    }

    val verticalSpacing = if (isSmallScreen) 12.dp else 20.dp
    val horizontalSpacing = if (isSmallScreen) 8.dp else 16.dp

    Scaffold(
        topBar = {
            TopAppBar(
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(
                            painter = painterResource(R.drawable.ic_arrow_left),
                            contentDescription = "Back",
                            tint = MaterialTheme.colorScheme.primary
                        )
                    }
                },
                title = {
                    Text(
                        text = stringResource(R.string.reception_menu_title),
                        style = MaterialTheme.typography.titleLarge.copy(
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.primary
                        )
                    )
                }
            )
        },
        containerColor = FioriBackground
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize()
        ) {
//            Text(
//                text = stringResource(R.string.reception_menu_title),
//                style = MaterialTheme.typography.headlineMedium.copy(
//                    fontWeight = FontWeight.Bold,
//                    color = MaterialTheme.colorScheme.primary
//                ),
//                modifier = Modifier
//                    .padding(start = 24.dp, top = 12.dp, bottom = 16.dp)
//                    .fillMaxWidth(),
//                textAlign = TextAlign.Center
//            )

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
                                "purchase" -> navController.navigate(Screen.Reception.route)
                                "warehouse" -> navController.navigate(Screen.Reception.route)
                                "return" -> navController.navigate(Screen.Reception.route)
                            }
                        }
                    )
                }
            }
        }
    }
}