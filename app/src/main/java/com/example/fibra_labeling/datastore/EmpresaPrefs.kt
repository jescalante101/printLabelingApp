package com.example.fibra_labeling.datastore

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class EmpresaPrefs(private val dataStore: DataStore<Preferences>) {
    companion object {
        val EMPRESA_KEY = stringPreferencesKey("empresa_seleccionada")
    }

    val empresaSeleccionada: Flow<String> = dataStore.data.map { it[EMPRESA_KEY] ?: "" }

    suspend fun guardarEmpresa(empresa: String) {
        dataStore.edit { prefs ->
            prefs[EMPRESA_KEY] = empresa
        }
    }
}