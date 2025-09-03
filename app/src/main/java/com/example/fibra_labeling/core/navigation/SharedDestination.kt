package com.example.fibra_labeling.core.navigation

/**
 * Shared navigation destinations used by both companies
 */
sealed class SharedDestination(val route: String) {
    
    // Barcode scanner used by both companies
    object BarcodeScanner : SharedDestination("barcode_scanner")
    
    // Login screen (if needed in future)
    object Login : SharedDestination("login")
}

/**
 * Settings navigation destinations
 */
sealed class SettingsDestination(val route: String) {
    
    object Printer : SettingsDestination("printer")
    object Server : SettingsDestination("server") 
    object General : SettingsDestination("general")
    object ZplTemplate : SettingsDestination("zpl_template")
}