package com.iliasg.startrekfleetcommand.core.domain.models

import com.squareup.moshi.JsonClass

sealed class ResourceType(
    open val id: Long,
    open val name: String,
    open val value: Long,
) {
    @JsonClass(generateAdapter = true)
    data class Resource(
        override val id: Long,
        override val name: String,
        override val value: Long,
    ) : ResourceType(id, name, value)

    @JsonClass(generateAdapter = true)
    data class Material(
        override val id: Long,
        override val name: String,
        val grade: Int,
        val rarity: Int,
        override val value: Long,
    ) : ResourceType(id, name, value)

    @JsonClass(generateAdapter = true)
    data class Other(
        override val id: Long,
        override val name: String,
        override val value: Long,
    ) : ResourceType(id, name, value)
}