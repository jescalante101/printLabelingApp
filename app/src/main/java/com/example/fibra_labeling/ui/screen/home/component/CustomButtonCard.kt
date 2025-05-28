package com.example.fibra_labeling.ui.screen.home.component

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.slideInVertically
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ShapeDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import kotlin.random.Random


@Composable
fun CustomButtonCard(
    category: HomeCategories, onClick: () -> Unit
){
    var visible by remember { mutableStateOf(false) }
    LaunchedEffect(Unit) {
        visible = true
    }

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,

        ) {

        AnimatedVisibility(
            visible = visible,
            enter = fadeIn(
                animationSpec = tween(500)
            )+ slideInVertically(
                animationSpec = tween(500)
            )
        ) {
            Card(
                modifier = Modifier.animateContentSize(),
                onClick = onClick,
                shape = ShapeDefaults.ExtraLarge,
                colors = CardDefaults.cardColors(Color(
                    Random.nextFloat(),
                    Random.nextFloat(),
                    Random.nextFloat(),0.1f
                )),

                ) {
                Column (
                    modifier = Modifier
                        .padding(36.dp)
                    ,
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement =Arrangement.Center,
                ) {
                    Image(
                        painter = painterResource(id = category.icon),
                        contentDescription = category.name,
                        contentScale = ContentScale.Fit,
                        modifier = Modifier.fillMaxWidth().wrapContentHeight()

                    )
                    Spacer(modifier = Modifier.height(16.dp)) // Espacio entre icono y texto
                    Text(
                        text = category.name,
                        style = MaterialTheme.typography.titleSmall.copy(
                            fontWeight = FontWeight.Black
                        ),
                    )

                }
            }
        }
    }

}

data class HomeCategories(
    val icon: Int,
    val name: String
)