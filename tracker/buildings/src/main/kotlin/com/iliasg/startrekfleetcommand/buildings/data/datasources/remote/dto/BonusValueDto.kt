package com.iliasg.startrekfleetcommand.buildings.data.datasources.remote.dto

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class BonusValueDto(
    val id: Int,
    val value: Double
)