package com.example.fibra_labeling.ui.navigation

sealed class Screen(val route:String) {
    object Home: Screen("home")
    object Print: Screen("print")
    object Transfer: Screen("transfer")
    object Reception: Screen("reception")
    object Inventory: Screen("inventory")
    object PackingList: Screen("packingList")

    object Scan: Screen("scan")
}