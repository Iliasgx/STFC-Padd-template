package com.iliasg.startrekfleetcommand.buildings.data.datasources.local

import androidx.room.*
import com.iliasg.startrekfleetcommand.buildings.data.datasources.local.entity.BuildingEntity
import com.iliasg.startrekfleetcommand.buildings.data.datasources.local.entity.BuildingLevelEntity
import com.iliasg.startrekfleetcommand.buildings.data.datasources.local.models.BuildingWithLevel
import kotlinx.coroutines.flow.Flow

@Dao
interface BuildingDao {

    @Query("""
        SELECT * FROM building
        INNER JOIN building_level ON id = building_id
        WHERE :group IS NULL OR `group` = :group
    """)
    fun getAllBuildings(group: Int?): Flow<Map<BuildingEntity, List<BuildingLevelEntity>>>

    @Query("SELECT COUNT(*) FROM building_level WHERE building_id = :buildingId")
    suspend fun countLevels(buildingId: Long): Int

    @Transaction
    @Query("""
        SELECT *, (SELECT COUNT(*) FROM building_level WHERE building_id = :buildingId) AS level_count 
        FROM building_level
        WHERE building_id = :buildingId AND level = :level
    """)
    fun getBuildingWithLevel(buildingId: Long, level: Int): Flow<BuildingWithLevel>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertBuildingWithLevels(building: BuildingEntity, levels: List<BuildingLevelEntity>)
}