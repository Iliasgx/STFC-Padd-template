package com.iliasg.startrekfleetcommand.buildings.data.mappers

import com.iliasg.startrekfleetcommand.buildings.data.datasources.local.entity.BuildingEntity
import com.iliasg.startrekfleetcommand.buildings.data.datasources.local.entity.BuildingLevelEntity
import com.iliasg.startrekfleetcommand.buildings.domain.models.BuildingBasic
import com.iliasg.startrekfleetcommand.buildings.domain.models.BuildingGroup

fun Map.Entry<BuildingEntity, List<BuildingLevelEntity>>.toBuildingBasic(): BuildingBasic {
    val (building, levels) = this

    return BuildingBasic(
        id = building.id,
        name = building.name,
        group = BuildingGroup.values()[building.group],
        startLevel = building.startLevel,
        levels = levels.map { BuildingBasic.Level(it.level, it.requiredOpsLevel) }
    )
}