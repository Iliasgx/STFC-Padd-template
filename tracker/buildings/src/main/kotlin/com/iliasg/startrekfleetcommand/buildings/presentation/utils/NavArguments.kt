package com.iliasg.startrekfleetcommand.buildings.presentation.utils

import android.os.Bundle

private const val key_buildingId = "buildingId"
internal var Bundle.buildingId: Long
    get() = getLong(key_buildingId, -1L)
    set(value) = putLong(key_buildingId, value)

private const val key_buildingLevel = "level"
internal var Bundle.buildingLevel: Int
    get() = getInt(key_buildingLevel, -1)
    set(value) = putInt(key_buildingLevel, value)

private const val key_buildingLevelCount = "level_count"
internal var Bundle.buildingLevelCount: Int
    get() = getInt(key_buildingLevelCount)
    set(value) = putInt(key_buildingLevelCount, value)