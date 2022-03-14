package com.iliasg.startrekfleetcommand.buildings.presentation.detailed

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.iliasg.startrekfleetcommand.buildings.domain.repository.BuildingRepository
import com.iliasg.startrekfleetcommand.buildings.domain.usecases.UseCaseInteractor
import com.iliasg.startrekfleetcommand.core.domain.repository.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
internal class DetailedViewModel @Inject constructor(
    private val useCaseInteractor: UseCaseInteractor,
    private val buildingRepository: BuildingRepository,
    private val userRepository: UserRepository,
    private val savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val _state = MutableStateFlow(DetailedUiState())
    val state: StateFlow<DetailedUiState> get() = _state

    private var loadLevelJob: Job? = null

    init {
        // Get the buildingId and level from savedState to handle process death.
        (savedStateHandle.get<Long>(STATE_BUILDING_ID) to savedStateHandle.get<Int>(STATE_LEVEL)).let { (buildingId, level) ->
            if (buildingId != null && level != null) {
                loadData(buildingId, level)
            }
        }
    }

    fun onEvent(event: DetailedEvent) {
        state.value.building?.let { building ->
            when(event) {
                DetailedEvent.OnNextClick -> {
                    loadData(building.id, building.levelDetails.level + 1)
                }
                DetailedEvent.OnPreviousClick -> {
                    loadData(building.id, building.levelDetails.level - 1)
                }
                DetailedEvent.OnUpgradeClick -> viewModelScope.launch {
                    state.value.let {
                        if (it.building?.levelDetails?.level == it.currentUserLevel + 1) {
                            useCaseInteractor.upgradeBuilding(building.id, it.currentUserLevel)
                            onEvent(DetailedEvent.OnNextClick)
                        }
                    }
                }
            }
        }
    }

    fun setUp(buildingId: Long, level: Int) {
        // Only allow the setup to be called once.
        if (state.value.building == null) {
            savedStateHandle[STATE_BUILDING_ID] = buildingId

            loadData(buildingId, level)
        }
    }

    private fun loadData(buildingId: Long, level: Int) {
        savedStateHandle[STATE_LEVEL] = level

        // Cancel the previous job if the user presses the previous or next button again before
        // the new data could be loaded, to prevent multiple scopes from running.
        loadLevelJob?.cancel()

        loadLevelJob = viewModelScope.launch {
            buildingRepository
                .getBuildingWithLevel(buildingId, level)
                .combine(userRepository.getBuildingLevel(buildingId)) { building, currentLevel ->
                    // If the building was not upgraded before, use its start level.
                    val current = currentLevel ?: building.startLevel
                    return@combine building to current

                }.collect { (building, currentLevel) ->
                    _state.value = DetailedUiState(building, currentLevel)
                }
        }
    }

    companion object {
        private const val STATE_BUILDING_ID = "building_id"
        private const val STATE_LEVEL = "level"
    }
}