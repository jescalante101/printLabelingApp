package com.example.fibra_labeling.ui.navigation

sealed class Screen(val route:String) {
    object Home: Screen("home")
    object Print: Screen("print")
    object Report: Screen("report")
    object Profile: Screen("profile")
    object Settings: Screen("settings")

    object Scan: Screen("scan")
}