package com.iliasg.startrekfleetcommand.buildings.data.datasources.remote.mappers

import com.iliasg.startrekfleetcommand.buildings.data.datasources.local.entity.BuildingEntity
import com.iliasg.startrekfleetcommand.buildings.data.datasources.local.entity.BuildingLevelEntity
import com.iliasg.startrekfleetcommand.buildings.data.datasources.remote.dto.BuildingDto
import com.iliasg.startrekfleetcommand.buildings.data.datasources.remote.dto.ItemsDto
import com.iliasg.startrekfleetcommand.buildings.data.datasources.remote.dto.RequirementDto
import com.iliasg.startrekfleetcommand.core.data.converters.ConverterHandler
import com.iliasg.startrekfleetcommand.core.data.converters.ItemsConverter
import com.iliasg.startrekfleetcommand.core.data.converters.RequirementListConverter
import com.iliasg.startrekfleetcommand.core.domain.models.Bonus
import com.iliasg.startrekfleetcommand.core.domain.models.Items
import com.iliasg.startrekfleetcommand.core.domain.models.Requirement
import com.iliasg.startrekfleetcommand.core.domain.models.ResourceType

@ExperimentalStdlibApi
fun BuildingDto.toLocalFormat(
    converter: ConverterHandler
): Pair<BuildingEntity, List<BuildingLevelEntity>> {

    val building = BuildingEntity(
        id = id,
        name = name,
        description = description,
        group = group,
        startLevel = start_level,
        bonusHeaders = converter.bonusHeaderConverter.fromBonusMap(
            bonuses?.associate {
                it.id to Bonus(name = it.name, isPercentage = it.is_percentage)
            }
        )
    )

    val buildingLevels = levels.map { level ->
        BuildingLevelEntity(
            buildingId = id,
            level = level.level,
            power = level.power,
            increasedPower = level.increased_power,
            buildTime = level.build_time,
            bonusValues = converter.bonusValuesConverter.fromBonusValueMap(
                level.bonuses?.associate { it.id to it.value }
            ),
            requiredOpsLevel = level.required_ops_level,
            requirements = level.requirements.toRequirementString(converter.requirementListConverter),
            buildCosts = level.build_costs.toItemsString(converter.itemsConverter),
            rewards = level.rewards?.toItemsString(converter.itemsConverter)
        )
    }

    return building to buildingLevels
}

@ExperimentalStdlibApi
private fun List<RequirementDto>.toRequirementString(handler: RequirementListConverter): String {
    return handler.fromRequirements(
        map {
            Requirement(
                id = it.id,
                type = it.type,
                name = it.name,
                value = it.value
            )
        }
    )
}

@ExperimentalStdlibApi
private fun ItemsDto.toItemsString(handler: ItemsConverter): String {
    val res = resources.map {
        ResourceType.Resource(
            id = it.id,
            name = it.type,
            value = it.value
        )
    }
    val mat = materials.map {
        ResourceType.Material(
            id = it.id,
            name = it.type,
            grade = it.grade,
            rarity = it.rarity,
            value = it.value
        )
    }
    val other = others.map {
        ResourceType.Other(
            id = it.id,
            name = it.type,
            value = it.value
        )
    }

    return handler.fromItems(
        Items(
            resources = res,
            materials = mat,
            others = other
        )
    )
}