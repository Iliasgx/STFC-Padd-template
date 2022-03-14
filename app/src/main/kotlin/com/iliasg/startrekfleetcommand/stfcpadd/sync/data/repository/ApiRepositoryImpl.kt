package com.iliasg.startrekfleetcommand.stfcpadd.sync.data.repository

import com.iliasg.startrekfleetcommand.stfcpadd.sync.data.datasources.remote.GitHubApi
import com.iliasg.startrekfleetcommand.stfcpadd.sync.data.datasources.remote.dto.CompareDto.FileMetaDto
import com.iliasg.startrekfleetcommand.stfcpadd.sync.data.datasources.remote.dto.ReleaseDto.ReleaseAsset
import com.iliasg.startrekfleetcommand.stfcpadd.sync.domain.models.Tag
import com.iliasg.startrekfleetcommand.stfcpadd.sync.domain.repository.ApiRepository
import okhttp3.ResponseBody
import timber.log.Timber
import java.io.IOException
import javax.inject.Inject

class ApiRepositoryImpl @Inject constructor(
    private val api: GitHubApi
) : ApiRepository {

    override suspend fun getLatestTag(): Result<Tag> {
        return try {
            val tag = api.getLatestTags().first()

            Result.success(
                Tag(
                    version = tag.name,
                    lastSha = tag.commit.sha
                )
            )
        } catch (e: Exception) {
            Timber.e(e, "getLatestTag: failed to request the latest tag.")
            Result.failure(e)
        }
    }

    override suspend fun getLatestReleaseAssets(): Result<List<ReleaseAsset>> {
        return try {
            val release = api.getLatestRelease()

            Result.success(release.assets)
        } catch (e: Exception) {
            Timber.e(e, "getLatestReleaseAssets: failed to request all release assets")
            Result.failure(e)
        }
    }

    override suspend fun getUpdates(
        savedSha: String,
        latestSha: String,
    ): Result<List<FileMetaDto>> {
        return try {
            val compare = api.compareChanges(savedSha, latestSha)

            Result.success(compare.files)
        } catch (e: Exception) {
            Timber.e(e, "getUpdates: failed to request a comparison between commits.")
            Result.failure(e)
        }
    }

    override suspend fun downloadLatestDbVersion(version: String): Result<ResponseBody> {
        @Suppress("BlockingMethodInNonBlockingContext")
        val response = try {
            api.getLatestArchivedDbVersion(version).execute()
        } catch (e: IOException) {
            Timber.e(e, "Failed to request the latest db version.")
            return Result.failure(e)
        } catch (e: RuntimeException) {
            Timber.e(e, "An error occurred while creating the request or decoding it.")
            return Result.failure(e)
        }

        if (!response.isSuccessful || response.body() == null) {
            Timber.e("Request was unsuccessful - Code: ${response.code()}")
            return Result.failure(IOException("Request unsuccessful"))
        }

        return Result.success(response.body()!!)
    }

    override suspend fun downloadAsset(url: String): Result<ResponseBody> {
        @Suppress("BlockingMethodInNonBlockingContext")
        val response = try {
            api.getVersionAsset(url).execute()
        } catch (e: IOException) {
            Timber.e(e, "Failed to request the asset. URL: $url")
            return Result.failure(e)
        } catch (e: RuntimeException) {
            Timber.e(e, "An error occurred while creating the request or decoding it.")
            return Result.failure(e)
        }

        if (!response.isSuccessful || response.body() == null) {
            Timber.e("Request was unsuccessful - Code: ${response.code()}")
            return Result.failure(IOException("Request unsuccessful"))
        }

        return Result.success(response.body()!!)
    }
}