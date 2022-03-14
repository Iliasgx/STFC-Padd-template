package com.iliasg.startrekfleetcommand.core.data.converters

import com.iliasg.startrekfleetcommand.core.domain.models.Requirement
import com.squareup.moshi.Moshi
import com.squareup.moshi.adapter

@ExperimentalStdlibApi
class RequirementListConverter(
    private val moshi: Moshi
) {

    /*private val type = Types.newParameterizedType(List::class.java, Requirement::class.java)
    private val adapter by lazy { moshi.adapter<List<Requirement>>(type) }*/

    private val adapter = moshi.adapter<List<Requirement>>()

    fun toRequirements(json: String): List<Requirement> {
        return adapter.fromJson(json) ?: emptyList()
    }

    fun fromRequirements(requirements: List<Requirement>): String {
        return adapter.toJson(requirements)
    }
}