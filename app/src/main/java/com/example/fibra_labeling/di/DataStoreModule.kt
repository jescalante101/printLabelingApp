package com.example.fibra_labeling.di

// DataStoreModule.kt
import android.content.Context
import androidx.datastore.preferences.preferencesDataStore
import com.example.fibra_labeling.datastore.ImpresoraPreferences
import org.koin.dsl.module

val Context.dataStore by preferencesDataStore(name = "impresora_prefs")

val dataStoreModule = module {
    single { get<Context>().dataStore }
    single { ImpresoraPreferences(get()) }
}
