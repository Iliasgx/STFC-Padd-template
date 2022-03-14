package com.iliasg.startrekfleetcommand.core.data.datasources.preferences

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.preferencesDataStore

object LocalPreferences {
    const val PREF_OPERATIONS_KEY = 0L

    private const val preferenceBuildingsId = "user_buildings"
    private const val preferenceResearchId = "user_research"

    val Context.prefUserBuildings: DataStore<Preferences> by preferencesDataStore(name = preferenceBuildingsId)
    val Context.prefUserResearch: DataStore<Preferences> by preferencesDataStore(name = preferenceResearchId)
}