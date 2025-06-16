package com.example.fibra_labeling.di

import com.example.fibra_labeling.data.local.repository.fibrafil.EtiquetaDetalleRepository
import com.example.fibra_labeling.data.local.repository.fibrafil.EtiquetaDetalleRepositoryImpl
import com.example.fibra_labeling.data.local.repository.fibrafil.almacen.FibAlmacenRepository
import com.example.fibra_labeling.data.local.repository.fibrafil.almacen.FibAlmacenRepositoryImpl
import com.example.fibra_labeling.data.local.repository.fibrafil.fibinc.FibIncRepository
import com.example.fibra_labeling.data.local.repository.fibrafil.fibinc.FibIncRepositoryImpl
import com.example.fibra_labeling.data.local.repository.fibrafil.maquina.FMaquinaRepository
import com.example.fibra_labeling.data.local.repository.fibrafil.maquina.FMaquinaRepositoryImpl
import com.example.fibra_labeling.data.local.repository.fibrafil.oinc.FibOincRepository
import com.example.fibra_labeling.data.local.repository.fibrafil.oinc.FibOincRepositoryImpl
import com.example.fibra_labeling.data.local.repository.fibrafil.oitm.FibOITMRepositoryImpl
import com.example.fibra_labeling.data.local.repository.fibrafil.oitm.FibOitmRepository
import com.example.fibra_labeling.data.local.repository.fibrafil.user.FUserRepository
import com.example.fibra_labeling.data.local.repository.fibrafil.user.FUserRepositoryImpl
import com.example.fibra_labeling.data.remote.SyncRepository
import com.example.fibra_labeling.data.remote.SyncRepositoryImpl
import org.koin.dsl.module

val repositoryModule = module {

    //local
    single<EtiquetaDetalleRepository> { EtiquetaDetalleRepositoryImpl(get()) }
    single<FMaquinaRepository>{ FMaquinaRepositoryImpl(get(),get()) }
    single<FUserRepository> { FUserRepositoryImpl(get()) }
    single<FibOincRepository> { FibOincRepositoryImpl(get()) }
    single<FibIncRepository> { FibIncRepositoryImpl(get()) }
    single<FibAlmacenRepository> { FibAlmacenRepositoryImpl(get()) }
    single<FibOitmRepository> { FibOITMRepositoryImpl(get()) }


    //remote
    single<SyncRepository> { SyncRepositoryImpl(get(),get(),get()) }

}