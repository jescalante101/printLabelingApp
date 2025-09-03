package com.example.fibra_labeling.fibrafil.ui.screen.inventario.register.stock

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
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.fibra_labeling.R
import org.koin.androidx.compose.koinViewModel

@Composable
fun ICountingScreen(
    onSave: (value:String,stock: Double) -> Unit = {_,_ ->},
    viewModel: StockViewModel = koinViewModel(),
    product:String = "Pernos",
    itemCode:String = "123456789",
    whsCode:String = "CH3-RE",
    conteo: Int = 0,
    onNavigateBack: () -> Unit = {}
){

    var cantidad by remember { mutableStateOf("0") }
    val stock by viewModel.stock.collectAsState()

    LaunchedEffect (Unit){
        viewModel.getStock()
        viewModel.setCodigo(itemCode)
        viewModel.setWhsCode(whsCode)
        cantidad=conteo.toString()
    }

    Box(
        modifier = Modifier
            .wrapContentHeight()
    ) {

        Column (
            modifier = Modifier

                .background(Color.White)
                .padding(top = 10.dp, start = 16.dp, end = 16.dp)
        ) {
            // Título
            Text(product, fontWeight = FontWeight.Bold,)
            Spacer(Modifier.height(4.dp))
            // Subtítulo o ID

            Spacer(Modifier.height(16.dp))

            // Indicadores de stock y cantidad contada
            Row (
                Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(stock.onHand.toString(), fontWeight = FontWeight.Bold, fontSize = 32.sp, color = Color(0xFF2E7D32))
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
                            painter = painterResource(R.drawable.ic_print),
                            contentDescription = "print",
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
                    listOf("x", "0", "\uD83D\uDCBE")
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
                                    }else if(label == "\uD83D\uDCBE"){
                                        onSave(cantidad, stock.onHand?.toDouble() ?: 00.0)
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
                                        textAlign = TextAlign.Center
                                    )
                                }
                            }

                        }
                    }
                    Spacer(Modifier.height(8.dp))
                }
            }
        }

//
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewIcounterScreen(){
    ICountingScreen(
        onSave = {_,_->}
    )
}
