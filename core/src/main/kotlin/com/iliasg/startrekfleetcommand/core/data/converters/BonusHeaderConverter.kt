package com.iliasg.startrekfleetcommand.core.data.converters

import com.iliasg.startrekfleetcommand.core.domain.models.Bonus
import com.squareup.moshi.Moshi
import com.squareup.moshi.adapter

@ExperimentalStdlibApi
class BonusHeaderConverter(
    moshi: Moshi
) {
    private val adapter = moshi.adapter<Map<Int, Bonus>?>()

    fun toBonusMap(json: String): Map<Int, Bonus>? {
        return adapter.fromJson(json)
    }

    fun fromBonusMap(bonuses: Map<Int, Bonus>?): String {
        return adapter.toJson(bonuses)
    }
}