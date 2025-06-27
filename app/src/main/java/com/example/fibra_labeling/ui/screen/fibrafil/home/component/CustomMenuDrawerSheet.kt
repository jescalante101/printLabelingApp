package com.example.fibra_labeling.ui.screen.fibrafil.home.component

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.fibra_labeling.R
@Composable
fun FioriMenuDrawerSheet(
    selectedMenu: String,
    onSelect: (String) -> Unit,
    onNavigateToSetting: () -> Unit,
    onNavigateToZplScreen: () -> Unit
) {
    val fioriBlue = Color(0xFF0A6ED1)
    val sectionBg = Color.White
    val sectionText = Color(0xFF687087)
    val fioriBackground = Color(0xFFF7F7F7)

    Column(
        modifier = Modifier
            .fillMaxHeight()
            .background(fioriBackground) // Color claro, SIN gradiente
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 0.dp)
    ) {
        Box(
            Modifier
                .fillMaxWidth()
                .background(sectionBg, shape = RoundedCornerShape(bottomEnd = 28.dp, bottomStart = 28.dp))
                .padding(vertical = 30.dp),
            contentAlignment = Alignment.Center
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Box(
                    Modifier
                        .size(56.dp)
                        .clip(CircleShape)
                        .background(fioriBlue)
                ) {
                    Icon(
                        painter = painterResource(R.drawable.ic_print),
                        contentDescription = "Logo",
                        modifier = Modifier
                            .align(Alignment.Center)
                            .size(34.dp),
                        tint = Color.White
                    )
                }
                Spacer(Modifier.height(9.dp))
                Text("Bienvenido", color = sectionText, style = MaterialTheme.typography.labelSmall)
                Text(
                    "Usuario Fibrafil",
                    color = Color.Black,
                    fontWeight = FontWeight.Bold,
                    style = MaterialTheme.typography.titleMedium
                )
            }
        }

        Spacer(Modifier.height(18.dp))
        // SECCIÓN 1
        Text(
            "GESTIÓN",
            modifier = Modifier.padding(start = 28.dp, bottom = 4.dp),
            color = fioriBlue,
            style = MaterialTheme.typography.labelSmall,
            fontWeight = FontWeight.Bold
        )

        CustomDrawerMenuItem(
            label = "Impresora",
            icon = R.drawable.ic_print,
            selected = selectedMenu == "Impresora",
            onClick = {
                onSelect("Impresora")
                onNavigateToSetting()
            }
        )
        CustomDrawerMenuItem(
            label = "Etiquetas ZPL",
            icon = R.drawable.ic_doc,
            selected = selectedMenu == "Etiquetas ZPL",
            onClick = {
                onSelect("Etiquetas ZPL")
                onNavigateToZplScreen()
            }
        )

        Spacer(Modifier.height(18.dp))
        Divider(modifier = Modifier.padding(horizontal = 24.dp), color = Color(0xFFE7EAF3))
        Spacer(Modifier.height(14.dp))

        // SECCIÓN 2
        Text(
            "SOPORTE",
            modifier = Modifier.padding(start = 28.dp, bottom = 4.dp),
            color = fioriBlue,
            style = MaterialTheme.typography.labelSmall,
            fontWeight = FontWeight.Bold
        )

        CustomDrawerMenuItem(
            label = "Configuración",
            icon = R.drawable.ic_setting,
            selected = selectedMenu == "Configuración",
            onClick = { onSelect("Configuración") }
        )
        CustomDrawerMenuItem(
            label = "Ayuda y feedback",
            icon = R.drawable.ic_help,
            selected = selectedMenu == "Ayuda y feedback",
            onClick = { onSelect("Ayuda y feedback") }
        )

        Spacer(Modifier.height(24.dp))
    }
}

@Composable
fun CustomDrawerMenuItem(
    label: String,
    icon: Int,
    selected: Boolean,
    onClick: () -> Unit
) {
    val fioriBlue = Color(0xFF0A6ED1)
    val selectedBg = Color(0xFFE3F0FA) // Azul muy claro para selección
    val shape = RoundedCornerShape(16.dp)
    val bg = if (selected) selectedBg else Color.Transparent
    val contentColor = if (selected) fioriBlue else Color(0xFF2B2F38)
    val fontWeight = if (selected) FontWeight.Bold else FontWeight.Normal

    Surface(
        color = bg,
        shape = shape,
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 10.dp, vertical = 2.dp)
            .height(52.dp)
            .clip(shape)
            .clickable { onClick() }
    ) {
        Row(
            Modifier
                .fillMaxSize()
                .padding(start = 18.dp, end = 12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(painter = painterResource(icon), contentDescription = null, tint = contentColor)
            Spacer(Modifier.width(18.dp))
            Text(
                label,
                color = contentColor,
                fontWeight = fontWeight,
                style = MaterialTheme.typography.bodyLarge
            )
        }
    }
}
