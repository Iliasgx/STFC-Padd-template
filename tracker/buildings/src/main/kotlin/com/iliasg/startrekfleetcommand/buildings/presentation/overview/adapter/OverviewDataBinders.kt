package com.iliasg.startrekfleetcommand.buildings.presentation.overview.adapter

import android.widget.TextView
import androidx.databinding.BindingAdapter
import com.iliasg.startrekfleetcommand.buildings.R
import com.iliasg.startrekfleetcommand.buildings.domain.models.BuildingGroup

@BindingAdapter("group_name")
internal fun setGroupName(view: TextView, group: BuildingGroup) {
    group.groupName?.let {
        view.text = view.context.getString(it)
    }
}

@BindingAdapter("progress_text")
internal fun setProgressText(view: TextView, progress: Int) {
    view.text = view.context.getString(R.string.item_building_group_progress, progress)
}