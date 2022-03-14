package com.iliasg.startrekfleetcommand.buildings.presentation.overview

import com.iliasg.startrekfleetcommand.buildings.domain.models.BuildingGroup
import com.iliasg.startrekfleetcommand.buildings.domain.models.BuildingListItem

internal sealed class OverviewEvent {
    data class FilterOnGroup(val group: BuildingGroup) : OverviewEvent()
    data class DowngradeBuilding(val building: BuildingListItem.SingleItem) : OverviewEvent()
    object RemoveGroupFilter : OverviewEvent()
}
