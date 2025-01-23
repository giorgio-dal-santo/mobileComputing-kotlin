package com.example.mangiaebasta.model.dataSource

import android.util.Log
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.core.Preferences.Key
import androidx.datastore.preferences.core.booleanPreferencesKey
import kotlinx.coroutines.flow.first
import androidx.datastore.preferences.core.edit


class PreferencesController private constructor(
    private val dataStore: DataStore<Preferences>
) {

    companion object {
        val SID = stringPreferencesKey("sid")
        val UID = intPreferencesKey("uid")
        val HAS_ALREADY_RUN = booleanPreferencesKey("has_already_run")
        val IS_REGISTERED = booleanPreferencesKey("is_registered")
        val CAN_USE_LOCATION = booleanPreferencesKey("can_use_location")
        // manca memorizzazione menu e order data

        private var INSTANCE: PreferencesController? = null

        // Avoids the creation of multiple instances of the PreferencesController when the App is restarted via code
        fun getInstance(dataStore: DataStore<Preferences>): PreferencesController {
            return INSTANCE ?: synchronized(this) {
                INSTANCE ?: PreferencesController(dataStore).also { INSTANCE = it }
            }
        }

    }

    suspend fun <T> get(prefKey: Key<T>): T? {
        val prefs = dataStore.data.first()
        Log.d("Preference", "get: ${prefs[prefKey]}")
        return prefs[prefKey]
    }

    suspend fun <T> set(prefKey: Key<T>, value: T) {
        dataStore.edit { prefs ->
            prefs[prefKey] = value
        }
        Log.d("Preference", "set: $value")
    }

    suspend fun memorizeSessionKeys(sid: String, uid: Int) {
        set(SID, sid)
        set(UID, uid)
    }

    suspend fun isFirstRun(): Boolean {
        val hasAlreadyRun = this.get(HAS_ALREADY_RUN)
        if (hasAlreadyRun == null || hasAlreadyRun == false) {
            this.set(HAS_ALREADY_RUN, true)
            this.set(IS_REGISTERED, false)
            return true
        }
        return false
    }
}