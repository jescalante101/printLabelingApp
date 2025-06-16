package com.example.fibra_labeling.ui.screen.print.register

import android.net.Uri
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.intl.Locale
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.toUpperCase
import androidx.compose.ui.unit.dp
import com.example.fibra_labeling.R
import com.example.fibra_labeling.data.local.mapper.toOitmData
import com.example.fibra_labeling.ui.component.CustomAppBar
import com.example.fibra_labeling.ui.screen.print.register.component.ProductCard
import com.example.fibra_labeling.ui.screen.print.register.component.SearchBar
import org.koin.androidx.compose.koinViewModel


@Composable
fun PrintRegisterScreen(
    onBack: () -> Unit,
    onNavigateToNewPrint: (code: String, name: String) -> Unit,
    onNavigateToFillPrint: (itemCode:String,productName:String) -> Unit,
    viewModel: RegisterViewModel= koinViewModel(),
    isPrint: Boolean=true,

) {

    val oitmResult by viewModel.oitmResponse.collectAsState()
    val loading by viewModel.loading.collectAsState()
    val totalResult by viewModel.totalResult.collectAsState()

    var searchText by remember { mutableStateOf("") }

    val productos by viewModel.productos.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.changeIsPrint(isPrint)
        viewModel.getOitm(isFill = !isPrint)
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
    ) {
        Scaffold(
            containerColor = Color.Transparent,
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color(0xFFF5F5F5))
                    .padding(16.dp)
                    .padding(it)
            ) {

                Spacer(modifier = Modifier
                    .height(60.dp)
                    .padding(bottom = 16.dp))

                // Campo de búsqueda
                SearchBar(
                    searchText = searchText,
                    onSearchTextChange = {
                        searchText = it.toString().toUpperCase(Locale.current)
                        viewModel.setFiltro(it)
                    },
                    modifier = Modifier.padding(bottom = 16.dp),
                    onSearch = {
                        if (isPrint){
                            viewModel.getOitm(filter = it, isFill = false)
                        }
                        viewModel.setFiltro(it)
                    },
                    onDone = {
                        if (isPrint){
                            viewModel.getOitm(filter = searchText, isFill = false)
                        }
                        viewModel.setFiltro(searchText)
                    }
                )

                // Contador de resultados
                Text(
                    text = "${productos.size} producto(s) encontrado(s)",
                    style = MaterialTheme.typography.bodyMedium,
                    color = Color(0xFF7F8C8D),
                    modifier = Modifier.padding(bottom = 8.dp)
                )

                if (!isPrint){
                    if (productos.isEmpty()){
                        EmptyResultsMessage()
                    }else{
                        LazyColumn(
                            verticalArrangement = Arrangement.spacedBy(12.dp),
                            contentPadding = PaddingValues(vertical = 8.dp)
                        ) {

                            items(productos.size) { index ->
                                val product = productos[index]
                                ProductCard(
                                    producto = product.toOitmData()!!,
                                    modifier = Modifier.animateItem(),
                                    onNavigateToDetail = {
                                        val encodedProductName = Uri.encode(it.desc)
                                        val code=Uri.encode(it.codesap)
                                        if (isPrint){
                                            onNavigateToNewPrint(code,encodedProductName)
                                        }else{
                                            onNavigateToFillPrint(code,encodedProductName)
                                        }

                                    }

                                )
                            }
                        }

                    }
                }else{
                    if (productos.isEmpty()){
                        CircularProgressIndicator(
                            modifier = Modifier.align(Alignment.CenterHorizontally)
                        )
                    }else{
                        when{
                            oitmResult.isFailure -> {
                                val errorMsg = oitmResult.exceptionOrNull()?.message ?: "Error desconocido"
                                var msj=errorMsg.toString()
                                if(errorMsg.toString().contains("Connection timed out")){
                                    msj="La conexión falló. Es posible que el servidor esté experimentando dificultades o que tu conexión a internet no sea estable. Revisa tu conexión y vuelve a intentarlo."
                                }

                                Text(
                                    text = msj,
                                    color = MaterialTheme.colorScheme.error,
                                    modifier = Modifier.padding(16.dp)
                                )
                                Spacer(modifier = Modifier.height(16.dp))
                                Button(
                                    onClick = {
                                        viewModel.getOitm(searchText)
                                    },
                                    shape = RoundedCornerShape(8.dp),
                                    colors = ButtonDefaults.buttonColors(
                                        containerColor = Color(0xFF2C3E50),
                                        contentColor = Color.White
                                    ),
                                    modifier = Modifier.align(Alignment.CenterHorizontally)
                                ) {
                                    Text("Reintentar")
                                }
                            }
                            oitmResult.isSuccess && oitmResult.getOrNull() != null -> {
                                val data = oitmResult.getOrNull()
                                if (data != null && data.data?.isNotEmpty() == true){


                                }
                            }
                        }
                    }
                }



            }

        }

        Box(
            modifier = Modifier.padding(top= 32.dp)
        ){
            CustomAppBar(
                title = { Text("Productos", color = Color.Black, style = MaterialTheme.typography.titleMedium ) },
                leadingIcon = {
                    IconButton(
                        onClick = {
                            viewModel.updateUser()
                            onBack()
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



@Composable
fun EmptyResultsMessage() {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(32.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Icon(
            imageVector = Icons.Default.Search,
            contentDescription = "Sin resultados",
            tint = Color(0xFF95A5A6),
            modifier = Modifier.size(64.dp)
        )

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = "No se encontraron productos",
            style = MaterialTheme.typography.titleMedium,
            color = Color(0xFF7F8C8D),
            textAlign = TextAlign.Center
        )

        Text(
            text = "Intenta con otros términos de búsqueda",
            style = MaterialTheme.typography.bodyMedium,
            color = Color(0xFF95A5A6),
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(top = 4.dp)
        )
    }
}

