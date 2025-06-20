import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

import com.example.fibra_labeling.data.local.entity.fibrafil.ZplLabel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ZplRegister(
    label: ZplLabel? = null,
    onSave: (ZplLabel) -> Unit,
    onCancel: () -> Unit
) {
    var codeName by remember { mutableStateOf(label?.codeName ?: "") }
    var name by remember { mutableStateOf(label?.name ?: "") }
    var description by remember { mutableStateOf(label?.description ?: "") }
    var zplFile by remember { mutableStateOf(label?.zplFile ?: "") }

    val title = if (label == null) "Crear Template ZPL" else "Editar Template ZPL"
    val enabled = codeName.isNotBlank() && name.isNotBlank() && description.isNotBlank() && zplFile.isNotBlank()

    val background = Color(0xFFF7F7F7)
    val card = Color.White
    val primary = Color(0xFF0070F2)
    val secondary = Color(0xFFE0E3E5)

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        title,
                        fontWeight = FontWeight.Bold,
                        fontSize = 22.sp,
                        color = Color.Black
                    )
                },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    containerColor = background,
                    titleContentColor = Color.Black
                )
            )
        },
        containerColor = background
    ) { padding ->
        Box(
            Modifier
                .fillMaxSize()
                .padding(padding),
            contentAlignment = Alignment.TopCenter
        ) {
            Card(
                colors = CardDefaults.cardColors(containerColor = card),
                shape = RoundedCornerShape(16.dp),
                elevation = CardDefaults.cardElevation(defaultElevation = 8.dp),
                modifier = Modifier
                    .widthIn(max = 600.dp)
                    .fillMaxWidth()
                    .padding(top = 32.dp, start = 16.dp, end = 16.dp, bottom = 16.dp)
            ) {
                Column(
                    Modifier
                        .padding(32.dp)
                        .verticalScroll(rememberScrollState())
                ) {
                    Text("Información del Template", fontWeight = FontWeight.Bold, fontSize = 18.sp)
                    Spacer(Modifier.height(18.dp))

                    OutlinedTextField(
                        value = codeName,
                        onValueChange = { codeName = it },
                        label = { Text("Nombre de código (único)") },
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = primary,
                            unfocusedBorderColor = secondary,
                            cursorColor = primary
                        ),
                        textStyle = LocalTextStyle.current.copy(
                            fontWeight = FontWeight.Medium,
                            letterSpacing = 0.5.sp
                        )
                    )
                    Spacer(Modifier.height(14.dp))

                    OutlinedTextField(
                        value = name,
                        onValueChange = { name = it },
                        label = { Text("Nombre para mostrar") },
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = primary,
                            unfocusedBorderColor = secondary,
                            cursorColor = primary
                        ),
                        textStyle = LocalTextStyle.current.copy(
                            fontWeight = FontWeight.Medium,
                            letterSpacing = 0.5.sp
                        )
                    )
                    Spacer(Modifier.height(14.dp))

                    OutlinedTextField(
                        value = description,
                        onValueChange = { description = it },
                        label = { Text("Descripción") },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = primary,
                            unfocusedBorderColor = secondary,
                            cursorColor = primary
                        ),
                        textStyle = LocalTextStyle.current.copy(letterSpacing = 0.5.sp)
                    )
                    Spacer(Modifier.height(22.dp))

                    Text("Archivo de plantilla ZPL", fontWeight = FontWeight.Medium)
                    Spacer(Modifier.height(6.dp))

                    OutlinedTextField(
                        value = zplFile,
                        onValueChange = { zplFile = it },
                        label = { Text("Pega aquí el código ZPL") },
                        modifier = Modifier
                            .fillMaxWidth()
                            .heightIn(min = 150.dp, max = 300.dp),
                        shape = RoundedCornerShape(12.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = primary,
                            unfocusedBorderColor = secondary,
                            cursorColor = primary
                        ),
                        textStyle = LocalTextStyle.current.copy(
                            fontFamily = FontFamily.Monospace,
                            fontSize = 15.sp,
                            lineHeight = 19.sp
                        ),
                        maxLines = 10,
                        singleLine = false
                    )

                    Spacer(Modifier.height(28.dp))

                    Row(
                        Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.End
                    ) {
                        TextButton(
                            onClick = onCancel,
                            modifier = Modifier.padding(end = 8.dp)
                        ) {
                            Text("Cancelar", color = Color.Gray)
                        }
                        Button(
                            onClick = {
                                onSave(
                                    ZplLabel(
                                        id = label?.id ?: 0,
                                        codeName = codeName.trim(),
                                        name = name.trim(),
                                        description = description.trim(),
                                        zplFile = zplFile.trim()
                                    )
                                )
                            },
                            enabled = enabled,
                            shape = RoundedCornerShape(12.dp),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = primary,
                                contentColor = Color.White,
                                disabledContainerColor = secondary,
                                disabledContentColor = Color.LightGray
                            )
                        ) {
                            Text(if (label == null) "Registrar" else "Actualizar", fontWeight = FontWeight.Bold)
                        }
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewREgister(){
    ZplRegister(
        onSave = {},
        onCancel = {}
    )

}
