package com.iliasg.startrekfleetcommand.buildings.presentation.overview.adapter

import com.iliasg.startrekfleetcommand.buildings.domain.models.BuildingGroup
import com.iliasg.startrekfleetcommand.buildings.domain.models.BuildingListItem

internal sealed class OverviewAdapterEvent {
    data class OnBuildingUpgrade(val building: BuildingListItem) : OverviewAdapterEvent()
    data class OnBuildingDowngrade(val building: BuildingListItem.SingleItem) : OverviewAdapterEvent()
    data class OnBuildingClick(val buildingId: Long, val levelCount: Int) : OverviewAdapterEvent()
    data class OnGroupClick(val group: BuildingGroup) : OverviewAdapterEvent()
}
