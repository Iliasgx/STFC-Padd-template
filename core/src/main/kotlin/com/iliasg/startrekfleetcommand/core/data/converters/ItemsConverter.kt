package com.iliasg.startrekfleetcommand.core.data.converters

import com.iliasg.startrekfleetcommand.core.domain.models.Items
import com.squareup.moshi.Moshi
import com.squareup.moshi.adapter

@ExperimentalStdlibApi
class ItemsConverter(
    private val moshi: Moshi
) {

    /*private val type = Types.newParameterizedType(Items::class.java)
    private val adapter by lazy { moshi.adapter<Items>(type) }*/

    private val adapter = moshi.adapter<Items>()

    fun toItems(json: String): Items {
        return json.let { adapter.fromJson(it)!! }
    }

    fun fromItems(items: Items): String {
        return items.let { adapter.toJson(it) }
    }
}