package com.example.fibra_labeling.shared.ui.screen.welcome

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.core.MutableTransitionState
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.slideInHorizontally
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.fibra_labeling.shared.ui.screen.welcome.component.EmpresaCard
import com.example.fibra_labeling.ui.theme.LightColors
import com.example.fibra_labeling.R
import org.koin.androidx.compose.koinViewModel

@OptIn(ExperimentalAnimationApi::class, ExperimentalMaterial3Api::class)
@Composable
fun WelcomeScreen(
    onEmpresaSeleccionada: (String) -> Unit,
    viewModel: WelcomeViewModel = koinViewModel()
) {
    val transitionState = remember { MutableTransitionState(false).apply { targetState = true } }

    Surface(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    listOf(
                        LightColors.background,
                        LightColors.primaryContainer.copy(alpha = 0.1f)
                    )
                )
            ),
        color = Color.Transparent
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp)
        ) {
            Column(
                modifier = Modifier
                    .align(Alignment.Center)
                    .fillMaxWidth(),
                horizontalAlignment = Alignment.Start
            ) {
                AnimatedVisibility(
                    visibleState = transitionState,
                    enter = fadeIn(tween(600))
                ) {
                    Column {
                        Text(
                            text = "¡Bienvenido!",
                            style = MaterialTheme.typography.headlineLarge,
                            fontWeight = FontWeight.Bold,
                            color = LightColors.primary,
                            textAlign = TextAlign.Start
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = "A grupo fibrafil",
                            fontSize = 24.sp,
                            fontWeight = FontWeight.Medium,
                            color = LightColors.onSurface,
                            textAlign = TextAlign.Start
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = "Seleccione una empresa para gestionar el almacén",
                            style = MaterialTheme.typography.bodyMedium,
                            fontWeight = FontWeight.Normal,
                            color = LightColors.onSurface,
                            textAlign = TextAlign.Start
                        )
                    }
                }
            }

            Column(
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .padding(bottom = 32.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                AnimatedVisibility(
                    visibleState = transitionState,
                    enter = fadeIn(tween(800)) + slideInHorizontally(initialOffsetX = { it }),
                ) {
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(16.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        EmpresaCard(
                            nombre = "Fibrafil",
                            descripcion = "Producción y almacén",
                            onClick = {
                                viewModel.saveEmpresa("Fibrafil")
                                onEmpresaSeleccionada("Fibrafil")
                            },
                            backgroundColor = LightColors.primary,
                            textColor = LightColors.surface,
                            modifier = Modifier.weight(1f),
                            icon = painterResource(R.drawable.ic_logofibra_fil)
                        )
                        EmpresaCard(
                            nombre = "Fibraprint",
                            descripcion = "Diseño e impresión",
                            onClick = {
                                viewModel.saveEmpresa("FibraPrint")
                                onEmpresaSeleccionada("FibraPrint")
                            },
                            backgroundColor = LightColors.background,
                            textColor = LightColors.onSurface,
                            modifier = Modifier.weight(1f),
                            icon = painterResource(R.drawable.ic_logofibra_print)
                        )
                    }
                }
            }


        }
    }
}


@Preview
@Composable
fun PreviewWelcome() {
    WelcomeScreen(
        onEmpresaSeleccionada = {},
    )
}
