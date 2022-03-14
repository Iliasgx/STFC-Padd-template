package com.iliasg.startrekfleetcommand.core.domain.models

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class Requirement(
    val id: Long,
    val type: String,
    val name: String,
    val value: Long)