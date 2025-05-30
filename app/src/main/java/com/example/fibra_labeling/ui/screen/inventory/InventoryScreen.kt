package com.example.fibra_labeling.ui.screen.inventory

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.fibra_labeling.R
import com.example.fibra_labeling.ui.component.CustomAppBar
import com.example.fibra_labeling.ui.screen.inventory.component.CustomItemCard
import com.example.fibra_labeling.ui.util.gradientBrush

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun InventoryScreen(
    onNavigateBack: () -> Unit,
    onNavigateToCounting: () -> Unit
){
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(gradientBrush)
    ) {



        Scaffold(
            containerColor = Color.Transparent,
        ) {
            LazyColumn (
                modifier = Modifier
                    .fillMaxSize().padding(it).padding(top = 56.dp)
            ) {

                item {
                    OutlinedTextField(
                        shape = MaterialTheme.shapes.medium,
                        value = "",
                        onValueChange = {

                        },
                        label = {
                            Text(
                                "Buscar",
                                color = Color.White,
                                style = MaterialTheme.typography.bodyMedium,
                                fontWeight = FontWeight.Black
                            )
                        },
                        maxLines = 1,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp),
                        leadingIcon = {
                            Icon(
                                painter = painterResource(R.drawable.barcode_scan),
                                contentDescription = "Barcode Icon",
                                tint = Color.White
                            )
                        },
                        trailingIcon = {
                            IconButton(
                                onClick = {
                                    onNavigateToCounting()
                                }
                            ) {
                                Icon(
                                    painter = painterResource(R.drawable.ic_search),
                                    contentDescription = "Search",
                                    tint = Color.White
                                )
                            }
                        },

                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = Color.White,
                            unfocusedBorderColor = Color.White,
                            cursorColor = Color.White,
                            focusedContainerColor = Color.DarkGray
                        )
                    )
                }

                item {
                    Spacer(
                        modifier = Modifier.height(8.dp)
                    )
                }

                items(
                    count =10
                ) {
                    AnimatedVisibility(
                        visible = true,
                        enter = fadeIn(),
                        exit = fadeOut()
                    ) {
                        CustomItemCard(
                            modifier = Modifier.animateItem(),
                            onClick = {
                                onNavigateToCounting()
                            }
                        )
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

}