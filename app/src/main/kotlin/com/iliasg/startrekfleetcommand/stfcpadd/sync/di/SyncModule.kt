package com.iliasg.startrekfleetcommand.stfcpadd.sync.di

import android.content.Context
import com.iliasg.startrekfleetcommand.buildings.domain.repository.BuildingRepository
import com.iliasg.startrekfleetcommand.core.data.converters.ConverterHandler
import com.iliasg.startrekfleetcommand.core.domain.repository.UserRepository
import com.iliasg.startrekfleetcommand.stfcpadd.sync.data.datasources.remote.GitHubApi
import com.iliasg.startrekfleetcommand.stfcpadd.sync.data.repository.ApiRepositoryImpl
import com.iliasg.startrekfleetcommand.stfcpadd.sync.data.repository.SyncPreferenceRepositoryImpl
import com.iliasg.startrekfleetcommand.stfcpadd.sync.domain.repository.ApiRepository
import com.iliasg.startrekfleetcommand.stfcpadd.sync.domain.repository.SyncPreferenceRepository
import com.iliasg.startrekfleetcommand.stfcpadd.sync.domain.usecases.*
import com.squareup.moshi.Moshi
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.create
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object SyncModule {

    @Provides
    @Singleton
    fun provideOkHttpClient(): OkHttpClient {
        return OkHttpClient.Builder()
            .addInterceptor(
                HttpLoggingInterceptor().apply {
                    level = HttpLoggingInterceptor.Level.BODY
                }
            )
            .build()
    }

    @Provides
    @Singleton
    fun provideGitHubApi(client: OkHttpClient, moshi: Moshi): GitHubApi {
        return Retrofit.Builder()
            .addConverterFactory(MoshiConverterFactory.create(moshi))
            .baseUrl(GitHubApi.BASE_URL)
            .client(client)
            .build()
            .create()
    }

    @Provides
    @Singleton
    fun provideApiRepository(api: GitHubApi): ApiRepository {
        return ApiRepositoryImpl(api)
    }

    @Provides
    @Singleton
    fun provideSyncPreferenceRepository(@ApplicationContext context: Context): SyncPreferenceRepository {
        return SyncPreferenceRepositoryImpl(context)
    }

    @Provides
    @Singleton
    fun provideDirectoryHandler(
        buildingRepo: BuildingRepository
    ): DirectoryHandler {
        return DirectoryHandler(
            buildingRepo = buildingRepo
        )
    }

    @ExperimentalStdlibApi
    @Provides
    @Singleton
    fun provideUseCaseInteractor(
        moshi: Moshi,
        apiRepo: ApiRepository,
        userRepo: UserRepository,
        syncRepo: SyncPreferenceRepository,
        directoryHandler: DirectoryHandler,
        converterHandler: ConverterHandler
    ): UseCaseInteractor {
        return UseCaseInteractor(
            decodeFiles = DecodeFiles(moshi),
            downloadAsset = DownloadAsset(apiRepo),
            downloadLatestVersion = DownloadLatestVersion(apiRepo),
            getUpdates = GetUpdates(apiRepo),
            getChangedAssets = GetChangedAssets(apiRepo),
            saveData = SaveData(userRepo, directoryHandler, converterHandler),
            updateSync = UpdateSync(syncRepo)
        )
    }

}