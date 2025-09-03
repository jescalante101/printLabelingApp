package com.example.fibra_labeling.shared.ui.screen.production

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import com.example.fibra_labeling.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProductionScreen(
    onNavigateBack: () -> Unit
){
    Scaffold (
        topBar = {
            TopAppBar(
                navigationIcon = {
                    IconButton(
                        onClick = {
                            onNavigateBack()
                        }
                    ) {
                        Icon(
                            painter = painterResource(R.drawable.ic_arrow_left),
                            contentDescription = "Back"
                        )
                    }
                },
                title = { Text("Production Screen") }
            )
        }
    ){
            padding ->
        Column (
            modifier = Modifier.padding(padding),

            ){
            Text("Production Screen")
        }
    }
}