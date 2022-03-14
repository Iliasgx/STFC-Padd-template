package com.iliasg.startrekfleetcommand.buildings.data.repository

import com.iliasg.startrekfleetcommand.buildings.data.datasources.local.BuildingDao
import com.iliasg.startrekfleetcommand.buildings.data.datasources.local.entity.BuildingEntity
import com.iliasg.startrekfleetcommand.buildings.data.datasources.local.entity.BuildingLevelEntity
import com.iliasg.startrekfleetcommand.buildings.data.mappers.toBuilding
import com.iliasg.startrekfleetcommand.buildings.data.mappers.toBuildingBasic
import com.iliasg.startrekfleetcommand.buildings.domain.models.Building
import com.iliasg.startrekfleetcommand.buildings.domain.models.BuildingBasic
import com.iliasg.startrekfleetcommand.buildings.domain.models.BuildingGroup
import com.iliasg.startrekfleetcommand.buildings.domain.repository.BuildingRepository
import com.iliasg.startrekfleetcommand.core.data.converters.ConverterHandler
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

@ExperimentalStdlibApi
class BuildingRepositoryImpl(
    private val dao: BuildingDao,
    private val converterHandler: ConverterHandler
) : BuildingRepository {

    override fun getAllBuildings(group: BuildingGroup?): Flow<List<BuildingBasic>> {
        return dao.getAllBuildings(group?.ordinal).map { buildings ->
            buildings.map { it.toBuildingBasic() }
        }
    }

    override suspend fun countLevels(buildingId: Long): Int {
        return dao.countLevels(buildingId)
    }

    override fun getBuildingWithLevel(buildingId: Long, level: Int): Flow<Building> {
        return dao.getBuildingWithLevel(buildingId, level).map {
            it.toBuilding(converterHandler)
        }
    }

    override suspend fun insertBuildingWithLevels(
        building: BuildingEntity,
        levels: List<BuildingLevelEntity>
    ) {
        dao.insertBuildingWithLevels(building, levels)
    }
}