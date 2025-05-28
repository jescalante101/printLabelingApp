package com.example.fibra_labeling

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.navigation.compose.rememberNavController
import com.example.fibra_labeling.ui.navigation.AppNavHost
import com.example.fibra_labeling.ui.navigation.Screen
import com.example.fibra_labeling.ui.screen.home.HomeScreen
import com.example.fibra_labeling.ui.theme.Fibra_labelingTheme

class MainActivity : ComponentActivity() {
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
}

