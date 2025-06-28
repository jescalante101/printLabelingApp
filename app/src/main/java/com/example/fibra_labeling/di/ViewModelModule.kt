package com.example.fibra_labeling.di

import com.example.fibra_labeling.ui.BarcodeViewModel
import com.example.fibra_labeling.ui.screen.fibra_print.etiqueta.impresion.PrintViewModel
import com.example.fibra_labeling.ui.screen.fibra_print.etiqueta.products.PrintProductViewModel
import com.example.fibra_labeling.ui.screen.fibra_print.etiqueta.register.NewPrintViewModel
import com.example.fibra_labeling.ui.screen.fibra_print.etiqueta.register.RegisterViewModel
import com.example.fibra_labeling.ui.screen.fibra_print.home_print.HomePrintViewModel
import com.example.fibra_labeling.ui.screen.fibra_print.inventario.PrintOncViewModel
import com.example.fibra_labeling.ui.screen.fibra_print.inventario.register.PrintRegisterOIncViewModel
import com.example.fibra_labeling.ui.screen.fibrafil.etiqueta.etiqueta.ImpresionModelView
import com.example.fibra_labeling.ui.screen.fibrafil.etiqueta.etiquetanueva.FillEtiquetaViewModel
import com.example.fibra_labeling.ui.screen.fibrafil.home.HomeViewModel
import com.example.fibra_labeling.ui.screen.fibrafil.inventario.RegisterCabeceraViewModel
import com.example.fibra_labeling.ui.screen.fibrafil.inventario.details.IncViewModel
import com.example.fibra_labeling.ui.screen.fibrafil.inventario.register.OincRegisterViewModel
import com.example.fibra_labeling.ui.screen.fibrafil.inventario.register.stock.StockViewModel

import com.example.fibra_labeling.ui.screen.setting.printer.PrinterSettingScreenViewModel
import com.example.fibra_labeling.ui.screen.setting.zpl.ZplTemplateViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val viewModelModule= module {
    viewModel {
        HomeViewModel(get(),get())
    }


    viewModel{ PrintViewModel(get(),get()) }
    viewModel{ RegisterViewModel(get(),get(),get()) }
    viewModel{ NewPrintViewModel(get(),get(),get(),get(),get(),get()) }

    viewModel{ BarcodeViewModel() }

    viewModel {
        PrinterSettingScreenViewModel(get(),get(),get())
    }

    viewModel{
        ImpresionModelView(get(),get(),get(),get(),get())
    }
    viewModel{
        FillEtiquetaViewModel(get(),get(),get(),get(),get(),get(),get(),get())
    }
    viewModel{
        OincRegisterViewModel(get(),get())
    }
    viewModel{
        RegisterCabeceraViewModel(get(),get(),get())
    }

    viewModel{
        StockViewModel(get())
    }
    viewModel{
        IncViewModel(get(),get(),get(),get(),get(),get())
    }

    viewModel{
        ZplTemplateViewModel(get())
    }

    viewModel{
        PrintProductViewModel(get())
    }

    viewModel{
        HomePrintViewModel(get())
    }


    //FIBRAPRINT VIEW MODEL
    viewModel{
        PrintOncViewModel(get(),get())
    }
    viewModel{
        PrintRegisterOIncViewModel(get(),get())
    }

}