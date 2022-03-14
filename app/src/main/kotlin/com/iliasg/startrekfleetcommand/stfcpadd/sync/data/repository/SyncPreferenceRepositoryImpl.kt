package com.iliasg.startrekfleetcommand.stfcpadd.sync.data.repository

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.emptyPreferences
import com.iliasg.startrekfleetcommand.stfcpadd.sync.data.datasources.preferences.SyncPreferences.PREF_LATEST_SHA
import com.iliasg.startrekfleetcommand.stfcpadd.sync.data.datasources.preferences.SyncPreferences.PREF_LATEST_UPDATE
import com.iliasg.startrekfleetcommand.stfcpadd.sync.data.datasources.preferences.SyncPreferences.PREF_LATEST_VERSION
import com.iliasg.startrekfleetcommand.stfcpadd.sync.data.datasources.preferences.SyncPreferences.prefSync
import com.iliasg.startrekfleetcommand.stfcpadd.sync.domain.repository.SyncPreferenceRepository
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.runBlocking
import javax.inject.Inject

class SyncPreferenceRepositoryImpl @Inject constructor(
    private val context: Context
) : SyncPreferenceRepository {

    override fun getLatestUpdate(): Long? = runBlocking {
        context.prefSync.data
            .catch {
                emit(emptyPreferences())
            }
            .map { preferences ->
                preferences[PREF_LATEST_UPDATE]
            }
            .first()
    }

    override fun getLastSha(): String = runBlocking {
        context.prefSync.data
            .catch {
                emit(emptyPreferences())
            }
            .map { preferences ->
                preferences[PREF_LATEST_SHA]
            }
            .firstOrNull() ?: ""
    }

    override fun getLastVersion(): String = runBlocking {
        context.prefSync.data
            .catch {
                emit(emptyPreferences())
            }
            .map { preferences ->
                preferences[PREF_LATEST_VERSION]
            }
            .firstOrNull() ?: ""
    }

    override suspend fun putLatestUpdate() {
        context.prefSync.edit {
            it[PREF_LATEST_UPDATE] = System.currentTimeMillis()
        }
    }

    override suspend fun putLatestSha(sha: String) {
        context.prefSync.edit {
            it[PREF_LATEST_SHA] = sha
        }
    }

    override suspend fun putLatestVersion(version: String) {
        context.prefSync.edit {
            it[PREF_LATEST_VERSION] = version
        }
    }
}