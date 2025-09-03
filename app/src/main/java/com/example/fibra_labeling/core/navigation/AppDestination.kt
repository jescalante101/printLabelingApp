package com.example.fibra_labeling.core.navigation

import com.example.fibra_labeling.fibrafil.navigation.FibraFilDestination
import com.example.fibra_labeling.fibraprint.navigation.FibraPrintDestination

/**
 * Core navigation destinations for the entire app
 * Routes to different company modules and shared features
 */
sealed class AppDestination(val route: String) {
    
    // Core/Welcome screen
    object Welcome : AppDestination("welcome")
    
    // Company-specific destinations
    data class FibraFil(val destination: FibraFilDestination) : AppDestination("fibrafil/${destination.route}")
    data class FibraPrint(val destination: FibraPrintDestination) : AppDestination("fibraprint/${destination.route}")
    
    // Shared features
    data class Settings(val destination: SettingsDestination) : AppDestination("settings/${destination.route}")
    data class Shared(val destination: SharedDestination) : AppDestination("shared/${destination.route}")
    
    companion object {
        // Helper functions to create navigation routes
        fun toFibraFil(destination: FibraFilDestination): String = "fibrafil/${destination.route}"
        fun toFibraPrint(destination: FibraPrintDestination): String = "fibraprint/${destination.route}"
        fun toSettings(destination: SettingsDestination): String = "settings/${destination.route}"
        fun toShared(destination: SharedDestination): String = "shared/${destination.route}"
    }
}