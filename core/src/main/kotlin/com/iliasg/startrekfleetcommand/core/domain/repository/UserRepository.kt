package com.iliasg.startrekfleetcommand.core.domain.repository

import kotlinx.coroutines.flow.Flow

interface UserRepository {

    suspend fun getOperationsLevel(): Int

    fun getAllBuildingLevels(): Flow<Map<Long, Int>>

    fun getBuildingLevel(buildingId: Long): Flow<Int?>

    suspend fun putBuildingLevel(buildingId: Long, level: Int)
}