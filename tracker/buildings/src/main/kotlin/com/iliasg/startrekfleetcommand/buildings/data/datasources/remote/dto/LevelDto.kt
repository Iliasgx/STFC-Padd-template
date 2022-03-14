package com.iliasg.startrekfleetcommand.buildings.data.datasources.remote.dto

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class LevelDto(
    val level: Int,
    val power: Int,
    val increased_power: Int,
    val build_time: Long,
    val bonuses: List<BonusValueDto>?,
    val required_ops_level: Int,
    val requirements: List<RequirementDto>,
    val build_costs: ItemsDto,
    val rewards: ItemsDto?
)