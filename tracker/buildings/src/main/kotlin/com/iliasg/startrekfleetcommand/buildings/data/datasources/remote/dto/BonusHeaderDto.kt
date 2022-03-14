package com.iliasg.startrekfleetcommand.buildings.data.datasources.remote.dto

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class BonusHeaderDto(
    val id: Int,
    val name: String,
    val is_percentage: Boolean
)