package com.iliasg.startrekfleetcommand.stfcpadd.sync.domain.usecases

import com.iliasg.startrekfleetcommand.stfcpadd.sync.domain.models.Directory
import com.iliasg.startrekfleetcommand.stfcpadd.sync.domain.repository.ApiRepository

class GetChangedAssets(
    private val apiRepo: ApiRepository
) {

    suspend operator fun invoke(directories: List<Directory>): Result<Map<Directory, String>> {
        val response = apiRepo
            .getLatestReleaseAssets()
            .getOrElse { e -> return Result.failure(e) }

        val map = response
            .associate { asset ->
                val dir = Directory.valueOf(
                    asset.name.removeSuffix(".zip").uppercase()
                )
                dir to asset.browser_download_url
            }
            .filterKeys { directory ->
                directory in directories
            }

        return Result.success(map)
    }
}