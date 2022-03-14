package com.iliasg.startrekfleetcommand.core.data.repository

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.core.intPreferencesKey
import com.iliasg.startrekfleetcommand.core.data.datasources.preferences.LocalPreferences
import com.iliasg.startrekfleetcommand.core.data.datasources.preferences.LocalPreferences.prefUserBuildings
import com.iliasg.startrekfleetcommand.core.domain.repository.UserRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import java.io.IOException
import javax.inject.Inject

class UserRepositoryImpl @Inject constructor(
    private val context: Context
) : UserRepository {

    override suspend fun getOperationsLevel(): Int {
        val key = intPreferencesKey(LocalPreferences.PREF_OPERATIONS_KEY.toString())
        return context.prefUserBuildings.data.first()[key] ?: 1
    }

    override fun getAllBuildingLevels(): Flow<Map<Long, Int>> {
        return context.prefUserBuildings.getAllLevels()
    }

    override fun getBuildingLevel(buildingId: Long): Flow<Int?> {
        return context.prefUserBuildings.getLevel(buildingId)
    }

    override suspend fun putBuildingLevel(buildingId: Long, level: Int) {
        return context.prefUserBuildings.putLevel(buildingId, level)
    }

    private fun DataStore<Preferences>.getAllLevels(): Flow<Map<Long, Int>> {
        return this.data
            .catch { exception ->
                if (exception is IOException) {
                    emit(emptyPreferences())
                } else {
                    throw exception
                }
            }.map { items ->
                items.asMap()
                    .asSequence()
                    .associate { (key, value) -> key.name.toLong() to (value as Int) }
            }
    }

    private fun DataStore<Preferences>.getLevel(parentId: Long): Flow<Int?> {
        val key = intPreferencesKey(parentId.toString())

        return this.data
            .catch { exception ->
                if (exception is IOException) {
                    emit(emptyPreferences())
                } else {
                    throw exception
                }
            }.map { it[key] }
    }

    private suspend fun DataStore<Preferences>.putLevel(parentId: Long, level: Int) {
        val key = intPreferencesKey(parentId.toString())

        this.edit {
            it[key] = level
        }
    }
}