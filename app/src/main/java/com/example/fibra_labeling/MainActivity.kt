package com.example.fibra_labeling

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.KeyEvent
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.getValue
import androidx.navigation.compose.rememberNavController
import com.example.fibra_labeling.datastore.ThemeManager
import com.example.fibra_labeling.datastore.ThemeMode
import com.example.fibra_labeling.ui.BarcodeViewModel
import com.example.fibra_labeling.core.navigation.MainAppNavHost
import com.example.fibra_labeling.core.navigation.AppDestination
import com.example.fibra_labeling.ui.theme.Fibra_labelingTheme
import org.koin.androidx.viewmodel.ext.android.viewModel

class MainActivity : ComponentActivity() {
    private val barcodeViewModel: BarcodeViewModel by viewModel()
    private lateinit var themeManager: ThemeManager


    @SuppressLint("ContextCastToActivity")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        themeManager = ThemeManager(this)
        setContent {
            val themeMode by themeManager.themeMode
            val darkTheme = when (themeMode) {
                ThemeMode.LIGHT -> false
                ThemeMode.DARK -> true
                ThemeMode.SYSTEM -> isSystemInDarkTheme()
            }
            Fibra_labelingTheme(darkTheme = darkTheme) {

               val navController = rememberNavController()
                MainAppNavHost(
                    navController = navController,
                    startDestination = AppDestination.Welcome.route
                )
            }
        }
    }

    @SuppressLint("RestrictedApi")
    override fun superDispatchKeyEvent(event: KeyEvent): Boolean {
        Log.d("KEYEVET", "Código escaneado: ${event}")
        Log.d("KEYEVET", "Código escaneado1: ${event.scanCode}")
        Log.d("KEYEVET", "Código escaneado2: ${event.characters}")
        Log.d("KEYEVET", "Código escaneado3: ${event.keyCode}")
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

