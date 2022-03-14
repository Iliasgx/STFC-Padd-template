package com.iliasg.startrekfleetcommand.buildings.data.datasources.remote.dto

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class ItemsDto(
    val resources: List<ResourceDto>,
    val materials: List<MaterialDto>,
    val others: List<ResourceDto>
)