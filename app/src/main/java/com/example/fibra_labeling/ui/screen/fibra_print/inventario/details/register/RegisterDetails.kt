package com.example.fibra_labeling.ui.screen.fibra_print.inventario.details.register

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.FilledTonalIconButton
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.rememberNestedScrollInteropConnection
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import com.example.fibra_labeling.R
import com.example.fibra_labeling.ui.screen.component.CustomFioriTextField
import com.example.fibra_labeling.ui.theme.FioriBackground
import org.koin.androidx.compose.koinViewModel


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PrintRegisterIncDetailsScreen(
    onBackNavigation: () -> Unit,
    viewModel: PrintRegisterIncDetailsViewModel= koinViewModel(),
    itemCode: String,
    itemName: String,
    codeBar: String,
) {

    val formState by viewModel.formState.collectAsState()


    LaunchedEffect (Unit) {
        viewModel.setCodigo(itemCode)
        viewModel.setNombreProducto(itemName)
        viewModel.setCodeBar(codeBar)
    }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        "Registrar Detalle",
                        style = MaterialTheme.typography.titleLarge,
                        // Considera usar un color del tema, como:
                        color = MaterialTheme.colorScheme.onSurface,
                        fontWeight = FontWeight.Bold
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onBackNavigation) { // Acción a ejecutar al hacer clic
                        Icon(
                            painter = painterResource(id = R.drawable.ic_arrow_left), // Tu recurso drawable
                            contentDescription = "Atrás", // Descripción para accesibilidad
                            tint = MaterialTheme.colorScheme.primary // Tinte del ícono
                        )
                    }
                },
            )
        },
        floatingActionButton = {
            ExtendedFloatingActionButton (
                onClick = {
                    //viewModel.save()
                },
            ){
                Text(
                    text = "Guardar",
                    style = MaterialTheme.typography.labelLarge,
                    color = Color.White,
                    fontWeight = FontWeight.Bold
                )
            }
        },
        containerColor = FioriBackground
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .fillMaxWidth()
                .padding(paddingValues)
                .padding(horizontal = 24.dp, vertical = 16.dp)
                .nestedScroll(rememberNestedScrollInteropConnection())
                .imePadding()
        ) {
            item {
                Spacer(Modifier.height(16.dp))
            }

            item{
                Text(
                    buildAnnotatedString {
                        withStyle(style= SpanStyle(color = Color.Black)) {
                            append("Código:")
                        }
                        withStyle(style= SpanStyle(color = Color.Black.copy(0.6f))) {
                            append(formState.codigo)
                        }
                    },
                    style = MaterialTheme.typography.labelSmall,
                )
            }

            item {
                Text(
                    buildAnnotatedString {
                        withStyle(style= SpanStyle(color = Color.Black)) {
                            append("Producto:")
                        }
                        withStyle(style= SpanStyle(color = Color.Black.copy(0.6f))) {
                            append(formState.nombreProducto)
                        }
                    },
                    style = MaterialTheme.typography.labelSmall,
                )
            }
            item {
                Text(
                    buildAnnotatedString {
                        withStyle(style= SpanStyle(color = Color.Black)) {
                            append("Proveedor:")
                        }
                        withStyle(style= SpanStyle(color = Color.Black.copy(0.6f))) {
                            append(formState.proveedor?.cardName)
                        }
                    },
                    style = MaterialTheme.typography.labelSmall,
                )
            }

            item {
                Text(
                    buildAnnotatedString {
                        withStyle(style= SpanStyle(color = Color.Black)) {
                            append("Almacén:")
                        }
                        withStyle(style= SpanStyle(color = Color.Black.copy(0.6f))) {
                            append(formState.almacen?.whsName)
                        }
                    },
                    style = MaterialTheme.typography.labelSmall,
                )
            }

            item {
                Spacer(Modifier.height(16.dp))

            }

            item{
                CustomFioriTextField(
                    label = "Observacion",
                    value = formState.observacion,
                    onValueChange = {
                        viewModel.onObservacionChange(it)
                    },
                    enabled = true,
                    modifier = Modifier.fillMaxWidth(),

                    )
            }

            item {
                Spacer(Modifier.height(16.dp))
            }

            item {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Row {
                        FilledTonalIconButton (
                            colors = IconButtonDefaults.iconButtonColors(
                                MaterialTheme.colorScheme.primary.copy(0.6f)
                            ),
                            onClick = {
                                viewModel.decrementarConteo()
                            }
                        ) {
                            Icon(
                                painter = painterResource(R.drawable.ic_minus),
                                contentDescription = "minus",

                                )
                        }
                        Spacer(Modifier.width(4.dp))
                        FilledTonalIconButton(
                            colors = IconButtonDefaults.iconButtonColors(
                                MaterialTheme.colorScheme.primary.copy(0.6f)
                            ),
                            onClick = {
                                viewModel.incrementarConteo()
                            }
                        ) {
                            Icon(
                                painter = painterResource(R.drawable.ic_plus),
                                contentDescription = "plus",

                                )
                        }
                    }
                    Spacer(Modifier.width(8.dp))
                    CustomFioriTextField(
                        label = "Conteo",
                        value = formState.conteo,
                        onValueChange = {
                            viewModel.onConteoChange(it)
                        },
                        enabled = false,
                        modifier = Modifier.weight(1f).clickable(
                            onClick = {
                                //showSheet = true
                            }
                        ),
                        isOnlyNumber = true
                    )
                    Spacer(Modifier.width(8.dp))
                }
            }
        }
    }
}