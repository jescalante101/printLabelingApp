package com.example.fibra_labeling.ui.navigation

import BarcodeScannerScreen
import android.R.attr.type
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.fibra_labeling.ui.screen.fibrafil.inventario.etiqueta.ImpresionScreen
import com.example.fibra_labeling.ui.screen.fibrafil.inventario.etiquetanueva.AddEtiquetaScreen
import com.example.fibra_labeling.ui.screen.home.HomeScreen
import com.example.fibra_labeling.ui.screen.inventory.ICountingScreen
import com.example.fibra_labeling.ui.screen.inventory.InventoryScreen
import com.example.fibra_labeling.ui.screen.inventory.RegisterCabecera
import com.example.fibra_labeling.ui.screen.packing.PackingScreen
import com.example.fibra_labeling.ui.screen.print.PrintScreen
import com.example.fibra_labeling.ui.screen.print.register.NewPrintScreen
import com.example.fibra_labeling.ui.screen.print.register.PrintRegisterScreen
import com.example.fibra_labeling.ui.screen.production.ProductionScreen
import com.example.fibra_labeling.ui.screen.reception.ReceptionScreen
import com.example.fibra_labeling.ui.screen.setting.printer.PrintSettingScreen
import com.example.fibra_labeling.ui.screen.transfer.TransferScreen

@Composable
fun AppNavHost(navController: NavHostController = rememberNavController(), startDestination: String = Screen.Home.route){
    NavHost(
        navController = navController,
        startDestination = startDestination,
    ){
        composable(
            Screen.Home.route,
            enterTransition = {
                fadeIn(animationSpec = tween(700)) +
                        scaleIn(initialScale = 0.9f, animationSpec = tween(700))
            }

        ){

            HomeScreen(
                onNavigateToPrint = { navController.navigate(Screen.Print.route) },
                onNavigateToReception = { navController.navigate(Screen.Reception.route) },
                onNavigateToTransfer = { navController.navigate(Screen.Transfer.route) },
                onNavigateToInventory = { navController.navigate(Screen.InventarioOnc.route) },
                onNavigateToPackingList = { navController.navigate(Screen.PackingList.route) },
                onNavigateToProduction = { navController.navigate(Screen.Production.route) },
                onNavigateToSetting = { navController.navigate(Screen.PrintSetting.route) },
                onNavigateToFill = {navController.navigate(Screen.FillImpresion.route)}
            )

        }


        composable(
            Screen.Print.route,
            enterTransition = {
                fadeIn(animationSpec = tween(700)) +
                        scaleIn(initialScale = 0.9f, animationSpec = tween(700))
            },
            exitTransition = {
                fadeOut(animationSpec = tween(300)) +
                        scaleOut(targetScale = 1.2f, animationSpec = tween(500))
            }
        ){
            PrintScreen(
                onBack = { navController.popBackStack() },
                onNavigateToScan = { navController.navigate(Screen.Scan.route) },
                navController = navController,
                onNavigateToRegister = { navController.navigate("${Screen.PrintRegister.route}/true") }
            )
        }

        composable(
            Screen.Scan.route,
        ){
            BarcodeScannerScreen(
                onBack = { navController.popBackStack() },
                navController = navController
            )
        }

        composable(
            route="${Screen.PrintRegister.route}/{isPrint}",
            arguments = listOf(
                navArgument("isPrint"){type= NavType.BoolType}
            )
        ){ backStackEntry ->
            val isPrint = backStackEntry.arguments?.getBoolean("isPrint")
            PrintRegisterScreen (
                onBack = { navController.popBackStack() },
                onNavigateToNewPrint = { code,name->
                    navController.navigate("${Screen.NewPrint.route}/$code/$name")
                },
                isPrint = isPrint == true,
                onNavigateToFillPrint = {itemCode,productName->
                    navController.navigate("${Screen.FillImpresionNew.route}/$itemCode/$productName")
                }
            )
        }

        composable(
            route="${Screen.NewPrint.route}/{code}/{name}",
            arguments = listOf(
                navArgument("code"){type= NavType.StringType},
                navArgument("name"){type= NavType.StringType}
            )
        ){ backStackEntry ->
            val code = backStackEntry.arguments?.getString("code")
            val name = backStackEntry.arguments?.getString("name")
            NewPrintScreen (
                onBack = { navController.popBackStack() },
                code = code ?: "",
                name = name ?: ""
            )
        }

        composable(
            Screen.Reception.route,
        ){
            ReceptionScreen (
                onNavigateBack = { navController.popBackStack() },
            )
        }

        composable(
            Screen.Transfer.route,
        ){
            TransferScreen (
                onNavigateBack = { navController.popBackStack() },
            )
        }

        composable(
            Screen.Inventory.route,
        ){
            InventoryScreen(
                onNavigateBack = { navController.popBackStack() },
                onNavigateToCounting = { navController.navigate(Screen.ICounting.route) }
            )
        }

        composable(
            Screen.PackingList.route,
        ) {
            PackingScreen {
                navController.popBackStack()
            }
        }

        composable(
            Screen.Production.route,
        ) {
            ProductionScreen {
                navController.popBackStack()
            }
        }
        composable(
            Screen.ICounting.route
        ){
            ICountingScreen {
                navController.popBackStack()
            }
        }

        composable(
            Screen.InventarioOnc.route
        ) {
            RegisterCabecera (
                onNavigateBack = { navController.popBackStack() },
                onNavigateToInvetory = { navController.navigate(Screen.Inventory.route) }
            )
        }
        composable(
            Screen.FillImpresion.route
        ) {
            ImpresionScreen (
                onBack = {navController.popBackStack()},
                onNavigateToRegister = {navController.navigate("${Screen.PrintRegister.route}/false")},
                onNavigateToScan = {navController.navigate(Screen.Scan.route)},
                navController
            )
        }

        composable(
            route="${Screen.FillImpresionNew.route}/{itemCode}/{productName}",
            arguments = listOf(
                navArgument("itemCode"){type= NavType.StringType},
                navArgument("productName"){type= NavType.StringType}
            )
        ) {backStackEntry ->
            val itemCode = backStackEntry.arguments?.getString("itemCode")
            val productName = backStackEntry.arguments?.getString("productName")
            AddEtiquetaScreen (
                onBack = {navController.popBackStack()},
                itemCode = itemCode ?: "",
                productName = productName ?: ""
            )
        }


        ///SETTIG SCREEN
         composable(
             Screen.PrintSetting.route
         ) {
             PrintSettingScreen(
                 onBack = {
                     navController.popBackStack()
                 }
             )
         }





    }
}