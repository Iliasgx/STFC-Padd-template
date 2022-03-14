package com.iliasg.startrekfleetcommand.stfcpadd.sync.domain.usecases

import com.iliasg.startrekfleetcommand.stfcpadd.sync.domain.models.Directory
import com.iliasg.startrekfleetcommand.stfcpadd.sync.domain.repository.ApiRepository
import timber.log.Timber
import java.util.zip.ZipInputStream

class DownloadLatestVersion(
    private val apiRepo: ApiRepository
) {

    suspend operator fun invoke(latestVersion: String): Result<Map<Directory, List<ByteArray>>> {
        val response = apiRepo
            .downloadLatestDbVersion(latestVersion)
            .getOrElse { e -> return Result.failure(e) }

        val bytes = response.byteStream().use { stream ->
            ZipInputStream(stream).use { zip ->
                generateSequence { zip.nextEntry }
                    .filter { it.name.endsWith(".json") && it.name.contains('/') }
                    .groupBy(
                        keySelector = {
                            Directory.valueOf(
                                it.name
                                    .substringAfter('/')
                                    .substringBeforeLast('/')
                                    .uppercase()
                            )
                        },
                        valueTransform = { zip.readBytes() }
                    )
            }
        }
        Timber.d("Downloaded ${bytes.size} directories.")
        return Result.success(bytes)
    }
}