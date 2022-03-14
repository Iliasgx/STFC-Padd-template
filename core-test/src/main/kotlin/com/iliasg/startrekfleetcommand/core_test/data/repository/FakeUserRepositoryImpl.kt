package com.iliasg.startrekfleetcommand.core_test.data.repository

import com.iliasg.startrekfleetcommand.core.domain.repository.UserRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class FakeUserRepositoryImpl : UserRepository {

    var currentLevels: MutableMap<Long, Int> = mutableMapOf()

    override suspend fun getOperationsLevel(): Int {
        return currentLevels[0L]!!
    }

    override fun getAllBuildingLevels(): Flow<Map<Long, Int>> {
        return flow {
            currentLevels
        }
    }

    override fun getBuildingLevel(buildingId: Long): Flow<Int> {
        return flow {
            currentLevels[buildingId]
        }
    }

    override suspend fun putBuildingLevel(buildingId: Long, level: Int) {
        currentLevels[buildingId] = level
    }
}