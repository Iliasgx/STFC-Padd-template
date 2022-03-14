package com.iliasg.startrekfleetcommand.buildings.data.datasources.remote.dto

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class BuildingDto(
    val id: Long,
    val name: String,
    val description: String,
    val group: Int,
    val start_level: Int,
    val bonuses: List<BonusHeaderDto>?,
    val levels: List<LevelDto>
)