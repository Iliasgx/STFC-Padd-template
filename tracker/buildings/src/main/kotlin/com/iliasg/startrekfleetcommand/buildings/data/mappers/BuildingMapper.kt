package com.iliasg.startrekfleetcommand.buildings.data.mappers

import com.iliasg.startrekfleetcommand.buildings.data.datasources.local.entity.BuildingLevelEntity
import com.iliasg.startrekfleetcommand.buildings.data.datasources.local.models.BuildingWithLevel
import com.iliasg.startrekfleetcommand.buildings.domain.models.Building
import com.iliasg.startrekfleetcommand.buildings.domain.models.BuildingLevel
import com.iliasg.startrekfleetcommand.core.data.converters.ConverterHandler

@ExperimentalStdlibApi
internal fun BuildingWithLevel.toBuilding(handler: ConverterHandler): Building {
    return Building(
        id = building.id,
        name = building.name,
        description = building.description,
        startLevel = building.startLevel,
        bonuses = building.bonusHeaders?.let { handler.bonusHeaderConverter.toBonusMap(it) },
        levelCount = level_count,
        levelDetails = level.toBuildingLevel(handler)
    )
}

@ExperimentalStdlibApi
private fun BuildingLevelEntity.toBuildingLevel(handler: ConverterHandler): BuildingLevel {
    return BuildingLevel(
        level = level,
        increasedPower = increasedPower,
        buildTime = buildTime,
        bonuses = bonusValues?.let { handler.bonusValuesConverter.toBonusValueMap(it) },
        requiredOpsLevel = requiredOpsLevel,
        requirements = handler.requirementListConverter.toRequirements(requirements),
        buildCosts = handler.itemsConverter.toItems(buildCosts),
        rewards = rewards?.let { handler.itemsConverter.toItems(it) }
    )
}