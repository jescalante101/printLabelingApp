package com.example.fibra_labeling.di

import com.example.fibra_labeling.data.network.networkModule
import com.example.fibra_labeling.data.repository.PesajeRespository
import kotlinx.serialization.json.Json
import okhttp3.OkHttpClient
import org.koin.core.module.Module
import org.koin.dsl.module

val appModules: List<Module> = listOf (
    networkModule,
)