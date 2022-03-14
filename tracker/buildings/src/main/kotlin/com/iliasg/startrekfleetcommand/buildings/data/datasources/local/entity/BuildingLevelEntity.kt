package com.iliasg.startrekfleetcommand.buildings.data.datasources.local.entity

import androidx.room.ColumnInfo
import androidx.room.Entity

@Entity(
    tableName = "building_level",
    primaryKeys = ["building_id", "level"]
)
data class BuildingLevelEntity(
    @ColumnInfo(name = "building_id")
    val buildingId: Long,

    @ColumnInfo(name = "level")
    val level: Int,

    @ColumnInfo(name = "power")
    val power: Int,

    @ColumnInfo(name = "increased_power")
    val increasedPower: Int,

    @ColumnInfo(name = "build_time")
    val buildTime: Long,

    @ColumnInfo(name = "bonus_values")
    val bonusValues: String?,

    @ColumnInfo(name = "required_ops_level")
    val requiredOpsLevel: Int,

    @ColumnInfo(name = "requirements")
    val requirements: String,

    @ColumnInfo(name = "build_costs")
    val buildCosts: String,

    @ColumnInfo(name = "rewards")
    val rewards: String?
)
