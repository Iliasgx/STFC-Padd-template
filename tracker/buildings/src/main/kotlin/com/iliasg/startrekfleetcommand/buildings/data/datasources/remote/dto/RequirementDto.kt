package com.iliasg.startrekfleetcommand.buildings.data.datasources.remote.dto

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class RequirementDto(
    val id: Long,
    val type: String,
    val name: String,
    val value: Long
)