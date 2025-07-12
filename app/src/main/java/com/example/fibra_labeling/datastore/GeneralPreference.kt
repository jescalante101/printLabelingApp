package com.example.fibra_labeling.datastore

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class GeneralPreference(private val dataStore: DataStore<Preferences>) {
    companion object {
        val CONTEO_USE_MODE= booleanPreferencesKey("conteo_use_mode")
    }

    val conteoUseMode: Flow<Boolean> = dataStore.data.map { it[CONTEO_USE_MODE] ?: false }


    suspend fun guardarConteoUseMode(conteoUseMode: Boolean) {
        dataStore.edit { prefs ->
            prefs[CONTEO_USE_MODE] = conteoUseMode
        }
    }



}