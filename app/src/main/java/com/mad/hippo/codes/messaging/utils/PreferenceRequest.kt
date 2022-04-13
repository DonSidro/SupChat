package com.mad.hippo.codes.messaging.utils

import androidx.datastore.preferences.core.Preferences

open class PreferenceRequest<T>(
    val key: Preferences.Key<T>,
    val defaultValue: T,
)