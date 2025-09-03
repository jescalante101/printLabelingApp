package com.example.fibra_labeling.fibraprint.navigation

import androidx.navigation.NavType
import androidx.navigation.navArgument

/**
 * Navigation destinations specific to FibraPrint company
 */
sealed class FibraPrintDestination(val route: String) {
    
    object Home : FibraPrintDestination("home")
    
    // Print/Etiquetas module
    sealed class Print(route: String) : FibraPrintDestination("print/$route") {
        object Main : Print("main")
        object Products : Print("products")
        
        data class Register(val isPrint: Boolean) : Print("register/$isPrint") {
            companion object {
                const val ROUTE_TEMPLATE = "register/{isPrint}"
                const val ARG_IS_PRINT = "isPrint"
                
                val arguments = listOf(
                    navArgument(ARG_IS_PRINT) { type = NavType.BoolType }
                )
                
                fun createRoute(isPrint: Boolean) = "print/register/$isPrint"
                fun getRoute() = "print/$ROUTE_TEMPLATE"
            }
        }
        
        data class NewPrint(val code: String, val name: String, val unidad: String = "") : Print("new/$code/$name/$unidad") {
            companion object {
                const val ROUTE_TEMPLATE = "new/{code}/{name}/{unidad}"
                const val ARG_CODE = "code"
                const val ARG_NAME = "name"
                const val ARG_UNIDAD = "unidad"
                
                val arguments = listOf(
                    navArgument(ARG_CODE) { type = NavType.StringType },
                    navArgument(ARG_NAME) { type = NavType.StringType },
                    navArgument(ARG_UNIDAD) { type = NavType.StringType }
                )
                
                fun createRoute(code: String, name: String, unidad: String = "") = "print/new/$code/$name/$unidad"
                fun getRoute() = "print/$ROUTE_TEMPLATE"
            }
        }
    }
    
    // Inventory module
    sealed class Inventory(route: String) : FibraPrintDestination("inventory/$route") {
        object OncList : Inventory("onc_list")
        object ProductWeighing : Inventory("product_weighing")
        
        data class IncDetails(val docEntry: Int) : Inventory("inc_details/$docEntry") {
            companion object {
                const val ROUTE_TEMPLATE = "inc_details/{docEntry}"
                const val ARG_DOC_ENTRY = "docEntry"
                
                val arguments = listOf(
                    navArgument(ARG_DOC_ENTRY) { type = NavType.IntType }
                )
                
                fun createRoute(docEntry: Int) = "inventory/inc_details/$docEntry"
                fun getRoute() = "inventory/$ROUTE_TEMPLATE"
            }
        }
        
        data class RegisterDetails(
            val itemCode: String, 
            val itemName: String, 
            val codeBar: String
        ) : Inventory("register_details/$itemCode/$itemName/$codeBar") {
            companion object {
                const val ROUTE_TEMPLATE = "register_details/{itemCode}/{itemName}/{codeBar}"
                const val ARG_ITEM_CODE = "itemCode"
                const val ARG_ITEM_NAME = "itemName"
                const val ARG_CODE_BAR = "codeBar"
                
                val arguments = listOf(
                    navArgument(ARG_ITEM_CODE) { type = NavType.StringType },
                    navArgument(ARG_ITEM_NAME) { type = NavType.StringType },
                    navArgument(ARG_CODE_BAR) { type = NavType.StringType }
                )
                
                fun createRoute(itemCode: String, itemName: String, codeBar: String) = 
                    "inventory/register_details/$itemCode/$itemName/$codeBar"
                fun getRoute() = "inventory/$ROUTE_TEMPLATE"
            }
        }
    }
    
    // Reception module
    object Reception : FibraPrintDestination("reception")
}