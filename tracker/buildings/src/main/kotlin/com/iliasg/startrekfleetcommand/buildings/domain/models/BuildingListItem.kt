package com.iliasg.startrekfleetcommand.buildings.domain.models

sealed class BuildingListItem {
    data class SingleItem(
        val id: Long,
        val name: String,
        val startLevel: Int,
        val totalLevels: Int,
        val availableLevels: Int,
        val currentLevel: Int
    ) : BuildingListItem()

    data class GroupItem(
        val group: BuildingGroup,
        val availableLevels: Int,
        val progress: Int
    ) : BuildingListItem()
}
