package com.example.fibra_labeling.core.navigation

import BarcodeScannerScreen
import ZplTemplateScreen
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.scaleIn
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.fibra_labeling.fibrafil.navigation.FibraFilNavHost
import com.example.fibra_labeling.fibrafil.navigation.FibraFilDestination
import com.example.fibra_labeling.fibraprint.navigation.FibraPrintNavHost
import com.example.fibra_labeling.shared.ui.screen.setting.general.GeneralSettingScreen
import com.example.fibra_labeling.shared.ui.screen.setting.printer.PrintSettingScreen
import com.example.fibra_labeling.shared.ui.screen.setting.servidor.ServerSettingScreen
import com.example.fibra_labeling.shared.ui.screen.welcome.WelcomeScreen
import com.example.fibra_labeling.fibraprint.ui.screen.etiqueta.register.PrintRegisterScreen
import com.example.fibra_labeling.shared.ui.screen.reception.ReceptionScreen
import com.example.fibra_labeling.fibrafil.ui.screen.etiqueta.etiquetanueva.AddEtiquetaScreen

@Composable
fun MainAppNavHost(
    navController: NavHostController = rememberNavController(),
    startDestination: String = AppDestination.Welcome.route
) {
    NavHost(
        navController = navController,
        startDestination = startDestination
    ) {
        
        // Welcome/Company Selection Screen
        composable(
            AppDestination.Welcome.route,
            enterTransition = {
                fadeIn(animationSpec = tween(700)) +
                        scaleIn(initialScale = 0.9f, animationSpec = tween(700))
            }
        ) {
            WelcomeScreen(
                onEmpresaSeleccionada = { empresa ->
                    when (empresa) {
                        "FibraPrint" -> {
                            navController.navigate("fibraprint") {
                                popUpTo(AppDestination.Welcome.route) { inclusive = true }
                            }
                        }
                        "Fibrafil" -> {
                            navController.navigate("fibrafil") {
                                popUpTo(AppDestination.Welcome.route) { inclusive = true }
                            }
                        }
                    }
                }
            )
        }
        
        // FibraFil Module - Complete navigation graph
        composable("fibrafil") {
            val fibraFilNavController = rememberNavController()
            FibraFilNavHost(
                navController = fibraFilNavController,
                onNavigateToSettings = { settingsDestination ->
                    navController.navigate("settings/${settingsDestination.route}")
                },
                onNavigateToShared = { sharedDestination ->
                    when (sharedDestination) {
                        is SharedDestination.BarcodeScanner -> 
                            navController.navigate("shared/${sharedDestination.route}")
                        is SharedDestination.Login -> 
                            navController.navigate("shared/${sharedDestination.route}")
                    }
                },
                onNavigateToPrintRegister = { isPrint ->
                    navController.navigate("shared/print_register/$isPrint")
                }
            )
        }
        
        // FibraPrint Module - Complete navigation graph  
        composable("fibraprint") {
            val fibraPrintNavController = rememberNavController()
            FibraPrintNavHost(
                navController = fibraPrintNavController,
                onNavigateToSettings = { settingsDestination ->
                    navController.navigate("settings/${settingsDestination.route}")
                },
                onNavigateToShared = { sharedDestination ->
                    when (sharedDestination) {
                        is SharedDestination.BarcodeScanner -> 
                            navController.navigate("shared/${sharedDestination.route}")
                        is SharedDestination.Login -> 
                            navController.navigate("shared/${sharedDestination.route}")
                    }
                }
            )
        }
        
        // Settings Module
        composable("settings/${SettingsDestination.Printer.route}") {
            PrintSettingScreen (
                onBack = { navController.popBackStack() },
                onNavigateTozplTemplate = {
                    navController.navigate("settings/${SettingsDestination.ZplTemplate.route}")
                }
            )
        }
        
        composable("settings/${SettingsDestination.ZplTemplate.route}") {
            ZplTemplateScreen(
                onBack = { navController.popBackStack() }
            )
        }
        
        composable("settings/${SettingsDestination.Server.route}") {
            ServerSettingScreen(
                onBack = { navController.popBackStack() }
            )
        }
        
        composable("settings/${SettingsDestination.General.route}") {
            GeneralSettingScreen(
                onBack = { navController.popBackStack() }
            )
        }
        
        // Shared Module - Cross-company functionality
        composable("shared/${SharedDestination.BarcodeScanner.route}") {
            BarcodeScannerScreen(
                onBack = { navController.popBackStack() },
                navController = navController
            )
        }
        
        composable(
            "shared/print_register/{isPrint}",
            arguments = listOf(navArgument("isPrint") { type = NavType.BoolType })
        ) { backStackEntry ->
            val isPrint = backStackEntry.arguments?.getBoolean("isPrint") ?: false
            PrintRegisterScreen(
                onBack = { navController.popBackStack() },
                onNavigateToNewPrint = { code, name ->
                    // This might need to navigate back to specific company flow
                    navController.popBackStack()
                },
                isPrint = isPrint,
                onNavigateToFillPrint = { itemCode, productName ->
                    // Navigate back to FibraFil new label screen
                    navController.navigate(
                        "fibrafil/" + FibraFilDestination.Labels.NewLabel.createRoute(itemCode, productName)
                    )
                }
            )
        }
        
        composable("shared/reception") {
            ReceptionScreen(
                onNavigateBack = { navController.popBackStack() }
            )
        }
        
        // FibraFil cross-module routes
        composable(
            route = "fibrafil/labels/${FibraFilDestination.Labels.NewLabel.ROUTE_TEMPLATE}",
            arguments = FibraFilDestination.Labels.NewLabel.arguments
        ) { backStackEntry ->
            val itemCode = backStackEntry.arguments?.getString(
                FibraFilDestination.Labels.NewLabel.ARG_ITEM_CODE
            ) ?: ""
            val productName = backStackEntry.arguments?.getString(
                FibraFilDestination.Labels.NewLabel.ARG_PRODUCT_NAME
            ) ?: ""
            
            AddEtiquetaScreen(
                onBack = { navController.popBackStack() },
                itemCode = itemCode,
                productName = productName,
                navController = navController
            )
        }
    }
}