package com.iliasg.startrekfleetcommand.core.di

import android.content.Context
import com.iliasg.startrekfleetcommand.core.data.converters.*
import com.iliasg.startrekfleetcommand.core.data.repository.UserRepositoryImpl
import com.iliasg.startrekfleetcommand.core.domain.repository.UserRepository
import com.squareup.moshi.Moshi
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object CoreModule {

    @Provides
    @Singleton
    fun provideMoshi(): Moshi {
        return Moshi.Builder().build()
    }

    @Provides
    @Singleton
    fun provideUserRepository(@ApplicationContext context: Context) : UserRepository {
        return UserRepositoryImpl(context)
    }

    @Provides
    @Singleton
    @ExperimentalStdlibApi
    fun provideConverterHandler(moshi: Moshi): ConverterHandler {
        return ConverterHandler(
            bonusHeaderConverter = BonusHeaderConverter(moshi),
            bonusValuesConverter = BonusValuesConverter(moshi),
            requirementListConverter = RequirementListConverter(moshi),
            itemsConverter = ItemsConverter(moshi)
        )
    }
}