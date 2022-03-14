package com.iliasg.startrekfleetcommand.stfcpadd.sync.domain.usecases

import com.iliasg.startrekfleetcommand.buildings.data.datasources.remote.dto.BuildingDto
import com.iliasg.startrekfleetcommand.buildings.data.datasources.remote.mappers.toLocalFormat
import com.iliasg.startrekfleetcommand.core.data.converters.ConverterHandler
import com.iliasg.startrekfleetcommand.core.domain.repository.UserRepository
import com.iliasg.startrekfleetcommand.stfcpadd.sync.di.DirectoryHandler
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import timber.log.Timber

@ExperimentalStdlibApi
class SaveData(
    private val userRepo: UserRepository,
    private val handler: DirectoryHandler,
    private val converterHandler: ConverterHandler
) {

    fun asBuildings(buildings: List<BuildingDto>) {
        CoroutineScope(Dispatchers.IO).launch {
            buildings
                .associate { it.toLocalFormat(converterHandler) }  // Dto to Entity
                .forEach { (building, levels) ->
                    // Insert each building and their levels into the database.
                    handler.buildingRepo.insertBuildingWithLevels(building, levels)
                }

            Timber.d("Saved ${buildings.size} building files.")

            // Get the current levels of each building the user has progressed.
            val levels = userRepo.getAllBuildingLevels()
                .catch { emptyMap<Long, Int>() }
                .first()
            // Return if no levels are cached, to save processing power.
            if (levels.isEmpty()) return@launch

            buildings.forEach { building ->
                // Only relevant if the current buildingLevel is already in cache.
                levels[building.id]?.let { currentLevel ->
                    // If the current saved level is below the initial startLevel, upgrade to startLevel.
                    if (currentLevel < building.start_level) {
                        userRepo.putBuildingLevel(building.id, building.start_level)
                    }
                }
            }
        }
    }
}