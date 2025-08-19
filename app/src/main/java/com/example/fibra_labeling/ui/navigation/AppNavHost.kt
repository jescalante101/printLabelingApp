package com.example.fibra_labeling.ui.navigation

import BarcodeScannerScreen
import ZplTemplateScreen
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
import com.example.fibra_labeling.ui.screen.fibra_print.etiqueta.impresion.PrintScreen
import com.example.fibra_labeling.ui.screen.fibra_print.etiqueta.products.PrintProductScreen
import com.example.fibra_labeling.ui.screen.fibra_print.etiqueta.register.NewPrintScreen
import com.example.fibra_labeling.ui.screen.fibra_print.etiqueta.register.PrintRegisterScreen
import com.example.fibra_labeling.ui.screen.fibra_print.home_print.HomePrintScreen
import com.example.fibra_labeling.ui.screen.fibra_print.inventario.PrintOncScreen
import com.example.fibra_labeling.ui.screen.fibra_print.inventario.details.PrintIncScreen
import com.example.fibra_labeling.ui.screen.fibra_print.inventario.details.register.PrintRegisterIncDetailsScreen
import com.example.fibra_labeling.ui.screen.fibra_print.inventario.product.PrintPesajeScreen
import com.example.fibra_labeling.ui.screen.fibrafil.etiqueta.etiqueta.ImpresionScreen
import com.example.fibra_labeling.ui.screen.fibrafil.etiqueta.etiquetanueva.AddEtiquetaScreen
import com.example.fibra_labeling.ui.screen.fibrafil.home.HomeScreen
import com.example.fibra_labeling.ui.screen.fibrafil.inventario.InventoryScreen
import com.example.fibra_labeling.ui.screen.fibrafil.inventario.RegisterCabecera
import com.example.fibra_labeling.ui.screen.fibrafil.inventario.details.IncScreen
import com.example.fibra_labeling.ui.screen.packing.PackingScreen
import com.example.fibra_labeling.ui.screen.production.ProductionScreen
import com.example.fibra_labeling.ui.screen.reception.ReceptionScreen
import com.example.fibra_labeling.ui.screen.setting.general.GeneralSettingScreen
import com.example.fibra_labeling.ui.screen.setting.printer.PrintSettingScreen
import com.example.fibra_labeling.ui.screen.setting.servidor.ServerSettingScreen
import com.example.fibra_labeling.ui.screen.transfer.TransferScreen
import com.example.fibra_labeling.ui.screen.welcome.WelcomeScreen

