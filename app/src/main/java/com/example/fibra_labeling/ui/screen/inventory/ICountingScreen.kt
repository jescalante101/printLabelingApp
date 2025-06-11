package com.example.fibra_labeling.ui.screen.inventory

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.fibra_labeling.R
import com.example.fibra_labeling.ui.component.CustomAppBar
import com.example.fibra_labeling.ui.util.gradientBrush

@Composable
fun ICountingScreen(
    onNavigateBack: () -> Unit
){

    var cantidad by remember { mutableStateOf("0") }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(gradientBrush)
    ) {

        Scaffold(
            containerColor = Color.Transparent,
//            floatingActionButton = {
//                FloatingActionButton(
//                    onClick = {
//
//                    },
//                    containerColor = MaterialTheme.colorScheme.surfaceTint,
//                    shape = CircleShape
//                ) {
//                    Row(
//                        modifier = Modifier.padding(horizontal = 16.dp),
//                    ) {
//                        Text("Guardar", color = Color.White, fontSize = 16.sp)
//                        Spacer(Modifier.width(4.dp))
//                        Icon(
//                            painter = painterResource(R.drawable.ic_save),
//                            contentDescription = "Carrito",
//                            tint = Color.White
//                        )
//                    }
//                }
//            }
        ) {
            Column (
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.White)
                    .padding(it)
                    .padding(top = 65.dp, start = 16.dp, end = 16.dp)
            ) {
                // Título
                Text("Esencias vegetales", fontWeight = FontWeight.Bold, fontSize = 20.sp)
                Spacer(Modifier.height(4.dp))
                // Subtítulo o ID
                Text("1", fontSize = 16.sp, color = Color.Gray)
                Spacer(Modifier.height(16.dp))

                // Indicadores de stock y cantidad contada
                Row (
                    Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text("1 850 ", fontWeight = FontWeight.Bold, fontSize = 32.sp, color = Color(0xFF2E7D32))
                        Text("Stock", color = Color.Gray)
                    }
                    Box(
                        Modifier
                            .height(50.dp)
                            .width(1.dp)
                            .background(Color(0xFFE0E0E0))
                    )
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text("${cantidad} ", fontWeight = FontWeight.Bold, fontSize = 32.sp, color = Color(0xFFD84315))
                        Text("Cantidad contada", color = Color.Gray)
                    }
                }

                Spacer(Modifier.height(24.dp))

                // Fila de iconos circulares
                Row(
                    Modifier
                        .fillMaxWidth()
                        .background(Color(0xFFE3F2FD))
                        .padding(vertical = 8.dp),
                    horizontalArrangement = Arrangement.SpaceEvenly,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // Icono "#"
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Box(
                            contentAlignment = Alignment.Center,
                            modifier = Modifier
                                .size(48.dp)
                                .background(Color(0xFF2196F3), CircleShape)
                        ) {
                            IconButton(
                                onClick = {}
                            ) {
                                Text("#", color = Color.White, fontSize = 28.sp, fontWeight = FontWeight.Bold)
                            }
                        }
                        Spacer(Modifier.height(2.dp))
                        Box(
                            Modifier
                                .height(3.dp)
                                .width(32.dp)
                                .background(Color(0xFF2196F3), RoundedCornerShape(2.dp))
                        )
                    }
                    // Icono "i"
                    Box(
                        contentAlignment = Alignment.Center,
                        modifier = Modifier
                            .size(48.dp)
                            .border(2.dp, Color(0xFF2196F3), CircleShape)
                    ) {
                        IconButton(
                            onClick = {}
                        ) {
                            Text("i", color = Color(0xFF2196F3), fontSize = 28.sp, fontWeight = FontWeight.Bold)
                        }
                    }

                    Box(
                        contentAlignment = Alignment.Center,
                        modifier = Modifier
                            .size(48.dp)
                            .border(2.dp, Color(0xFF2196F3), CircleShape)
                    ) {
                        IconButton(
                            onClick = {}
                        ) {
                            Icon(
                                painter = painterResource(R.drawable.ic_store),
                                contentDescription = "Tienda",
                                tint = Color(0xFF2196F3),
                                modifier = Modifier.size(28.dp)
                            )
                        }
                    }
                }

                Spacer(Modifier.height(24.dp))

                // Teclado numérico 3x4
                Column(
                    Modifier.fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    val numbers = listOf(
                        listOf("1", "2", "3"),
                        listOf("4", "5", "6"),
                        listOf("7", "8", "9"),
                        listOf("x", "0", "«")
                    )
                    numbers.forEach { row ->
                        Row(
                            Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceEvenly
                        ) {
                            row.forEach { label ->
                                Card(
                                    modifier = Modifier
                                        .size(56.dp)
                                        ,
                                    colors = CardDefaults.cardColors(
                                        containerColor = if(label=="«") Color.Green else Color.White
                                    ),
                                    elevation = CardDefaults.cardElevation(
                                        defaultElevation = 4.dp
                                    ),
                                    onClick = {
                                        if(label == "x"){
                                            cantidad = if (cantidad.length == 1){
                                                "0"
                                            }else{
                                                cantidad.dropLast(1)
                                            }
                                        }else if(label == "«"){
                                            Log.d("TAG", "ICountingScreen: $cantidad")
                                        }else{
                                            if(cantidad == "0"){
                                                cantidad = label
                                            }else{
                                                cantidad += label
                                            }
                                        }
                                    }
                                ) {
                                    Box(
                                        modifier = Modifier.fillMaxSize(),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        Text(
                                            label,
                                            fontSize = 24.sp,
                                            textAlign = androidx.compose.ui.text.style.TextAlign.Center
                                        )
                                    }
                                }

                            }
                        }
                        Spacer(Modifier.height(8.dp))
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

