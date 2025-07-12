package com.example.fibra_labeling.di

// DataStoreModule.kt
import android.content.Context
import androidx.datastore.preferences.preferencesDataStore
import com.example.fibra_labeling.datastore.EmpresaPrefs
import com.example.fibra_labeling.datastore.GeneralPreference
import com.example.fibra_labeling.datastore.ImpresoraPreferences
import com.example.fibra_labeling.datastore.ThemeManager
import com.example.fibra_labeling.datastore.UserLoginPreference
import org.koin.dsl.module
import kotlin.math.sin

val Context.dataStore by preferencesDataStore(name = "impresora_prefs")

val dataStoreModule = module {
    single { get<Context>().dataStore }
    single { ImpresoraPreferences(get()) }
    single { UserLoginPreference(get()) }
    single { EmpresaPrefs(get()) }
    single{ GeneralPreference(get()) }
    single { ThemeManager(get()) }
}
