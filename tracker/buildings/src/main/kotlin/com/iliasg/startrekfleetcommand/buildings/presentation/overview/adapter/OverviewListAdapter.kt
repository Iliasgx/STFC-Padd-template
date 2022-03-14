package com.iliasg.startrekfleetcommand.buildings.presentation.overview.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.iliasg.startrekfleetcommand.buildings.databinding.ItemBuildingGroupBinding
import com.iliasg.startrekfleetcommand.buildings.databinding.ItemBuildingSingleBinding
import com.iliasg.startrekfleetcommand.buildings.domain.models.BuildingListItem

internal class OverviewListAdapter(
    private val onEvent: (event: OverviewAdapterEvent) -> Unit
) : RecyclerView.Adapter<OverviewListViewHolder>() {

    private val differ = AsyncListDiffer(this, differCallback)

    fun setItems(items: List<BuildingListItem>) = differ.submitList(items)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OverviewListViewHolder {
        val inflater = LayoutInflater.from(parent.context)

        return when(viewType) {
            VH_GROUP -> {
                OverviewListViewHolder.GroupViewHolder(
                    binding = ItemBuildingGroupBinding.inflate(inflater, parent, false),
                    events = onEvent
                )
            }
            VH_SINGLE -> {
                OverviewListViewHolder.BuildingViewHolder(
                    binding = ItemBuildingSingleBinding.inflate(inflater, parent, false),
                    events = onEvent
                )
            }
            else -> throw ClassCastException("Unknown OverviewListAdapter ViewType of type: $viewType")
        }
    }

    override fun onBindViewHolder(holder: OverviewListViewHolder, position: Int) {
        holder.bind(differ.currentList[position])
    }

    override fun getItemViewType(position: Int): Int {
        return when(differ.currentList[position]) {
            is BuildingListItem.GroupItem -> VH_GROUP
            is BuildingListItem.SingleItem -> VH_SINGLE
        }
    }

    override fun getItemCount(): Int = differ.currentList.size

    companion object {
        private const val VH_GROUP = 1
        private const val VH_SINGLE = 2

        private val differCallback = object : DiffUtil.ItemCallback<BuildingListItem>() {
            override fun areItemsTheSame(
                oldItem: BuildingListItem,
                newItem: BuildingListItem
            ): Boolean {
                return when {
                    oldItem is BuildingListItem.GroupItem
                            && newItem is BuildingListItem.GroupItem -> oldItem.group == newItem.group
                    oldItem is BuildingListItem.SingleItem
                            && newItem is BuildingListItem.SingleItem -> oldItem.id == newItem.id
                    else -> false
                }
            }

            override fun areContentsTheSame(
                oldItem: BuildingListItem,
                newItem: BuildingListItem
            ): Boolean {
                return oldItem == newItem
            }
        }
    }
}