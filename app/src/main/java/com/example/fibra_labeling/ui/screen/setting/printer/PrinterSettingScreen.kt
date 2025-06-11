package com.example.fibra_labeling.ui.screen.setting.printer

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.fibra_labeling.ui.screen.setting.printer.form.isFormValid
import com.example.fibra_labeling.ui.theme.Fibra_labelingTheme
import org.koin.androidx.compose.koinViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PrintSettingScreen(
    onBack: () -> Unit,
    viewModel: PrinterSettingScreenViewModel = koinViewModel()
) {
    val formState = viewModel.formState
    val errorState = viewModel.errorState
    val loading = viewModel.loading
    val resultMsg = viewModel.resultMsg

    val snackbarHostState = remember { SnackbarHostState() }

    // Mostrar Snackbar solo cuando resultMsg cambia
    LaunchedEffect(resultMsg) {
        resultMsg?.let {
            snackbarHostState.showSnackbar(it)
            viewModel.clearResultMsg()
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                colors = TopAppBarDefaults.topAppBarColors(Color(0XFFDDE9F7)),
                title = { Text("Configuración de impresora") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(
                            painterResource(com.example.fibra_labeling.R.drawable.ic_arrow_left),
                            contentDescription = "Back"
                        )
                    }
                }
            )
        },
        snackbarHost = { SnackbarHost(snackbarHostState) }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp)
                .padding(innerPadding),
            verticalArrangement = Arrangement.Top
        ) {
            Spacer(Modifier.height(8.dp))
            OutlinedTextField(
                value = formState.printerName,
                onValueChange = viewModel::onPrinterNameChange,
                label = { Text("Nombre de la impresora") },
                isError = errorState.printerNameError != null,
                singleLine = true,
                modifier = Modifier.fillMaxWidth(),
                placeholder = { Text("Ej: Zebra ZT230") }
            )
            errorState.printerNameError?.let {
                Text(it, color = MaterialTheme.colorScheme.error, style = MaterialTheme.typography.labelMedium)
            }
            Spacer(Modifier.height(20.dp))
            OutlinedTextField(
                value = formState.ip,
                onValueChange = viewModel::onIpChange,
                label = { Text("Dirección IP de la impresora") },
                isError = errorState.ipError != null,
                singleLine = true,
                modifier = Modifier.fillMaxWidth(),
                placeholder = { Text("Ej: 192.168.1.150") }
            )
            errorState.ipError?.let {
                Text(it, color = MaterialTheme.colorScheme.error, style = MaterialTheme.typography.labelMedium)
            }
            Spacer(Modifier.height(16.dp))
            OutlinedTextField(
                value = formState.port,
                onValueChange = viewModel::onPortChange,
                label = { Text("Puerto") },
                isError = errorState.portError != null,
                singleLine = true,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                modifier = Modifier.fillMaxWidth(),
                placeholder = { Text("9100") }
            )
            errorState.portError?.let {
                Text(it, color = MaterialTheme.colorScheme.error, style = MaterialTheme.typography.labelMedium)
            }
            Spacer(Modifier.height(20.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Button(
                    onClick = { viewModel.probarConexion() },
                    enabled = !loading
                ) { Text("Probar conexión") }

                Button(
                    onClick = { viewModel.guardar() },
                    enabled = isFormValid(errorState) && !loading
                ) { Text("Guardar") }
            }
            if (loading) {
                Spacer(Modifier.height(16.dp))
                LinearProgressIndicator(modifier = Modifier.fillMaxWidth())
            }
        }
    }
}


@Preview(showBackground = true)
@Composable
fun PreviewSetting(){
    Fibra_labelingTheme {
        PrintSettingScreen(
            onBack = {}
        )
    }
}