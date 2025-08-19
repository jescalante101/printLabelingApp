package com.example.fibra_labeling.datastore

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class EmpresaPrefs(private val dataStore: DataStore<Preferences>) {
    companion object {
        val EMPRESA_KEY = stringPreferencesKey("empresa_seleccionada")
        val SyncCompletedKey = booleanPreferencesKey("sync_completed")

        val EMPRESA_ID_KEY = stringPreferencesKey("empresa_id")
    }

    val empresaSeleccionada: Flow<String> = dataStore.data.map { it[EMPRESA_KEY] ?: "" }
    val empresaId: Flow<String> = dataStore.data.map { it[EMPRESA_ID_KEY] ?: "" }
    val syncCompleted: Flow<Boolean> = dataStore.data.map { it[SyncCompletedKey] ?: false }


    suspend fun guardarEmpresa(empresa: String) {
        //SI LA EMPRESA ES Fibrafil empresa_id=01
        val empresaId = if (empresa == "Fibrafil") "01" else "02"
        dataStore.edit { prefs ->
            prefs[EMPRESA_KEY] = empresa
            prefs[EMPRESA_ID_KEY] = empresaId
        }
    }
    suspend fun setSyncCompleted(completed: Boolean) {
        dataStore.edit { prefs ->
            prefs[SyncCompletedKey] = completed
        }
    }

}