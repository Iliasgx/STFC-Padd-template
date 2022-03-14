package com.iliasg.startrekfleetcommand.core.data.converters

import com.squareup.moshi.Moshi
import com.squareup.moshi.adapter

@ExperimentalStdlibApi
class BonusValuesConverter(
    moshi: Moshi
) {
    private val adapter = moshi.adapter<Map<Int, Double>?>()

    fun toBonusValueMap(json: String): Map<Int, Double>? {
        return adapter.fromJson(json)
    }

    fun fromBonusValueMap(bonuses: Map<Int, Double>?): String? {
        return adapter.toJson(bonuses)
    }
}