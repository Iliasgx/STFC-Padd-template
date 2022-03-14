package com.iliasg.startrekfleetcommand.buildings.data.datasources.remote.dto

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class ResourceDto(
    val id: Long,
    val type: String,
    val value: Long
)