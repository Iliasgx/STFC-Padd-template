package com.iliasg.startrekfleetcommand.buildings.data.datasources.local.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "building")
data class BuildingEntity(
    @PrimaryKey
    @ColumnInfo(name = "id")
    val id: Long,

    @ColumnInfo(name = "name")
    val name: String,

    @ColumnInfo(name = "description")
    val description: String,

    @ColumnInfo(name = "group")
    val group: Int,

    @ColumnInfo(name = "start_level")
    val startLevel: Int,

    @ColumnInfo(name = "bonus_headers")
    val bonusHeaders: String?
)