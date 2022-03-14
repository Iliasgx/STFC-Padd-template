package com.iliasg.startrekfleetcommand.buildings.domain.models

data class BuildingBasic(
    val id: Long,
    val name: String,
    val group: BuildingGroup,
    val startLevel: Int,
    val levels: List<Level>
) {
    data class Level(
        val level: Int,
        val requiredOpsLevel: Int
    )
}
