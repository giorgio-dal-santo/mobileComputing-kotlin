package com.example.mangiaebasta.data.dataSource.preference

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import androidx.datastore.preferences.core.Preferences.Key
import kotlinx.coroutines.flow.first
import androidx.datastore.preferences.core.edit

val Context.dataStore by preferencesDataStore(name = "appStatus")

object Preference {
    val SID = stringPreferencesKey("sid")
    val UID = intPreferencesKey("uid")

    suspend fun <T> get(dataStore: DataStore<Preferences>, prefKey: Key<T>) : T? {
        val prefs = dataStore.data.first()
        return prefs[prefKey]
    }

    suspend fun <T> set(dataStore: DataStore<Preferences>, prefKey: Key<T>, value: T) {
        dataStore.edit { prefs ->
            prefs[prefKey] = value
        }
    }
}