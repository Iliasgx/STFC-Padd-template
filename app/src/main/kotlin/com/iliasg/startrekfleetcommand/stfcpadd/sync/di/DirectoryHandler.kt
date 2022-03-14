package com.iliasg.startrekfleetcommand.stfcpadd.sync.di

import com.iliasg.startrekfleetcommand.buildings.domain.repository.BuildingRepository

data class DirectoryHandler(
    val buildingRepo: BuildingRepository
)