@Composable
fun AppNavHost(navController: NavHostController = rememberNavController(), startDestination: String = Screen.Welcome.route){
    NavHost(
        navController = navController,
        startDestination = startDestination,
    ){

        composable(
            Screen.Welcome.route,
            enterTransition = {
                fadeIn(animationSpec = tween(700)) +
                        scaleIn(initialScale = 0.9f, animationSpec = tween(700))
            },
        ){
            WelcomeScreen (
                onEmpresaSeleccionada = {
                    // Navegar a la pantalla SELECCIONADA Y ALMACENAR LA EMPRESA SELECCIONADA
                    if(it == "FibraPrint"){
                        navController.navigate(Screen.HomePrint.route){
                            popUpTo(Screen.Welcome.route) { inclusive = true }
                        }

                    }else{
                        navController.navigate(Screen.Home.route){
                            popUpTo(Screen.Welcome.route) { inclusive = true }
                        }
                    }
                }
            )
        }

        composable(
            Screen.Home.route,
            enterTransition = {
                fadeIn(animationSpec = tween(700)) +
                        scaleIn(initialScale = 0.9f, animationSpec = tween(700))
            }

        ){
            HomeScreen(
                navController = navController,
            )

        }

        composable(
            Screen.HomePrint.route,
        ){
            HomePrintScreen(
                navController = navController
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
                onNavigateToRegister = { navController.navigate(Screen.PrintProduct.route) }
            )
        }

        composable(
            Screen.PrintOncScreen.route
        ) {
            PrintOncScreen(
                onBack = { navController.popBackStack() },
                onNavigateToProduct = {
                    navController.navigate(Screen.PrintPesajeScreen.route)
                },
                onNavigateToDetails = {docEntry->
                    navController.navigate("${Screen.PrintIncScreen.route}/$docEntry")
                }
            )
        }

        composable(
            route="${Screen.PrintIncScreen.route}/{docEntry}",
            arguments = listOf(
                navArgument("docEntry"){type= NavType.IntType}
            )
        ) {backStackEntry ->
            val docEntry = backStackEntry.arguments?.getInt("docEntry")
            PrintIncScreen(
                onNavigateBack = {
                    navController.popBackStack()
                },
                navController = navController,
                docEntry = docEntry ?: 0
            )
        }

        composable(
            Screen.PrintPesajeScreen.route
        ) {
            PrintPesajeScreen(
                onNavigateBack = navController::popBackStack,
                onNavigateToIncRegister = {itemCode,itemName,codeBar->
                    navController.navigate("${Screen.PrintIncDetailsRegister.route}/$itemCode/$itemName/$codeBar")
                },
                onNavigateToProducts = {
                    navController.navigate(Screen.PrintProduct.route)
                },
                navController = navController,
                onNavigateToScan = {
                    navController.navigate(Screen.Scan.route)
                }
            )
        }

        composable(
            "${Screen.PrintIncDetailsRegister.route}/{itemCode}/{itemName}/{codeBar}",
            enterTransition = {
                fadeIn(animationSpec = tween(700)) +
                        scaleIn(initialScale = 0.9f, animationSpec = tween(700))
            },
            exitTransition = {
                fadeOut(animationSpec = tween(300)) +
                        scaleOut(targetScale = 1.2f, animationSpec = tween(500))
            },
            arguments = listOf(
                navArgument("itemCode"){type= NavType.StringType},
                navArgument("itemName"){type= NavType.StringType},
                navArgument("codeBar"){type= NavType.StringType}

            )
        ) { backStackEntry ->
            PrintRegisterIncDetailsScreen(
                onBackNavigation = { navController.popBackStack() },
                itemCode = backStackEntry.arguments?.getString("itemCode") ?: "",
                itemName = backStackEntry.arguments?.getString("itemName") ?: "",
                codeBar = backStackEntry.arguments?.getString("codeBar") ?: "",
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
                },

            )
        }

        composable(
            Screen.PrintProduct.route,
        ) {
            PrintProductScreen(
                onNavigateToNewPrint = {
                    code,name,unidad->
                    navController.navigate("${Screen.NewPrint.route}/$code/$name/$unidad")

                },
                onBack = { navController.popBackStack() },
            )
        }

        composable(
            route="${Screen.NewPrint.route}/{code}/{name}/{unidad}",
            arguments = listOf(
                navArgument("code"){type= NavType.StringType},
                navArgument("name"){type= NavType.StringType},
                navArgument("unidad"){type= NavType.StringType}
            )
        ){ backStackEntry ->
            val code = backStackEntry.arguments?.getString("code")
            val name = backStackEntry.arguments?.getString("name")
            val unidad = backStackEntry.arguments?.getString("unidad")
            NewPrintScreen (
                onBack = { navController.popBackStack() },
                code = code ?: "",
                name = name ?: "",
                unidad = unidad ?: "",
                navController = navController
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
            route = "${Screen.IncDetail.route}/{docEntry}",
            arguments = listOf(
                navArgument("docEntry"){type= NavType.IntType}
            )
        ) {backStackEntry ->
            val docEntry = backStackEntry.arguments?.getInt("docEntry")
            IncScreen(
                onNavigateBack = {navController.popBackStack()},
                docEntry = docEntry ?: 0,
                navController = navController
            )
        }

        composable(
            Screen.Production.route,
        ) {
            ProductionScreen {
                navController.popBackStack()
            }
        }


        composable(
            Screen.InventarioOnc.route
        ) {
            RegisterCabecera (
                onNavigateBack = { navController.popBackStack() },
                onNavigateToFilEtiqueta = {
                    navController.navigate("${Screen.PrintRegister.route}/$it")
                },
                onNavigateToDetails = {
                    navController.navigate("${Screen.IncDetail.route}/$it")
                }
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
                productName = productName ?: "",
                navController = navController
            )
        }

        composable(
            Screen.ZplTemplateScreen.route
        ) {
            ZplTemplateScreen(
                onBack = {
                    navController.popBackStack()
                }
            )
        }

        ///SETTIG SCREEN
         composable(
             Screen.PrintSetting.route
         ) {
             PrintSettingScreen(
                 onBack = {
                     navController.popBackStack()
                 },
                 onNavigateTozplTemplate = {
                     navController.navigate(Screen.ZplTemplateScreen.route)
                 }
             )
         }

        composable(
            Screen.ServerSettingScren.route
        )
        {
            ServerSettingScreen(
                onBack = {navController.popBackStack()}
            )
        }

        composable(
            Screen.GeneralSettingScreen.route
        ) {
            GeneralSettingScreen(
                onBack = {navController.popBackStack()}
            )
        }



    }
}