package com.example.fibra_labeling.ui.screen.inventory

import FioriCardConteoCompact
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.fibra_labeling.R
import com.example.fibra_labeling.ui.component.CustomAppBar
import com.example.fibra_labeling.ui.screen.component.CustomSearch
import com.example.fibra_labeling.ui.screen.inventory.register.OncRegisterScreen
import org.koin.androidx.compose.koinViewModel


@Composable
fun RegisterCabecera(
    onNavigateBack: () -> Unit,
    onNavigateToFilEtiqueta: (isPrint: Boolean) -> Unit,
    viewModel: RegisterCabeceraViewModel = koinViewModel(),
    onNavigateToDetails: ()-> Unit
){

    var showSheet by remember { mutableStateOf(false) }

    val oincs by viewModel.oincs.collectAsState()

    Box {
        Scaffold (
            floatingActionButton = {
                Column {
                    ExtendedFloatingActionButton (
                        onClick = {
                            showSheet = true
                        },
                        containerColor = Color(0xFF2C3E50),
                    ) {
                        Text("Nuevo",color = Color.White)
                    }
                }
            }
        ){
            LazyColumn (
                modifier = Modifier
                    .fillMaxSize().padding(it).padding(top = 56.dp)
            ) {
                item {
                    Spacer(modifier = Modifier.height(16.dp))
                }
                item {
                    CustomSearch(
                        value = "",
                        onChangeValue = {},
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
                                onClick = {
                                    //onNavigateToCounting()
                                }
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
                }
                item {
                    Spacer(
                        modifier = Modifier.height(8.dp)
                    )
                }

                items(oincs.size) { index ->
                    AnimatedVisibility(
                        visible = true,
                        enter = fadeIn(),
                        exit = fadeOut()
                    ) {
                        val oinc = oincs[index]
                        FioriCardConteoCompact(
                            dto = oinc,
                            onClick = {
                                //Navegate to App
                                viewModel.saveUserLogin(
                                    userLogin = oinc.u_UserNameCount ?: "",
                                    code = oinc.u_Ref ?: "",
                                    docEntry = oinc.docEntry.toString()
                                )
                                onNavigateToFilEtiqueta(false)
                            },
                            onDetailsClick = {onNavigateToDetails()}
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

    OncRegisterScreen (
        showSheet = showSheet,
        onDismiss = { showSheet = false },
        onSave = {

        }
    )



}

@Preview
@Composable
fun PreviewRegisterCabecera() {
    RegisterCabecera(
        onNavigateBack = {},
        onNavigateToFilEtiqueta = {},
        onNavigateToDetails = {}

    )
}