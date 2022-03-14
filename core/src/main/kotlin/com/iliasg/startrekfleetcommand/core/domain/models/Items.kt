package com.iliasg.startrekfleetcommand.core.domain.models

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class Items(
    val resources: List<ResourceType.Resource> = emptyList(),
    val materials: List<ResourceType.Material> = emptyList(),
    val others: List<ResourceType.Other> = emptyList()
)