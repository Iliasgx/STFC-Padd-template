package com.iliasg.startrekfleetcommand.buildings.domain.usecases

data class UseCaseInteractor(
    val getAndGroupBuildings: GetAndGroupBuildings,
    val upgradeBuilding: UpgradeBuilding,
    val downgradeBuilding: DowngradeBuilding
)
