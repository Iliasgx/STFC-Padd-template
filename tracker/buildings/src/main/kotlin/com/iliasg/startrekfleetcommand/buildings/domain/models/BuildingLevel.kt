package com.iliasg.startrekfleetcommand.buildings.domain.models

import com.iliasg.startrekfleetcommand.core.domain.models.Items
import com.iliasg.startrekfleetcommand.core.domain.models.Requirement

data class BuildingLevel(
    val level: Int,
    val increasedPower: Int,
    val buildTime: Long,
    val bonuses: Map<Int, Double>?,
    val requiredOpsLevel: Int,
    val requirements: List<Requirement>,
    val buildCosts: Items,
    val rewards: Items?
)
