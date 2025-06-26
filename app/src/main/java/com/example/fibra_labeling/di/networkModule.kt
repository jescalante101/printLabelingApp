package com.example.fibra_labeling.di

import com.example.fibra_labeling.data.network.ApiService
import com.example.fibra_labeling.data.remote.FillRepository
import com.example.fibra_labeling.data.remote.FillRepositoryImpl
import com.example.fibra_labeling.data.remote.OitmRepository
import com.example.fibra_labeling.data.remote.OitmRespositoryImpl
import com.example.fibra_labeling.data.remote.PesajeRepositoryImpl
import com.example.fibra_labeling.data.remote.PesajeRepository
import com.example.fibra_labeling.data.remote.SettingRepository
import com.example.fibra_labeling.data.remote.SettingRepositoryImpl
import com.example.fibra_labeling.data.remote.SyncRepository
import com.example.fibra_labeling.data.remote.SyncRepositoryImpl
import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.dsl.module
import retrofit2.Retrofit
import java.util.concurrent.TimeUnit

@OptIn(ExperimentalSerializationApi::class)
val networkModule = module {

    single {
        Json {
            ignoreUnknownKeys = true
            isLenient = true
            encodeDefaults = true
        }
    }
    single {
        
        OkHttpClient.Builder()
            .addInterceptor(HttpLoggingInterceptor().apply {
                level = HttpLoggingInterceptor.Level.BODY
            })
            .connectTimeout(60, TimeUnit.SECONDS)    // Tiempo máximo para conectar al servidor
            .readTimeout(400, TimeUnit.SECONDS)       // Tiempo máximo esperando respuesta
            .writeTimeout(400, TimeUnit.SECONDS)
            .build()
    }
    single {

        val contentType = "application/json".toMediaType()
        Retrofit.Builder()
            .baseUrl("http://192.168.20.125:5000/")
            .addConverterFactory(get<Json>().asConverterFactory(contentType))
            .client(get())
            .build()
    }

    single<ApiService> { get<Retrofit>().create(ApiService::class.java) }

    single<PesajeRepository> { PesajeRepositoryImpl(get()) }
    single<OitmRepository> { OitmRespositoryImpl(get()) }
    single<SettingRepository> { SettingRepositoryImpl(get()) }
    single<FillRepository> { FillRepositoryImpl(get()) }

}
