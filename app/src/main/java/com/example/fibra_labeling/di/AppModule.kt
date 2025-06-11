package com.example.fibra_labeling.di

import org.koin.core.module.Module

val appModules: List<Module> = listOf (
    networkModule,
    dataStoreModule,
    viewModelModule
)