package com.iliasg.startrekfleetcommand.buildings.di

import android.content.Context
import androidx.room.Room
import com.iliasg.startrekfleetcommand.buildings.data.datasources.local.BuildingDatabase
import com.iliasg.startrekfleetcommand.buildings.data.repository.BuildingRepositoryImpl
import com.iliasg.startrekfleetcommand.buildings.domain.repository.BuildingRepository
import com.iliasg.startrekfleetcommand.buildings.domain.usecases.DowngradeBuilding
import com.iliasg.startrekfleetcommand.buildings.domain.usecases.GetAndGroupBuildings
import com.iliasg.startrekfleetcommand.buildings.domain.usecases.UpgradeBuilding
import com.iliasg.startrekfleetcommand.buildings.domain.usecases.UseCaseInteractor
import com.iliasg.startrekfleetcommand.core.data.converters.ConverterHandler
import com.iliasg.startrekfleetcommand.core.data.datasources.local.Constants
import com.iliasg.startrekfleetcommand.core.domain.repository.UserRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
@ExperimentalStdlibApi
object BuildingsModule {

    @Provides
    @Singleton
    fun provideLocalDatabase(@ApplicationContext context: Context): BuildingDatabase {
        return Room.databaseBuilder(
            context,
            BuildingDatabase::class.java,
            Constants.localDatabaseId
        ).build()
    }

    @Provides
    @Singleton
    fun provideBuildingRepository(
        db: BuildingDatabase,
        converterHandler: ConverterHandler
    ): BuildingRepository {
        return BuildingRepositoryImpl(
            dao = db.buildingDao,
            converterHandler = converterHandler
        )
    }

    @Provides
    @Singleton
    fun provideInteractor(
        buildingRepository: BuildingRepository,
        userRepository: UserRepository
    ) : UseCaseInteractor {
        return UseCaseInteractor(
            getAndGroupBuildings = GetAndGroupBuildings(buildingRepository, userRepository),
            upgradeBuilding = UpgradeBuilding(userRepository),
            downgradeBuilding = DowngradeBuilding(userRepository)
        )
    }
}