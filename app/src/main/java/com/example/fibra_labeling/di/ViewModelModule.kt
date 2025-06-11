package com.example.fibra_labeling.di

import com.example.fibra_labeling.ui.BarcodeViewModel
import com.example.fibra_labeling.ui.screen.home.HomeViewModel
import com.example.fibra_labeling.ui.screen.print.PrintViewModel
import com.example.fibra_labeling.ui.screen.print.register.NewPrintViewModel
import com.example.fibra_labeling.ui.screen.print.register.RegisterViewModel
import com.example.fibra_labeling.ui.screen.setting.printer.PrinterSettingScreenViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val viewModelModule= module {
    viewModel {
        HomeViewModel(get())
    }


    viewModel{ PrintViewModel(get(),get()) }
    viewModel{ RegisterViewModel(get()) }
    viewModel{ NewPrintViewModel(get(),get(),get()) }

    viewModel{ BarcodeViewModel() }

    viewModel {
        PrinterSettingScreenViewModel(get(),get())
    }
}