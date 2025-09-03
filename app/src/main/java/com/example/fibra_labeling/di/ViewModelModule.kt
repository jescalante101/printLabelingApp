package com.example.fibra_labeling.di

import com.example.fibra_labeling.ui.BarcodeViewModel
import com.example.fibra_labeling.fibraprint.ui.screen.etiqueta.impresion.PrintViewModel
import com.example.fibra_labeling.fibraprint.ui.screen.etiqueta.products.PrintProductViewModel
import com.example.fibra_labeling.fibraprint.ui.screen.etiqueta.register.NewPrintViewModel
import com.example.fibra_labeling.fibraprint.ui.screen.etiqueta.register.RegisterViewModel
import com.example.fibra_labeling.fibraprint.ui.screen.home_print.HomePrintViewModel
import com.example.fibra_labeling.fibraprint.ui.screen.inventario.PrintOncViewModel
import com.example.fibra_labeling.fibraprint.ui.screen.inventario.details.PrintIncViewModel
import com.example.fibra_labeling.fibraprint.ui.screen.inventario.details.register.PrintRegisterIncDetailsViewModel
import com.example.fibra_labeling.fibraprint.ui.screen.inventario.product.PrintPesajeViewModel
import com.example.fibra_labeling.fibraprint.ui.screen.inventario.register.PrintRegisterOIncViewModel
import com.example.fibra_labeling.fibrafil.ui.screen.etiqueta.etiqueta.ImpresionModelView
import com.example.fibra_labeling.fibrafil.ui.screen.etiqueta.etiquetanueva.FillEtiquetaViewModel
import com.example.fibra_labeling.fibrafil.ui.screen.home.HomeViewModel
import com.example.fibra_labeling.fibrafil.ui.screen.inventario.RegisterCabeceraViewModel
import com.example.fibra_labeling.fibrafil.ui.screen.inventario.details.IncViewModel
import com.example.fibra_labeling.fibrafil.ui.screen.inventario.register.OincRegisterViewModel
import com.example.fibra_labeling.fibrafil.ui.screen.inventario.register.stock.StockViewModel
import com.example.fibra_labeling.fibrafil.ui.screen.reception.registerreception.supplier.SupplierListViewModel
import com.example.fibra_labeling.fibrafil.ui.screen.reception.registerreception.order.OrderListViewModel
import com.example.fibra_labeling.fibrafil.ui.screen.reception.registerreception.register.ReceptionFibrafilViewModel
import com.example.fibra_labeling.shared.ui.screen.setting.general.GeneralSettingScreenViewModel

import com.example.fibra_labeling.shared.ui.screen.setting.printer.PrinterSettingScreenViewModel
import com.example.fibra_labeling.shared.ui.screen.setting.servidor.ServerSettingViewModel
import com.example.fibra_labeling.shared.ui.screen.setting.zpl.ZplTemplateViewModel
import com.example.fibra_labeling.shared.ui.screen.welcome.WelcomeViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val viewModelModule= module {
    viewModel {
        HomeViewModel(get(),get())
    }


    viewModel{ PrintViewModel(get(),get(),get(),get(),get(),get()) }
    viewModel{ RegisterViewModel(get(),get(),get()) }
    viewModel{ NewPrintViewModel(get(),get(),get(),get(),get(),get(),get(),get(),get(),get()) }

    viewModel{ BarcodeViewModel() }


    viewModel {
        PrinterSettingScreenViewModel(get(),get(),get(),get())
    }

    viewModel{
        ImpresionModelView(get(),get(),get(),get(),get(),get())
    }
    viewModel{
        FillEtiquetaViewModel(get(),get(),get(),get(),get(),get(),get(),get(),get(),get())
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
        ZplTemplateViewModel(get(),get())
    }

    viewModel{
        PrintProductViewModel(get())
    }

    viewModel{
        HomePrintViewModel(get(),get(),get(),get())
    }


    //FIBRAPRINT VIEW MODEL
    viewModel{
        PrintOncViewModel(get(),get(),get())
    }
    viewModel{
        PrintRegisterOIncViewModel(get(),get())
    }
    viewModel{
        PrintIncViewModel(get(),get(),get(),get(),get())
    }

    viewModel{
        PrintPesajeViewModel(get(),get())
    }

    viewModel{
        PrintRegisterIncDetailsViewModel(get(),get(),get(),get(),get(),get())
    }

    viewModel{
        WelcomeViewModel(get())
    }
    viewModel{
        ServerSettingViewModel(get(),get())
    }

    viewModel{
        GeneralSettingScreenViewModel(get())
    }
    
    // Reception ViewModels - FibraFil
    viewModel{
        SupplierListViewModel()
    }
    
    viewModel{
        OrderListViewModel()
    }
    
    viewModel{
        ReceptionFibrafilViewModel()
    }
}