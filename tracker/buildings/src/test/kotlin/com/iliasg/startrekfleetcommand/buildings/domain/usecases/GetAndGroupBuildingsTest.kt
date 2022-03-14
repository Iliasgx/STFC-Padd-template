package com.iliasg.startrekfleetcommand.buildings.domain.usecases

import com.google.common.truth.Truth.assertThat
import com.iliasg.startrekfleetcommand.buildings.data.datasources.local.entity.BuildingEntity
import com.iliasg.startrekfleetcommand.buildings.data.datasources.local.entity.BuildingLevelEntity
import com.iliasg.startrekfleetcommand.buildings.data.repository.FakeBuildingRepositoryImpl
import com.iliasg.startrekfleetcommand.buildings.domain.models.BuildingGroup
import com.iliasg.startrekfleetcommand.buildings.domain.models.BuildingListItem
import com.iliasg.startrekfleetcommand.buildings.domain.repository.BuildingRepository
import com.iliasg.startrekfleetcommand.core.data.datasources.preferences.LocalPreferences
import com.iliasg.startrekfleetcommand.core.domain.repository.UserRepository
import com.iliasg.startrekfleetcommand.core_test.data.repository.FakeUserRepositoryImpl
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Test

@ExperimentalStdlibApi
class GetAndGroupBuildingsTest {

    private lateinit var buildingRepo: BuildingRepository
    private lateinit var userRepo: UserRepository
    private lateinit var getAndGroupBuildings: GetAndGroupBuildings

    @Before
    fun setUp() {
        buildingRepo = FakeBuildingRepositoryImpl()
        userRepo = FakeUserRepositoryImpl()
        getAndGroupBuildings = GetAndGroupBuildings(buildingRepo, userRepo)
    }

    @Test
    fun test_all_buildingsOfSameGroup_mappedToSingleItem() = runBlocking {
        LongRange(1, 5).forEach {
            val building = BuildingEntity(
                group = BuildingGroup.DEFENSE_PLATFORM.ordinal,
                id = it, name = "", description = "", startLevel = 0, bonusHeaders = null
            )
            buildingRepo.insertBuildingWithLevels(building, emptyList())
        }

        getAndGroupBuildings().collectLatest {
            assertThat(it.size).isEqualTo(1)

            val firstItem = it.first() as BuildingListItem.GroupItem
            assertThat(firstItem.group).isEqualTo(BuildingGroup.DEFENSE_PLATFORM)
        }
    }

    @Test
    fun test_all_buildingsOfGroupNone_notMappedToSingleItem() = runBlocking {
        LongRange(1, 3).forEach {
            val building = BuildingEntity(
                group = BuildingGroup.NONE.ordinal,
                id = it, name = "", description = "", startLevel = 0, bonusHeaders = null
            )
            buildingRepo.insertBuildingWithLevels(building, emptyList())
        }

        getAndGroupBuildings().collectLatest {
            assertThat(it.size).isEqualTo(3)

            it.forEach { item ->
                assertThat(item).isInstanceOf(BuildingListItem.SingleItem::class.java)
            }
        }
    }

    @Test
    fun test_all_buildings_orderedByNameOrGroup() = runBlocking {
        val operations = BuildingEntity(
            id = LocalPreferences.PREF_OPERATIONS_KEY,
            group = BuildingGroup.NONE.ordinal,
            name = "", description = "", startLevel = 0, bonusHeaders = null
        )

        val singleBuildings = buildList {
            ('A'..'D').forEachIndexed { idx, letter ->
                add(
                    BuildingEntity(
                        group = BuildingGroup.NONE.ordinal,
                        id = idx.toLong(),
                        name = "Building $letter",
                        description = "", startLevel = 0, bonusHeaders = null
                    )
                )
            }
        }

        val groupBuildings = buildList {
            LongRange(12, 14).forEach {
                add(
                    BuildingEntity(
                        group = BuildingGroup.DRYDOCK.ordinal,
                        id = it, name = "", description = "", startLevel = 0, bonusHeaders = null
                    )
                )
            }
        }

        val expectedOrder = listOf(operations) + singleBuildings + groupBuildings

        expectedOrder.shuffled().forEach {
            buildingRepo.insertBuildingWithLevels(it, emptyList())
        }

        getAndGroupBuildings().collectLatest {
            it.forEachIndexed { index, item ->
                assertThat(item).isEqualTo(expectedOrder[index])
            }
        }
    }

    @Test
    fun test_group_allSortedAlphabetically() = runBlocking {
        LongRange(1, 4).forEach {
            val building = BuildingEntity(
                group = BuildingGroup.DEFENSE_PLATFORM.ordinal,
                id = it, name = "", description = "", startLevel = 0, bonusHeaders = null
            )
            buildingRepo.insertBuildingWithLevels(building, emptyList())
        }

        // Single Drydock instead of Defense Platform.
        val outsiderBuilding = BuildingEntity(
            group = BuildingGroup.DRYDOCK.ordinal,
            id = 99L, name = "", description = "", startLevel = 0, bonusHeaders = null
        )
        buildingRepo.insertBuildingWithLevels(outsiderBuilding, emptyList())

        getAndGroupBuildings(BuildingGroup.DEFENSE_PLATFORM).collectLatest {
            // Shouldn't contain the Drydock building as 5th.
            assertThat(it.size).isEqualTo(4)

            it.map { item ->
                (item as BuildingListItem.SingleItem).name
            }.let { names ->
                assertThat(names).isInOrder()
            }
        }
    }

