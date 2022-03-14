package com.iliasg.startrekfleetcommand.buildings.domain.mappers

import com.iliasg.startrekfleetcommand.buildings.domain.models.BuildingBasic
import com.iliasg.startrekfleetcommand.buildings.domain.models.BuildingListItem

fun BuildingBasic.toSingleItem(
    currentOpsLevel: Int,
    currentLevel: Int? = null
): BuildingListItem.SingleItem {

    return BuildingListItem.SingleItem(
        id = id,
        name = name,
        startLevel = startLevel,
        totalLevels = levels.count(),
        availableLevels = levels.count { it.requiredOpsLevel <= currentOpsLevel },
        currentLevel = currentLevel ?: startLevel // Set startLevel as default
    )
}

fun List<BuildingBasic>.toGroupItem(
    currentOpsLevel: Int,
    levels: Map<Long, Int>
): BuildingListItem.GroupItem {

    val available = fold(0) { acc, building ->
        acc + building.levels.count { it.requiredOpsLevel <= currentOpsLevel }
    }
    val progress = fold(0) { acc, building ->
        acc + (levels[building.id] ?: building.startLevel) // Set startLevel as default
    }

    return BuildingListItem.GroupItem(
        group = first().group,
        availableLevels = available,
        progress = progress
    )
}