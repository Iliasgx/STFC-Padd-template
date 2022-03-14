package com.iliasg.startrekfleetcommand.buildings.presentation.detailed

import com.iliasg.startrekfleetcommand.buildings.domain.models.Building

internal data class DetailedUiState(
    val building: Building? = null,
    val currentUserLevel: Int = 0
)