package com.example.fibra_labeling.di

import com.example.fibra_labeling.data.local.repository.EtiquetaDetalleRepository
import com.example.fibra_labeling.data.local.repository.EtiquetaDetalleRepositoryImpl
import org.koin.dsl.module

val repositoryModule = module {
    single<EtiquetaDetalleRepository> { EtiquetaDetalleRepositoryImpl(get()) }
}