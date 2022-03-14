package com.iliasg.startrekfleetcommand.buildings.domain.usecases

import com.iliasg.startrekfleetcommand.buildings.domain.mappers.toGroupItem
import com.iliasg.startrekfleetcommand.buildings.domain.mappers.toSingleItem
import com.iliasg.startrekfleetcommand.buildings.domain.models.BuildingGroup
import com.iliasg.startrekfleetcommand.buildings.domain.models.BuildingListItem
import com.iliasg.startrekfleetcommand.buildings.domain.repository.BuildingRepository
import com.iliasg.startrekfleetcommand.core.data.datasources.preferences.LocalPreferences
import com.iliasg.startrekfleetcommand.core.domain.repository.UserRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine

class GetAndGroupBuildings(
    private val buildingRepository: BuildingRepository,
    private val userRepository: UserRepository
) {

    operator fun invoke(group: BuildingGroup? = null): Flow<List<BuildingListItem>> {
        return buildingRepository
            .getAllBuildings(group)
            .combine(userRepository.getAllBuildingLevels()) { buildings, currentLevels ->
                if (buildings.isEmpty()) return@combine emptyList<BuildingListItem>()

                val currentOpsLevel = currentLevels[LocalPreferences.PREF_OPERATIONS_KEY] ?: 1

                // No group was selected, show all buildings.
                if (group == null) {
                    // Split items by group.
                    val allGroups = buildings.groupBy { it.group }

                    // Split the list into the Operations building and others without a group.
                    // The Operations building always should be shown first, on top.
                    val (ops, singles) = allGroups[BuildingGroup.NONE]!!
                        .map { building ->
                            // Use StartLevel as default when no level has been saved yet.
                            building.toSingleItem(
                                currentOpsLevel = currentOpsLevel,
                                currentLevel = currentLevels[building.id] ?: building.startLevel
                            )
                        }
                        .sortedBy { it.name } // All singles should be sorted by name.
                        .partition { it.id == LocalPreferences.PREF_OPERATIONS_KEY }

                    // Map all groups which is not NONE.
                    val groups = allGroups
                        .filterNot { it.key == BuildingGroup.NONE } // Filter out the singles.
                        .map { (_, items) ->
                            items.toGroupItem(
                                currentOpsLevel = currentOpsLevel,
                                levels = currentLevels
                            )
                        }
                        .sortedBy { it.group.ordinal } // Sort groups by their respective enum type.

                    return@combine ops + groups + singles
                } else {
                    // Show all buildings of a single group.
                    return@combine buildings
                        .map { building ->
                            // Use StartLevel as default when no level has been saved yet.
                            building.toSingleItem(
                                currentOpsLevel = currentOpsLevel,
                                currentLevel = currentLevels[building.id] ?: building.startLevel
                            )
                        }
                        .sortedBy { it.name }
                }
            }
    }
}