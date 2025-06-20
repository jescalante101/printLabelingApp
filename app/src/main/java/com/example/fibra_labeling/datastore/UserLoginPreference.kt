package com.example.fibra_labeling.datastore

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class UserLoginPreference(private val dataStore: DataStore<Preferences>) {
    companion object {
        val COD_USER = stringPreferencesKey("username_code")
        val USER_NAME = stringPreferencesKey("username_full")
        val DOC_ENTRY = stringPreferencesKey("doc_entry")
    }

    val userCode: Flow<String> = dataStore.data.map { it[COD_USER] ?: "" }
    val userName: Flow<String> = dataStore.data.map { it[USER_NAME] ?: "" }
    val docEntry: Flow<String> = dataStore.data.map { it[DOC_ENTRY] ?: "" }

    suspend fun saveUserLogin(userCode: String, name:String,docEntry:String) {
        dataStore.edit { prefs ->
            prefs[COD_USER] = userCode
            prefs[USER_NAME] = name
            prefs[DOC_ENTRY]=docEntry
        }
    }
}