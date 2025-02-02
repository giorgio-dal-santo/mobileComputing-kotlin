package com.example.mangiaebasta.model.dataSource

import android.util.Log
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.Preferences.Key
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import kotlinx.coroutines.flow.first

class PreferencesController private constructor(
    private val dataStore: DataStore<Preferences>
) {
    companion object {
        private val TAG = PreferencesController::class.simpleName
        val SID = stringPreferencesKey("sid")
        val UID = intPreferencesKey("uid")
        val HAS_ALREADY_RUN = booleanPreferencesKey("has_already_run")
        val IS_REGISTERED = booleanPreferencesKey("is_registered")
        private val KEY_LAST_SCREEN = stringPreferencesKey("last_screen")

        private var INSTANCE: PreferencesController? = null

        fun getInstance(dataStore: DataStore<Preferences>): PreferencesController {
            return INSTANCE ?: synchronized(this) {
                INSTANCE ?: PreferencesController(dataStore).also { INSTANCE = it }
            }
        }
    }

    suspend fun <T> get(prefKey: Key<T>): T? {
        val prefs = dataStore.data.first()
        Log.d(TAG, "get: ${prefs[prefKey]}")
        return prefs[prefKey]
    }

    suspend fun <T> set(prefKey: Key<T>, value: T) {
        dataStore.edit { prefs ->
            prefs[prefKey] = value
        }
        Log.d(TAG, "set: $value")
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

    suspend fun getLastScreen(): String {
        val currentScreen = get(KEY_LAST_SCREEN) ?: "home_stack"
        Log.d(TAG, "getLastScreen: $currentScreen")
        return currentScreen
    }

    suspend fun setLastScreen(screen: String) {
        set(KEY_LAST_SCREEN, screen)
        Log.d(TAG, "setLastScreen to: $screen")
    }
}