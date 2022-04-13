package com.mad.hippo.codes.messaging.utils

import android.content.Context
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore

private const val DATASTORE_NAME = "key_preferences"
val Context.baseDataStore by preferencesDataStore(
    name = DATASTORE_NAME
)

val PUBLIC_KEY = stringPreferencesKey("pref_public_key")
val PRIVATE_KEY = stringPreferencesKey("pref_private_key")