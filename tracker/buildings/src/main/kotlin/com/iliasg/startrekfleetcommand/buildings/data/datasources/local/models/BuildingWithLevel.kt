package com.iliasg.startrekfleetcommand.buildings.data.datasources.local.models

import androidx.room.Embedded
import androidx.room.Relation
import com.iliasg.startrekfleetcommand.buildings.data.datasources.local.entity.BuildingEntity
import com.iliasg.startrekfleetcommand.buildings.data.datasources.local.entity.BuildingLevelEntity

data class BuildingWithLevel(
    @Relation(
        parentColumn = "building_id",
        entityColumn = "id"
    )
    val building: BuildingEntity,
    @Embedded
    val level: BuildingLevelEntity,
    val level_count: Int
)
