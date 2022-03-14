package com.iliasg.startrekfleetcommand.buildings.domain.usecases

import com.iliasg.startrekfleetcommand.core.domain.repository.UserRepository

class UpgradeBuilding(
    private val userRepository: UserRepository
) {
    suspend operator fun invoke(buildingId: Long, level: Int) {
        userRepository.putBuildingLevel(buildingId, level + 1)
    }
}