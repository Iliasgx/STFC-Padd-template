package com.iliasg.startrekfleetcommand.buildings.presentation.overview

import com.iliasg.startrekfleetcommand.buildings.domain.models.BuildingGroup
import com.iliasg.startrekfleetcommand.buildings.domain.models.BuildingListItem

internal data class OverviewUiState(
    val buildings: List<BuildingListItem> = emptyList(),
    val filterGroup: BuildingGroup? = null
)
