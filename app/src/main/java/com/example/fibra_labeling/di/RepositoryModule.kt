package com.example.fibra_labeling.di

import com.example.fibra_labeling.data.local.repository.fibrafil.EtiquetaDetalleRepository
import com.example.fibra_labeling.data.local.repository.fibrafil.EtiquetaDetalleRepositoryImpl
import com.example.fibra_labeling.data.local.repository.fibrafil.maquina.FMaquinaRepository
import com.example.fibra_labeling.data.local.repository.fibrafil.maquina.FMaquinaRepositoryImpl
import org.koin.dsl.module

val repositoryModule = module {
    single<EtiquetaDetalleRepository> { EtiquetaDetalleRepositoryImpl(get()) }
    single<FMaquinaRepository>{ FMaquinaRepositoryImpl(get(),get()) }
}