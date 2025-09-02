package com.example.fibra_labeling.ui.navigation

sealed class Screen(val route:String) {
    object Welcome: Screen("welcome")
    object Login: Screen("login")
    object Home: Screen("home")
    object HomePrint: Screen("homeP")

    object Print: Screen("print")
    object Scan: Screen("scan")
    object PrintRegister: Screen("printRegister")
    object NewPrint: Screen("newPrint")
    object PrintProduct: Screen("product")

    object PrintOncScreen: Screen("printOnc")
    object PrintIncScreen: Screen("printInc")

    object PrintIncDetailsRegister: Screen("printIncDetailsRegister")
    object PrintPesajeScreen: Screen("printPesaje")




    object Transfer: Screen("transfer")
    object Reception: Screen("reception")
    object ReceptionMenu: Screen("receptionMenu")
    object ReceptionMenuPrint: Screen("receptionMenuPrint")
    object Inventory: Screen("inventory")
    object PackingList: Screen("packingList")
    object Production: Screen("production")
    object ICounting: Screen("counting")
    object PrintSetting: Screen("printSetting")

    object InventarioOnc: Screen("inventarioOnc")
    object  FillImpresion: Screen("fillImpresion")
    object FillImpresionNew: Screen("fillImpresionNew")

    object IncDetail: Screen("incDetail")
    object ZplTemplateScreen: Screen("zplTemplate")

    object ServerSettingScren: Screen("serverSettingScree")

    object GeneralSettingScreen: Screen("generalSetting")

}