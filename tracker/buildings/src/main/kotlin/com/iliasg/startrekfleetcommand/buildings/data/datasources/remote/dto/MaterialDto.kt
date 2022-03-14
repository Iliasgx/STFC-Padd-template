package com.iliasg.startrekfleetcommand.buildings.data.datasources.remote.dto

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class MaterialDto(
    val id: Long,
    val type: String,
    val grade: Int,
    val rarity: Int,
    val value: Long
)