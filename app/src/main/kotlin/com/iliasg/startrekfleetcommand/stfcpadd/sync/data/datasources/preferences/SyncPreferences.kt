package com.iliasg.startrekfleetcommand.stfcpadd.sync.data.datasources.preferences

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.longPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore

object SyncPreferences {
    val Context.prefSync: DataStore<Preferences> by preferencesDataStore(name = "data_sync")

    val PREF_LATEST_UPDATE = longPreferencesKey("latest_update")
    val PREF_LATEST_SHA = stringPreferencesKey("latest_sha")
    val PREF_LATEST_VERSION = stringPreferencesKey("latest_version")
}