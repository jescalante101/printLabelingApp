package com.example.fibra_labeling.fibrafil.navigation

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
import com.example.fibra_labeling.fibrafil.ui.screen.etiqueta.etiqueta.ImpresionScreen
import com.example.fibra_labeling.fibrafil.ui.screen.etiqueta.etiquetanueva.AddEtiquetaScreen
import com.example.fibra_labeling.fibrafil.ui.screen.home.HomeScreen
import com.example.fibra_labeling.fibrafil.ui.screen.inventario.InventoryScreen
import com.example.fibra_labeling.fibrafil.ui.screen.inventario.RegisterCabecera
import com.example.fibra_labeling.fibrafil.ui.screen.inventario.details.IncScreen
import com.example.fibra_labeling.fibrafil.ui.screen.inventario.register.stock.ICountingScreen
import com.example.fibra_labeling.fibrafil.ui.screen.reception.ReceptionMenuScreen
import com.example.fibra_labeling.fibrafil.ui.screen.reception.registerreception.supplier.SupplierListScreen
import com.example.fibra_labeling.fibrafil.ui.screen.reception.registerreception.order.OrderListScreen
import com.example.fibra_labeling.fibrafil.ui.screen.reception.registerreception.register.ReceptionFibrafilScreen
import org.koin.androidx.compose.koinViewModel

@Composable
fun FibraFilNavHost(
    navController: NavHostController,
    startDestination: String = FibraFilDestination.Home.route,
    onNavigateToSettings: ((SettingsDestination) -> Unit)? = null,
    onNavigateToShared: ((SharedDestination) -> Unit)? = null,
    onNavigateToPrintRegister: ((Boolean) -> Unit)? = null
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
        composable(FibraFilDestination.Home.route) {
            HomeScreen(
                navController = navController,
                onNavigateToSettings = onNavigateToSettings
            )
        }
        
        // Inventory Module
        composable(FibraFilDestination.Inventory.Main.route) {
            InventoryScreen(
                onNavigateBack = { navController.popBackStack() },
                onNavigateToCounting = { 
                    navController.navigate(FibraFilDestination.Inventory.Counting.route) 
                }
            )
        }
        
        composable(FibraFilDestination.Inventory.Counting.route) {
            ICountingScreen(
                onNavigateBack = { navController.popBackStack() }
            )
        }
        
        composable(FibraFilDestination.Inventory.RegisterHeader.route) {
            RegisterCabecera(
                onNavigateBack = { navController.popBackStack() },
                onNavigateToFilEtiqueta = { isPrint ->
                    // Navigate to shared print register with parameter
                    onNavigateToPrintRegister?.invoke(isPrint)
                },
                onNavigateToDetails = { docEntry ->
                    navController.navigate(
                        FibraFilDestination.Inventory.Details.createRoute(docEntry)
                    )
                }
            )
        }
        
        composable(
            route = FibraFilDestination.Inventory.Details.getRoute(),
            arguments = FibraFilDestination.Inventory.Details.arguments
        ) { backStackEntry ->
            val docEntry = backStackEntry.arguments?.getInt(
                FibraFilDestination.Inventory.Details.ARG_DOC_ENTRY
            ) ?: 0
            
            IncScreen(
                onNavigateBack = { navController.popBackStack() },
                docEntry = docEntry,
                onNavigationSetting = onNavigateToSettings
            )
        }
        
        // Labels Module
        composable(FibraFilDestination.Labels.PrintMain.route) {
            ImpresionScreen(
                onBack = { navController.popBackStack() },
                onNavigateToRegister = { 
                    onNavigateToPrintRegister?.invoke(false)
                },
                onNavigateToScan = { 
                    onNavigateToShared?.invoke(SharedDestination.BarcodeScanner)
                },
                navController = navController,
                onNavigateToSettings = onNavigateToSettings
            )
        }
        
        composable(
            route = FibraFilDestination.Labels.NewLabel.getRoute(),
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
        
        // Reception Module
        composable(FibraFilDestination.Reception.Menu.route) {
            ReceptionMenuScreen(
                navController = navController,
                onNavigateBack = { navController.popBackStack() }
            )
        }
        
        composable(FibraFilDestination.Reception.SupplierList.route) {
            SupplierListScreen(
                onNavigateBack = { navController.popBackStack() },
                onNavigateToOrderList = { supplierId ->
                    navController.navigate(
                        FibraFilDestination.Reception.OrderList.createRoute(supplierId)
                    )
                }
            )
        }
        
        composable(
            route = FibraFilDestination.Reception.OrderList.getRoute(),
            arguments = FibraFilDestination.Reception.OrderList.arguments
        ) { backStackEntry ->
            val supplierId = backStackEntry.arguments?.getString(
                FibraFilDestination.Reception.OrderList.ARG_SUPPLIER_ID
            ) ?: ""
            
            OrderListScreen(
                supplierId = supplierId,
                onNavigateBack = { navController.popBackStack() },
                onNavigateToItemDetails = { orderId ->
                    navController.navigate(
                        FibraFilDestination.Reception.ItemDetails.createRoute(orderId)
                    )
                }
            )
        }
        
        composable(
            route = FibraFilDestination.Reception.ItemDetails.getRoute(),
            arguments = FibraFilDestination.Reception.ItemDetails.arguments
        ) { backStackEntry ->
            val orderId = backStackEntry.arguments?.getString(
                FibraFilDestination.Reception.ItemDetails.ARG_ORDER_ID
            ) ?: ""
            
            ReceptionFibrafilScreen()
        }
        
        // Note: Shared routes like BarcodeScanner are handled by MainAppNavHost
        // No need to duplicate them here
    }
}