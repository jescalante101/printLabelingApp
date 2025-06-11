package com.example.fibra_labeling.datastore

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.*
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class ImpresoraPreferences(private val dataStore: DataStore<Preferences>) {

    companion object {
        val IP_KEY = stringPreferencesKey("impresora_ip")
        val PUERTO_KEY = stringPreferencesKey("impresora_puerto")
        val NAME_KEY = stringPreferencesKey("impresora_name")
    }

    val impresoraIp: Flow<String> = dataStore.data.map { it[IP_KEY] ?: "" }
    val impresoraPuerto: Flow<String> = dataStore.data.map { it[PUERTO_KEY] ?: "9100" }
    val impresoraName: Flow<String> =dataStore.data.map { it[NAME_KEY] ?:"" }

    suspend fun guardarImpresora(ip: String, puerto: String,name:String) {
        dataStore.edit { prefs ->
            prefs[IP_KEY] = ip
            prefs[PUERTO_KEY] = puerto
            prefs[NAME_KEY] =name
        }
    }
}
