package com.example.fibra_labeling.di

import com.example.fibra_labeling.ui.BarcodeViewModel
import com.example.fibra_labeling.ui.screen.fibrafil.inventario.etiqueta.ImpresionModelView
import com.example.fibra_labeling.ui.screen.fibrafil.inventario.etiquetanueva.FillEtiquetaViewModel
import com.example.fibra_labeling.ui.screen.home.HomeViewModel
import com.example.fibra_labeling.ui.screen.inventory.RegisterCabeceraViewModel
import com.example.fibra_labeling.ui.screen.inventory.details.IncViewModel
import com.example.fibra_labeling.ui.screen.inventory.register.OincRegisterViewModel
import com.example.fibra_labeling.ui.screen.inventory.register.stock.StockViewModel
import com.example.fibra_labeling.ui.screen.print.PrintViewModel
import com.example.fibra_labeling.ui.screen.print.register.NewPrintViewModel
import com.example.fibra_labeling.ui.screen.print.register.RegisterViewModel
import com.example.fibra_labeling.ui.screen.setting.printer.PrinterSettingScreenViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val viewModelModule= module {
    viewModel {
        HomeViewModel(get(),get())
    }


    viewModel{ PrintViewModel(get(),get()) }
    viewModel{ RegisterViewModel(get(),get(),get()) }
    viewModel{ NewPrintViewModel(get(),get(),get()) }

    viewModel{ BarcodeViewModel() }

    viewModel {
        PrinterSettingScreenViewModel(get(),get())
    }

    viewModel{
        ImpresionModelView(get(),get())
    }
    viewModel{
        FillEtiquetaViewModel(get(),get(),get(),get(),get(),get())
    }
    viewModel{
        OincRegisterViewModel(get(),get())
    }
    viewModel{
        RegisterCabeceraViewModel(get(),get())
    }

    viewModel{
        StockViewModel(get())
    }
    viewModel{
        IncViewModel(get())
    }


}