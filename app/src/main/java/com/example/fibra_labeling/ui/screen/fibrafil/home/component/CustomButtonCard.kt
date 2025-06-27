package com.example.fibra_labeling.ui.screen.fibrafil.home.component

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.slideInVertically
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.fibra_labeling.R
import com.example.fibra_labeling.ui.navigation.Screen
import com.example.fibra_labeling.ui.theme.Fibra_labelingTheme


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
        modifier = Modifier.padding(horizontal = 16.dp)
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

            ) {
                Column (
                    modifier = Modifier
                        .padding(28.dp).height(120.dp)
                    ,
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement =Arrangement.Center,
                ) {
                    Icon(
                        painter = painterResource(id = category.icon),
                        contentDescription = category.name,
                        modifier = Modifier.fillMaxWidth().wrapContentHeight().size(36.dp),

                    )
                    Spacer(modifier = Modifier.height(12.dp)) // Espacio entre icono y texto
                    Text(
                        text = category.name,
                        style = MaterialTheme.typography.titleSmall.copy(
                            fontWeight = FontWeight.Black

                        ),

                        textAlign = TextAlign.Center
                    )

                }
            }
        }
    }

}

@Preview
@Composable
fun PreviewCustomButtonCard() {
    Fibra_labelingTheme {
        Column(modifier = Modifier.padding(16.dp).fillMaxSize()) {
            CustomButtonCard(
                category = HomeCategories(
                    icon = R.drawable.ic_menu,
                    name = "Home",
                    navigation = Screen.Home.route
                ),
                onClick = {}
            )
        }
    }
}





data class HomeCategories(
    val icon: Int,
    val name: String,
    val navigation: String
)