package com.example.fibra_labeling.ui.screen.home

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
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
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Card
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.fibra_labeling.R
import com.example.fibra_labeling.ui.screen.home.component.CustomButtonCard
import com.example.fibra_labeling.ui.screen.home.component.HomeCategories
import com.example.fibra_labeling.ui.screen.home.component.HomeHeader
import org.koin.androidx.compose.koinViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    viewModel: HomeViewModel= koinViewModel(),
    onNavigateToPrint: () -> Unit ,
    onNavigateToReport: () -> Unit,
    onNavigateToProfile: () -> Unit,
    onNavigateToSettings: () -> Unit,
){
    val categories = listOf(
        HomeCategories(R.drawable.ic_scan, "Scan"),
        HomeCategories(R.drawable.ic_report,"Report"),
//        HomeCategories(R.drawable.ic_user, "Profile"),
        HomeCategories(R.drawable.ic_setting, "Settings"),
        HomeCategories(R.drawable.ic_logout, "Logout")
    )

    Scaffold(
//        topBar = {
//            CenterAlignedTopAppBar(
//                title = { Text("Labeling") }
//            )
//        }
    ) { padding ->
        LazyVerticalStaggeredGrid(
            columns = StaggeredGridCells.Adaptive(180.dp),
            verticalItemSpacing = 24.dp,
            horizontalArrangement = Arrangement.spacedBy(24.dp),
            modifier = Modifier.padding(padding)
        ) {
            item(span = StaggeredGridItemSpan.FullLine) {
                HomeHeader()
            }

            item {
                OutlinedCard (
                    modifier = Modifier.fillMaxWidth(),
                    onClick = {},
                    shape = MaterialTheme.shapes.extraLarge
                ) {
                    Row(
                        modifier = Modifier.padding(16.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Search,
                            contentDescription = "Search",
                            modifier = Modifier.padding(8.dp)
                        )
                        Text(
                            text = "Search",
                        )
                    }
                }
            }

            items(
                categories.size,
                key = { categories[it].name },
            ) { category ->
                CustomButtonCard (
                    category = categories[category],
                    onClick = {
                        println("Clicked on ${categories[category].name}")
                        when(categories[category].name){
                            "Scan" -> onNavigateToPrint()
                            "Report" -> onNavigateToReport()
                            "Profile" -> onNavigateToProfile()
                            "Settings" -> onNavigateToSettings()
                        }
                    },
                )
            }


        }
    }
}

