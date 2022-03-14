package com.iliasg.startrekfleetcommand.core.presentation.utils

import android.widget.ListView
import android.widget.TextView
import androidx.databinding.BindingAdapter
import com.iliasg.startrekfleetcommand.core.R
import java.text.NumberFormat

@BindingAdapter("level_text")
fun setLevelText(view: TextView, level: Int) {
    view.text = view.context.getString(R.string.level, level)
}

@BindingAdapter("power_increase")
fun setPowerIncrease(view: TextView, power: Int) {
    val nrFormat = NumberFormat.getNumberInstance().format(power.toLong())
    val format = "+ $nrFormat"
    view.text = format
}

@BindingAdapter("duration")
fun setDuration(view: TextView, duration: Long) {
    view.text = DurationFormatter.format(view.context, duration)
}