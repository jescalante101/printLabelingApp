package com.example.fibra_labeling.di

import android.util.Log
import com.example.fibra_labeling.data.local.dao.fibraprint.ApiConfigDao
import com.example.fibra_labeling.data.network.fibrafil.ApiService
import com.example.fibra_labeling.data.network.fibraprint.PrintApiService
import com.example.fibra_labeling.data.remote.fibrafil.FillRepository
import com.example.fibra_labeling.data.remote.fibrafil.FillRepositoryImpl
import com.example.fibra_labeling.data.remote.OitmRepository
import com.example.fibra_labeling.data.remote.OitmRespositoryImpl
import com.example.fibra_labeling.data.remote.PesajeRepositoryImpl
import com.example.fibra_labeling.data.remote.PesajeRepository
import com.example.fibra_labeling.data.remote.SettingRepository
import com.example.fibra_labeling.data.remote.SettingRepositoryImpl
import com.example.fibra_labeling.datastore.EmpresaPrefs
import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
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

    factory {
        val dao = get<ApiConfigDao>()
        val empresaStore = get<EmpresaPrefs>()
        val contentType = "application/json".toMediaType()

        // Mejor manejo para obtener la configuración
        val configSeleccionada = try {
            val empresa = runBlocking { empresaStore.empresaSeleccionada.first() }
            runBlocking { dao.getSelectedConfigByEmpresa(empresa) }
        } catch (e: Exception) {
            Log.e("RetrofitModule", "Error obteniendo configuración: ${e.message}")
            null
        }

        val url = configSeleccionada?.urlBase ?: "http://192.168.1.7:8080/backend/"

        Log.d("RetrofitModule", "URL configurada: $url")

        Retrofit.Builder()
            .baseUrl(url)
            .addConverterFactory(get<Json>().asConverterFactory(contentType))
            .client(get())
            .build()
    }

    single<ApiService> { get<Retrofit>().create(ApiService::class.java) }
    single<PrintApiService> { get<Retrofit>().create(PrintApiService::class.java) }

    single<PesajeRepository> { PesajeRepositoryImpl(get()) }
    single<OitmRepository> { OitmRespositoryImpl(get()) }
    single<SettingRepository> { SettingRepositoryImpl(get()) }
    single<FillRepository> { FillRepositoryImpl(get()) }

}
