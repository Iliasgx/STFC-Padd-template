package com.iliasg.startrekfleetcommand.buildings.presentation.overview

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.iliasg.startrekfleetcommand.buildings.domain.models.BuildingGroup
import com.iliasg.startrekfleetcommand.buildings.domain.usecases.UseCaseInteractor
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
internal class OverviewViewModel @Inject constructor(
    private val useCaseInteractor: UseCaseInteractor,
    private val savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val _state = MutableStateFlow(OverviewUiState())
    val state: StateFlow<OverviewUiState> = _state.asStateFlow()

    private var getBuildingsJob: Job? = null

    init {
        // Get the applied filter from savedState to handle process death.
        savedStateHandle.get<BuildingGroup>(STATE_FILTER_GROUP)?.let {
            _state.value = state.value.copy(filterGroup = it)
        }
        getBuildings()
    }

    fun onEvent(event: OverviewEvent) {
        when(event) {
            is OverviewEvent.FilterOnGroup -> {
                savedStateHandle[STATE_FILTER_GROUP] = event.group
                _state.value = state.value.copy(filterGroup = event.group)
                getBuildings()
            }
            is OverviewEvent.RemoveGroupFilter -> {
                savedStateHandle.remove<BuildingGroup>(STATE_FILTER_GROUP)
                _state.value = state.value.copy(filterGroup = null)
                getBuildings()
            }
            is OverviewEvent.DowngradeBuilding -> viewModelScope.launch {
                useCaseInteractor.downgradeBuilding(event.building.id, event.building.currentLevel)
                getBuildings()
            }
        }
    }

    private fun getBuildings() {
        // Cancel the previous job to prevent multiple running the same request.
        getBuildingsJob?.cancel()

        getBuildingsJob = viewModelScope.launch {
            useCaseInteractor.getAndGroupBuildings(state.value.filterGroup).collectLatest { buildings ->
                _state.value = state.value.copy(buildings = buildings)
            }
        }
    }

    companion object {
        private const val STATE_FILTER_GROUP = "selected_group"
    }
}