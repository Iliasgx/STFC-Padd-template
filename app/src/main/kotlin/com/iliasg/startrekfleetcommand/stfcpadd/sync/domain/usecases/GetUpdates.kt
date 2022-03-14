package com.iliasg.startrekfleetcommand.stfcpadd.sync.domain.usecases

import com.iliasg.startrekfleetcommand.stfcpadd.sync.domain.models.Directory
import com.iliasg.startrekfleetcommand.stfcpadd.sync.domain.repository.ApiRepository
import timber.log.Timber

class GetUpdates(
    private val apiRepo: ApiRepository
) {

    suspend operator fun invoke(
        savedSha: String,
        latestSha: String
    ) : Result<List<Directory>?> {
        val files = apiRepo
            .getUpdates(savedSha, latestSha)
            .getOrElse { e -> return Result.failure(e) }

        Timber.d("Update contains ${files.size} changed files.")

        // When the update contains 300 or more changed files, return null as the files
        // should not get processed, but rather the full database should be re-downloaded.
        // Current file request limit is 300, but failsafe if changes in the future.
        if (files.size >= 300) return Result.success(null)

        // Get a list of all directories with changed files.
        val directories = files.map {
            Directory.valueOf(
                it.filename.substringBeforeLast('/').uppercase()
            )
        }.distinct()

        Timber.d("Directories changed: (${directories.size}) [${directories.joinToString(", ")}]")
        return Result.success(directories)
    }
}