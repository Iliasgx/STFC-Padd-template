package com.iliasg.startrekfleetcommand.buildings.domain.usecases

import com.iliasg.startrekfleetcommand.core.domain.repository.UserRepository

class DowngradeBuilding(
    private val userRepository: UserRepository
) {
    suspend operator fun invoke(buildingId: Long, level: Int) {
        userRepository.putBuildingLevel(buildingId, level - 1)

        // TODO: If building is Operations, downgrade all buildings and research based on the new Ops level.
    }
}