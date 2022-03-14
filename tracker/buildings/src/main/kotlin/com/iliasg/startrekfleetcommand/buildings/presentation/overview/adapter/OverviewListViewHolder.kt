package com.iliasg.startrekfleetcommand.buildings.presentation.overview.adapter

import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.RecyclerView
import com.iliasg.startrekfleetcommand.buildings.databinding.ItemBuildingGroupBinding
import com.iliasg.startrekfleetcommand.buildings.databinding.ItemBuildingSingleBinding
import com.iliasg.startrekfleetcommand.buildings.domain.models.BuildingListItem
import com.iliasg.startrekfleetcommand.buildings.presentation.overview.adapter.OverviewAdapterEvent.*

internal sealed class OverviewListViewHolder(binding: ViewDataBinding) : RecyclerView.ViewHolder(binding.root) {

    abstract fun bind(item: BuildingListItem)

    class GroupViewHolder(
        private val binding: ItemBuildingGroupBinding,
        private val events: (event: OverviewAdapterEvent) -> Unit
    ) : OverviewListViewHolder(binding) {

        override fun bind(item: BuildingListItem) {
            binding.data = item as BuildingListItem.GroupItem

            binding.root.setOnClickListener {
                events(OnGroupClick(item.group))
            }
        }
    }

    class BuildingViewHolder(
        private val binding: ItemBuildingSingleBinding,
        private val events: (event: OverviewAdapterEvent) -> Unit
    ) : OverviewListViewHolder(binding) {

        override fun bind(item: BuildingListItem) {
            binding.data = item as BuildingListItem.SingleItem

            binding.btnUpgrade.setOnClickListener {
                events(OnBuildingUpgrade(item))
            }

            binding.root.setOnClickListener {
                events(OnBuildingClick(item.id, item.totalLevels))
            }

            binding.btnUpgrade.setOnLongClickListener {
                events(OnBuildingDowngrade(item))
                return@setOnLongClickListener true
            }
        }
    }
}
