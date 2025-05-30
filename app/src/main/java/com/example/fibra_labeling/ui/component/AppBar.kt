package com.example.fibra_labeling.ui.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.fibra_labeling.R
import com.example.fibra_labeling.ui.theme.Fibra_labelingTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CustomAppBar(
    modifier: Modifier = Modifier,
    leadingIcon: @Composable (() -> Unit)? = null,
    title: @Composable () -> Unit,
    trailingIcon: @Composable (() -> Unit)? = null,
) {
    Surface (
        color = Color(0XFFDDE9F7),
        shadowElevation = 8.dp // sombra/elevación
    ) {
        Row (
            modifier = modifier
                .fillMaxWidth()
                .height(56.dp) // altura estándar de appbar
                .padding(horizontal = 8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Leading Icon
            Box(
                Modifier.size(40.dp), // espacio para icono a la izquierda
                contentAlignment = Alignment.Center
            ) {
                leadingIcon?.invoke()
            }

            // Title (ocupa el espacio restante)
            Box(
                modifier = Modifier
                    .weight(1f)
                    .padding(horizontal = 8.dp),
                contentAlignment = Alignment.CenterStart
            ) {
                title()
            }

            // Trailing Icon
            Box(
                Modifier.size(40.dp), // espacio para icono a la derecha
                contentAlignment = Alignment.Center
            ) {
                trailingIcon?.invoke()
            }
        }
    }
}

@Preview
@Composable
fun PreviewAppBar(){
    Fibra_labelingTheme{
        Scaffold(
            topBar = {
                CustomAppBar(
                    title = {
                        Image(
                            painter = painterResource(R.drawable.ic_logo2),
                            contentDescription = "Logo",
                            modifier = Modifier.size(150.dp)
                        )
                    },
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
                               painter = painterResource(R.drawable.ic_user),
                               contentDescription = "User",
                               tint = Color.Black
                           )
                       }
                    },

                )
            }

        ) { padding ->
            Column (
                modifier = Modifier.padding(padding)
            ) {

            }
        }
    }
}