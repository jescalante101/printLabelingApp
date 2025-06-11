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
import com.example.fibra_labeling.ui.screen.home.HomeScreen
import com.example.fibra_labeling.ui.screen.inventory.ICountingScreen
import com.example.fibra_labeling.ui.screen.inventory.InventoryScreen
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
                onNavigateToInventory = { navController.navigate(Screen.Inventory.route) },
                onNavigateToPackingList = { navController.navigate(Screen.PackingList.route) },
                onNavigateToProduction = { navController.navigate(Screen.Production.route) },
                onNavigateToSetting = { navController.navigate(Screen.PrintSetting.route) }
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
                onNavigateToRegister = { navController.navigate(Screen.PrintRegister.route) }
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
            Screen.PrintRegister.route
        ){
            PrintRegisterScreen (
                onBack = { navController.popBackStack() },
                onNavigateToNewPrint = { code,name->
                    navController.navigate("${Screen.NewPrint.route}/$code/$name")
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