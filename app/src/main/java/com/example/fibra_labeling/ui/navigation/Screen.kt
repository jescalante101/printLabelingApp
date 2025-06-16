package com.example.fibra_labeling.ui.navigation

sealed class Screen(val route:String) {
    object Home: Screen("home")

    object Print: Screen("print")
    object Scan: Screen("scan")
    object PrintRegister: Screen("printRegister")
    object NewPrint: Screen("newPrint")


    object Transfer: Screen("transfer")
    object Reception: Screen("reception")
    object Inventory: Screen("inventory")
    object PackingList: Screen("packingList")
    object Production: Screen("production")
    object ICounting: Screen("counting")
    object PrintSetting: Screen("printSetting")

    object InventarioOnc: Screen("inventarioOnc")
    object  FillImpresion: Screen("fillImpresion")
    object FillImpresionNew: Screen("fillImpresionNew")

     object IncDetail: Screen("incDetail")

}