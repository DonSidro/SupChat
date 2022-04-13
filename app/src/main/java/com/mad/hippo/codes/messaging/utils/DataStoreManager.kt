package com.mad.hippo.codes.messaging.utils

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import kotlinx.coroutines.flow.first

class DataStoreManager( private val dataStore: DataStore<Preferences>)  {

    private val preferenceFlow = dataStore.data

    suspend fun <T> getPreference(preferenceEntry: PreferenceRequest<T>) =
        preferenceFlow.first()[preferenceEntry.key] ?: preferenceEntry.defaultValue


    suspend fun <T> editPreference(key: Preferences.Key<T>, newValue: T) {
        dataStore.edit { preferences ->
            preferences[key] = newValue
        }
    }

    suspend fun clearPreferences() {
        dataStore.edit { preferences -> preferences.clear() }
    }
}