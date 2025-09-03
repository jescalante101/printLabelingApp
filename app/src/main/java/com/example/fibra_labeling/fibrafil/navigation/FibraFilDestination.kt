package com.example.fibra_labeling.fibrafil.navigation

import androidx.navigation.NavType
import androidx.navigation.navArgument

/**
 * Navigation destinations specific to FibraFil company
 */
sealed class FibraFilDestination(val route: String) {
    
    object Home : FibraFilDestination("home")
    
    // Inventory module
    sealed class Inventory(route: String) : FibraFilDestination("inventory/$route") {
        object Main : Inventory("main")
        object Counting : Inventory("counting")
        object RegisterHeader : Inventory("register_header")
        
        data class Details(val docEntry: Int) : Inventory("details/$docEntry") {
            companion object {
                const val ROUTE_TEMPLATE = "details/{docEntry}"
                const val ARG_DOC_ENTRY = "docEntry"
                
                val arguments = listOf(
                    navArgument(ARG_DOC_ENTRY) { type = NavType.IntType }
                )
                
                fun createRoute(docEntry: Int) = "inventory/details/$docEntry"
                fun getRoute() = "inventory/$ROUTE_TEMPLATE"
            }
        }
    }
    
    // Labels/Etiquetas module
    sealed class Labels(route: String) : FibraFilDestination("labels/$route") {
        object PrintMain : Labels("print_main")
        
        data class NewLabel(val itemCode: String, val productName: String) : Labels("new/$itemCode/$productName") {
            companion object {
                const val ROUTE_TEMPLATE = "new/{itemCode}/{productName}"
                const val ARG_ITEM_CODE = "itemCode"
                const val ARG_PRODUCT_NAME = "productName"
                
                val arguments = listOf(
                    navArgument(ARG_ITEM_CODE) { type = NavType.StringType },
                    navArgument(ARG_PRODUCT_NAME) { type = NavType.StringType }
                )
                
                fun createRoute(itemCode: String, productName: String) = 
                    "labels/new/$itemCode/$productName"
                fun getRoute() = "labels/$ROUTE_TEMPLATE"
            }
        }
    }
    
    // Reception module
    sealed class Reception(route: String) : FibraFilDestination("reception/$route") {
        object Menu : Reception("menu")
        object SupplierList : Reception("suppliers")
        
        data class OrderList(val supplierId: String) : Reception("orders/$supplierId") {
            companion object {
                const val ROUTE_TEMPLATE = "orders/{supplierId}"
                const val ARG_SUPPLIER_ID = "supplierId"
                
                val arguments = listOf(
                    navArgument(ARG_SUPPLIER_ID) { type = NavType.StringType }
                )
                
                fun createRoute(supplierId: String) = "reception/orders/$supplierId"
                fun getRoute() = "reception/$ROUTE_TEMPLATE"
            }
        }
        
        data class ItemDetails(val orderId: String) : Reception("items/$orderId") {
            companion object {
                const val ROUTE_TEMPLATE = "items/{orderId}"
                const val ARG_ORDER_ID = "orderId"
                
                val arguments = listOf(
                    navArgument(ARG_ORDER_ID) { type = NavType.StringType }
                )
                
                fun createRoute(orderId: String) = "reception/items/$orderId"
                fun getRoute() = "reception/$ROUTE_TEMPLATE"
            }
        }
    }
}