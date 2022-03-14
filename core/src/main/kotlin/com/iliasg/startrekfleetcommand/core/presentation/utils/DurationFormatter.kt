package com.iliasg.startrekfleetcommand.core.presentation.utils

import android.content.Context
import com.iliasg.startrekfleetcommand.core.R
import kotlin.time.DurationUnit
import kotlin.time.toDuration

object DurationFormatter {

    fun format(ctx: Context, time: Long, components: Int = 2): String {
        time.toDuration(DurationUnit.SECONDS).toComponents { mDays, hours, minutes, seconds, _ ->
            var years = 0
            var days = mDays.toInt()

            if (days >= 365) {
                years = (days / 365)
                days -= (years * 365)
            }

            val comps = mutableListOf<String>()

            fun check(resId: Int, value: Int): Boolean {
                if (comps.size < components) {
                    if (value != 0) comps.add(ctx.getString(resId, value))
                    return true
                }
                return false
            }

            if (check(R.string.short_year, years)) {
                if (check(R.string.short_day, days)) {
                    if (check(R.string.short_hour, hours)) {
                        if (check(R.string.short_minute, minutes)) {
                            check(R.string.short_second, seconds)
                        }
                    }
                }
            }

            return if (comps.isEmpty()) {
                ctx.getString(R.string.short_second, 0)
            } else {
                comps.joinToString(separator = " ")
            }

        }
    }
}