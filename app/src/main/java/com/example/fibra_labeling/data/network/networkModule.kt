package com.example.fibra_labeling.data.network

import com.example.fibra_labeling.data.repository.PesajeRepositoryImpl
import com.example.fibra_labeling.data.repository.PesajeRespository
import com.example.fibra_labeling.ui.screen.HomeViewModel
import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module
import retrofit2.Retrofit

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
            .build()
    }

    single {
        val contentType = "application/json".toMediaType()
        Retrofit.Builder()
            .baseUrl("http://192.168.20.227:7217/")
            .addConverterFactory(get<Json>().asConverterFactory(contentType))
            .client(get())
            .build()
    }

    single<ApiService> { get<Retrofit>().create(ApiService::class.java) }

    single<PesajeRespository> { PesajeRepositoryImpl(get()) }
    viewModel { HomeViewModel(get()) }
}
