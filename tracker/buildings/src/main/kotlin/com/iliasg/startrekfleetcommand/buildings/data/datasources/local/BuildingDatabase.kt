package com.iliasg.startrekfleetcommand.buildings.data.datasources.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.iliasg.startrekfleetcommand.buildings.data.datasources.local.entity.BuildingEntity
import com.iliasg.startrekfleetcommand.buildings.data.datasources.local.entity.BuildingLevelEntity

@Database(
    entities = [
        BuildingEntity::class,
        BuildingLevelEntity::class
    ],
    version = 1,
    exportSchema = false
)
abstract class BuildingDatabase : RoomDatabase() {

    abstract val buildingDao: BuildingDao
}