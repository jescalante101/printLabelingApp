package com.example.fibra_labeling.fibraprint.navigation

import BarcodeScannerScreen
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.fibra_labeling.core.navigation.SharedDestination
import com.example.fibra_labeling.core.navigation.SettingsDestination
import com.example.fibra_labeling.fibraprint.ui.screen.etiqueta.impresion.PrintScreen
import com.example.fibra_labeling.fibraprint.ui.screen.etiqueta.products.PrintProductScreen
import com.example.fibra_labeling.fibraprint.ui.screen.etiqueta.register.NewPrintScreen
import com.example.fibra_labeling.fibraprint.ui.screen.etiqueta.register.PrintRegisterScreen
import com.example.fibra_labeling.fibraprint.ui.screen.home_print.HomePrintScreen
import com.example.fibra_labeling.fibraprint.ui.screen.inventario.PrintOncScreen
import com.example.fibra_labeling.fibraprint.ui.screen.inventario.details.PrintIncScreen
import com.example.fibra_labeling.fibraprint.ui.screen.inventario.details.register.PrintRegisterIncDetailsScreen
import com.example.fibra_labeling.fibraprint.ui.screen.inventario.product.PrintPesajeScreen
import com.example.fibra_labeling.fibraprint.ui.screen.reception.ReceptionMenuPrintScreen

@Composable
fun FibraPrintNavHost(
    navController: NavHostController,
    startDestination: String = FibraPrintDestination.Home.route,
    onNavigateToSettings: ((SettingsDestination) -> Unit)? = null,
    onNavigateToShared: ((SharedDestination) -> Unit)? = null
) {
    NavHost(
        navController = navController,
        startDestination = startDestination,
        enterTransition = {
            fadeIn(animationSpec = tween(700)) +
                    scaleIn(initialScale = 0.9f, animationSpec = tween(700))
        },
        exitTransition = {
            fadeOut(animationSpec = tween(300)) +
                    scaleOut(targetScale = 1.2f, animationSpec = tween(500))
        }
    ) {
        
        // Home Screen
        composable(FibraPrintDestination.Home.route) {
            HomePrintScreen(
                navController = navController,
                onNavigateToSettings = onNavigateToSettings
            )
        }
        
        // Print Module
        composable(FibraPrintDestination.Print.Main.route) {
            PrintScreen(
                onBack = { navController.popBackStack() },
                onNavigateToScan = { 
                    onNavigateToShared?.invoke(SharedDestination.BarcodeScanner)
                },
                navController = navController,
                onNavigateToRegister = { 
                    navController.navigate(FibraPrintDestination.Print.Products.route) 
                },
                onNavigateToSettings = onNavigateToSettings
            )
        }
        
        composable(FibraPrintDestination.Print.Products.route) {
            PrintProductScreen(
                onNavigateToNewPrint = { code, name, unidad ->
                    navController.navigate(
                        FibraPrintDestination.Print.NewPrint.createRoute(code, name, unidad)
                    )
                },
                onBack = { navController.popBackStack() }
            )
        }
        
        composable(
            route = FibraPrintDestination.Print.Register.getRoute(),
            arguments = FibraPrintDestination.Print.Register.arguments
        ) { backStackEntry ->
            val isPrint = backStackEntry.arguments?.getBoolean(
                FibraPrintDestination.Print.Register.ARG_IS_PRINT
            ) == true
            
            PrintRegisterScreen(
                onBack = { navController.popBackStack() },
                onNavigateToNewPrint = { code, name ->
                    navController.navigate(
                        FibraPrintDestination.Print.NewPrint.createRoute(code, name, "")
                    )
                },
                isPrint = isPrint,
                onNavigateToFillPrint = { itemCode, productName ->
                    // Navigate to FibraFil's new label screen (cross-company navigation)
                    navController.navigate("fibrafil/labels/new/$itemCode/$productName")
                }
            )
        }
        
        composable(
            route = FibraPrintDestination.Print.NewPrint.getRoute(),
            arguments = FibraPrintDestination.Print.NewPrint.arguments
        ) { backStackEntry ->
            val code = backStackEntry.arguments?.getString(
                FibraPrintDestination.Print.NewPrint.ARG_CODE
            ) ?: ""
            val name = backStackEntry.arguments?.getString(
                FibraPrintDestination.Print.NewPrint.ARG_NAME
            ) ?: ""
            val unidad = backStackEntry.arguments?.getString(
                FibraPrintDestination.Print.NewPrint.ARG_UNIDAD
            ) ?: ""
            
            NewPrintScreen(
                onBack = { navController.popBackStack() },
                code = code,
                name = name,
                unidad = unidad,
                //navController = navController,
                onNavigateToSettings = onNavigateToSettings
            )
        }
        
        // Inventory Module
        composable(FibraPrintDestination.Inventory.OncList.route) {
            PrintOncScreen(
                onBack = { navController.popBackStack() },
                onNavigateToProduct = {
                    navController.navigate(FibraPrintDestination.Inventory.ProductWeighing.route)
                },
                onNavigateToDetails = { docEntry ->
                    navController.navigate(
                        FibraPrintDestination.Inventory.IncDetails.createRoute(docEntry)
                    )
                }
            )
        }
        
        composable(
            route = FibraPrintDestination.Inventory.IncDetails.getRoute(),
            arguments = FibraPrintDestination.Inventory.IncDetails.arguments
        ) { backStackEntry ->
            val docEntry = backStackEntry.arguments?.getInt(
                FibraPrintDestination.Inventory.IncDetails.ARG_DOC_ENTRY
            ) ?: 0
            
            PrintIncScreen(
                onNavigateBack = { navController.popBackStack() },
                docEntry = docEntry,
                onNavigationToSetting = onNavigateToSettings

            )
        }
        
        composable(FibraPrintDestination.Inventory.ProductWeighing.route) {
            PrintPesajeScreen(
                onNavigateBack = navController::popBackStack,
                onNavigateToIncRegister = { itemCode, itemName, codeBar ->
                    navController.navigate(
                        FibraPrintDestination.Inventory.RegisterDetails.createRoute(
                            itemCode, itemName, codeBar
                        )
                    )
                },
                onNavigateToProducts = {
                    navController.navigate(FibraPrintDestination.Print.Products.route)
                },
                navController = navController,
                onNavigateToScan = {
                    onNavigateToShared?.invoke(SharedDestination.BarcodeScanner)
                }
            )
        }
        
        composable(
            route = FibraPrintDestination.Inventory.RegisterDetails.getRoute(),
            arguments = FibraPrintDestination.Inventory.RegisterDetails.arguments
        ) { backStackEntry ->
            val itemCode = backStackEntry.arguments?.getString(
                FibraPrintDestination.Inventory.RegisterDetails.ARG_ITEM_CODE
            ) ?: ""
            val itemName = backStackEntry.arguments?.getString(
                FibraPrintDestination.Inventory.RegisterDetails.ARG_ITEM_NAME
            ) ?: ""
            val codeBar = backStackEntry.arguments?.getString(
                FibraPrintDestination.Inventory.RegisterDetails.ARG_CODE_BAR
            ) ?: ""
            
            PrintRegisterIncDetailsScreen(
                onBackNavigation = { navController.popBackStack() },
                itemCode = itemCode,
                itemName = itemName,
                codeBar = codeBar
            )
        }
        
        // Reception Module
        composable(FibraPrintDestination.Reception.route) {
            ReceptionMenuPrintScreen(
                onNavigateBack = { navController.popBackStack() }
            )
        }
        
        // Note: Shared routes like BarcodeScanner are handled by MainAppNavHost
        // No need to duplicate them here
    }
}