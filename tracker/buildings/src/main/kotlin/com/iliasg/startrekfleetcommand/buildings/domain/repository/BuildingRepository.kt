package com.iliasg.startrekfleetcommand.buildings.domain.repository

import com.iliasg.startrekfleetcommand.buildings.data.datasources.local.entity.BuildingEntity
import com.iliasg.startrekfleetcommand.buildings.data.datasources.local.entity.BuildingLevelEntity
import com.iliasg.startrekfleetcommand.buildings.domain.models.Building
import com.iliasg.startrekfleetcommand.buildings.domain.models.BuildingBasic
import com.iliasg.startrekfleetcommand.buildings.domain.models.BuildingGroup
import kotlinx.coroutines.flow.Flow

interface BuildingRepository {

     fun getAllBuildings(group: BuildingGroup? = null): Flow<List<BuildingBasic>>

     suspend fun countLevels(buildingId: Long): Int

     fun getBuildingWithLevel(buildingId: Long, level: Int): Flow<Building>

     suspend fun insertBuildingWithLevels(building: BuildingEntity, levels: List<BuildingLevelEntity>)
}