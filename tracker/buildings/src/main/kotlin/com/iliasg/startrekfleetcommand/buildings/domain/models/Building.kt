package com.iliasg.startrekfleetcommand.buildings.domain.models

import com.iliasg.startrekfleetcommand.core.domain.models.Bonus

data class Building(
    val id: Long,
    val name: String,
    val description: String,
    val startLevel: Int,
    val bonuses: Map<Int, Bonus>?,
    val levelCount: Int,
    val levelDetails: BuildingLevel
)