    @Test
    fun test_singleItem_progressAccurate() = runBlocking {
        val building = BuildingEntity(
            group = BuildingGroup.NONE.ordinal,
            id = 1L, name = "", description = "", startLevel = 0, bonusHeaders = null
        )
        val levels = listOf(
            BuildingLevelEntity(
                buildingId = 1L,
                level = 1,
                requiredOpsLevel = 0,
                power = 0, increasedPower = 0, buildTime = 0, bonusValues = null, requirements = "", buildCosts = "", rewards = null
            ),
            BuildingLevelEntity(
                buildingId = 1L,
                level = 2,
                requiredOpsLevel = 1,
                power = 0, increasedPower = 0, buildTime = 0, bonusValues = null, requirements = "", buildCosts = "", rewards = null
            ),
            BuildingLevelEntity(
                buildingId = 1L,
                level = 3,
                requiredOpsLevel = 1,
                power = 0, increasedPower = 0, buildTime = 0, bonusValues = null, requirements = "", buildCosts = "", rewards = null
            ),
            BuildingLevelEntity(
                buildingId = 3L,
                level = 4,
                requiredOpsLevel = 3,
                power = 0, increasedPower = 0, buildTime = 0, bonusValues = null, requirements = "", buildCosts = "", rewards = null
            )
        )
        buildingRepo.insertBuildingWithLevels(building, levels)

        userRepo.putBuildingLevel(1L, 2)

        val expected = BuildingListItem.SingleItem(
            id = 1L,
            name = "",
            startLevel = 0,
            totalLevels = 4,
            availableLevels = 3,
            currentLevel = 2
        )

        getAndGroupBuildings().collectLatest {
            assertThat(it.first()).isEqualTo(expected)
        }
    }

    @Test
    fun test_groupItem_progressAccurate() = runBlocking {
        val defensePlatformA = BuildingEntity(
            group = BuildingGroup.DEFENSE_PLATFORM.ordinal,
            id = 8L, name = "", description = "", startLevel = 0, bonusHeaders = null
        )
        val defensePlatformB = BuildingEntity(
            group = BuildingGroup.DEFENSE_PLATFORM.ordinal,
            id = 9L, name = "", description = "", startLevel = 0, bonusHeaders = null
        )

        val levelsDefensePlatformA = listOf(
            BuildingLevelEntity(
                buildingId = 8L,
                level = 1,
                requiredOpsLevel = 1,
                power = 0, increasedPower = 0, buildTime = 0, bonusValues = null, requirements = "", buildCosts = "", rewards = null
            ),
            BuildingLevelEntity(
                buildingId = 8L,
                level = 2,
                requiredOpsLevel = 2,
                power = 0, increasedPower = 0, buildTime = 0, bonusValues = null, requirements = "", buildCosts = "", rewards = null
            ),
            BuildingLevelEntity(
                buildingId = 8L,
                level = 3,
                requiredOpsLevel = 3,
                power = 0, increasedPower = 0, buildTime = 0, bonusValues = null, requirements = "", buildCosts = "", rewards = null
            )
        )
        val levelsDefensePlatformB = listOf(
            BuildingLevelEntity(
                buildingId = 9L,
                level = 1,
                requiredOpsLevel = 2,
                power = 0, increasedPower = 0, buildTime = 0, bonusValues = null, requirements = "", buildCosts = "", rewards = null
            ),
            BuildingLevelEntity(
                buildingId = 9L,
                level = 2,
                requiredOpsLevel = 3,
                power = 0, increasedPower = 0, buildTime = 0, bonusValues = null, requirements = "", buildCosts = "", rewards = null
            ),
            BuildingLevelEntity(
                buildingId = 9L,
                level = 3,
                requiredOpsLevel = 4,
                power = 0, increasedPower = 0, buildTime = 0, bonusValues = null, requirements = "", buildCosts = "", rewards = null
            )
        )

        buildingRepo.insertBuildingWithLevels(defensePlatformA, levelsDefensePlatformA)
        buildingRepo.insertBuildingWithLevels(defensePlatformB, levelsDefensePlatformB)
        userRepo.putBuildingLevel(8L, 2)
        userRepo.putBuildingLevel(9L, 1)
        userRepo.putBuildingLevel(LocalPreferences.PREF_OPERATIONS_KEY, 3)

        val expected = BuildingListItem.GroupItem(
            group = BuildingGroup.DEFENSE_PLATFORM,
            availableLevels = 5,
            progress = 3
        )

        getAndGroupBuildings().collectLatest {
            assertThat(it.first()).isEqualTo(expected)
        }
    }
}