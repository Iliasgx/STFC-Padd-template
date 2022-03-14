package com.iliasg.startrekfleetcommand.stfcpadd.sync.domain.usecases

import com.iliasg.startrekfleetcommand.stfcpadd.sync.domain.repository.ApiRepository
import timber.log.Timber
import java.util.zip.ZipInputStream

class DownloadAsset(private val apiRepo: ApiRepository) {

    suspend operator fun invoke(assetUrl: String): Result<List<ByteArray>> {
        val response = apiRepo
            .downloadAsset(assetUrl)
            .getOrElse { e -> return Result.failure(e) }

        val bytes = response.byteStream().use { stream ->
            ZipInputStream(stream).use { zip ->
                generateSequence { zip.nextEntry }
                    .filter { it.name.endsWith(".json") }
                    .map { zip.readBytes() }
                    .toList()
            }
        }
        Timber.d("Downloaded ${bytes.size} files.")
        return Result.success(bytes)
    }
}