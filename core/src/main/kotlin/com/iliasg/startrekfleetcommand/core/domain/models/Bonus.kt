package com.iliasg.startrekfleetcommand.core.domain.models

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class Bonus(
    val name: String,
    val isPercentage: Boolean
)