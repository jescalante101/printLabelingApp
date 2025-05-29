package com.example.fibra_labeling

import android.annotation.SuppressLint
import android.content.Context
import android.content.pm.PackageManager
import android.hardware.usb.UsbManager
import android.os.Bundle
import android.util.Log
import android.view.KeyEvent
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.ui.platform.LocalGraphicsContext
import androidx.navigation.compose.rememberNavController
import com.example.fibra_labeling.ui.BarcodeViewModel
import com.example.fibra_labeling.ui.navigation.AppNavHost
import com.example.fibra_labeling.ui.navigation.Screen
import com.example.fibra_labeling.ui.screen.home.HomeScreen
import com.example.fibra_labeling.ui.theme.Fibra_labelingTheme
import org.koin.androidx.viewmodel.ext.android.viewModel

class MainActivity : ComponentActivity() {


    private val barcodeViewModel: BarcodeViewModel by viewModel()

    private var barcodeBuffer = StringBuilder()



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()


        setContent {
            Fibra_labelingTheme {
               val navController = rememberNavController()
                AppNavHost(
                    navController = navController,
                    startDestination = Screen.Home.route
                )
            }
        }
    }

    @SuppressLint("RestrictedApi")
    override fun superDispatchKeyEvent(event: KeyEvent): Boolean {

        if (event.action == KeyEvent.ACTION_MULTIPLE && event.characters != null) {
            val scannedCode = event.characters // Aquí tienes el código de barras completo
            Log.d("KEYEVET", "Código escaneado: $scannedCode")
            // Envía al ViewModel si quieres:
            barcodeViewModel.onBarcodeScanned(scannedCode)
            return true // Consumir el evento
        }
        // ... (opcional: aquí puedes manejar otros tipos de KeyEvent si lo deseas)
        return super.superDispatchKeyEvent(event)
    }

}

