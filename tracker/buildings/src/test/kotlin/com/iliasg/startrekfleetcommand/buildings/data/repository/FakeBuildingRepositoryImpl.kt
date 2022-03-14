package com.iliasg.startrekfleetcommand.buildings.data.repository

import com.iliasg.startrekfleetcommand.buildings.data.datasources.local.entity.BuildingEntity
import com.iliasg.startrekfleetcommand.buildings.data.datasources.local.entity.BuildingLevelEntity
import com.iliasg.startrekfleetcommand.buildings.data.datasources.local.models.BuildingWithLevel
import com.iliasg.startrekfleetcommand.buildings.data.mappers.toBuildingBasic
import com.iliasg.startrekfleetcommand.buildings.domain.models.Building
import com.iliasg.startrekfleetcommand.buildings.domain.models.BuildingBasic
import com.iliasg.startrekfleetcommand.buildings.domain.models.BuildingGroup
import com.iliasg.startrekfleetcommand.buildings.domain.repository.BuildingRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

@ExperimentalStdlibApi
class FakeBuildingRepositoryImpl : BuildingRepository {

    private val buildings = mutableMapOf<BuildingEntity, List<BuildingLevelEntity>>()

    override fun getAllBuildings(group: BuildingGroup?): Flow<List<BuildingBasic>> {
        return flow {
            buildings.let { all ->
                group?.let { grp ->
                    all.filterKeys { it.group == grp.ordinal }
                } ?: all
            }.map { it.toBuildingBasic() }
        }
    }

    override suspend fun countLevels(buildingId: Long): Int {
        return buildings
            .toList()
            .first { it.first.id == buildingId }
            .second
            .count()
    }

    override fun getBuildingWithLevel(buildingId: Long, level: Int): Flow<Building> {
        return flow {
            buildings
                .toList()
                .first { it.first.id == buildingId }
                .let { (building, levels) ->
                    val buildingLevel = levels.first { it.level == level }

                    BuildingWithLevel(
                        building = building,
                        level = buildingLevel,
                        level_count = levels.count()
                    )
                }
        }
    }

    override suspend fun insertBuildingWithLevels(
        building: BuildingEntity,
        levels: List<BuildingLevelEntity>
    ) {
        buildings[building] = levels
    }
}