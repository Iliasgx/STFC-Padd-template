package com.iliasg.startrekfleetcommand.stfcpadd.sync.domain.workers

import android.content.Context
import androidx.hilt.work.HiltWorker
import androidx.work.*
import com.iliasg.startrekfleetcommand.buildings.data.datasources.remote.dto.BuildingDto
import com.iliasg.startrekfleetcommand.stfcpadd.sync.domain.models.Directory
import com.iliasg.startrekfleetcommand.stfcpadd.sync.domain.usecases.UseCaseInteractor
import com.iliasg.startrekfleetcommand.stfcpadd.sync.domain.utils.AsyncNotification
import com.iliasg.startrekfleetcommand.stfcpadd.sync.domain.utils.Constants
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import timber.log.Timber

@ExperimentalStdlibApi
@HiltWorker
class SyncWorker @AssistedInject constructor(
    @Assisted appContext: Context,
    @Assisted workerParams: WorkerParameters,
    private val interactor: UseCaseInteractor
) : CoroutineWorker(appContext, workerParams) {

    override suspend fun doWork(): Result {
        val isInitial = inputData.getBoolean(Constants.KEY_INITIAL, false)
        val latestVersion = inputData.getString(Constants.KEY_LATEST_VERSION)!!
        val latestSha = inputData.getString(Constants.KEY_LATEST_SHA)!!
        val savedSha = inputData.getString(Constants.KEY_SAVED_SHA)!!

        if (isInitial) {
            Timber.d("Initial usage. Download full database version.")
            // Download full database
            return processFiles(
                interactor
                    .downloadLatestVersion(latestVersion)
                    .getOrElse { return Result.failure() }
            )

        } else {
            val updates = interactor
                .getUpdates(savedSha, latestSha)
                .getOrElse { return Result.failure() }

            // If too many changes occurred, re-download full database.
            // Else only download the updated directories.
            return if (updates == null) {
                Timber.d("Too many changes occurred, downloading full database version.")
                processFiles(
                    interactor
                        .downloadLatestVersion(latestVersion)
                        .getOrElse { return Result.failure() }
                )
            } else {
                Timber.d("Downloading assets of changed directories.")

                val assets = interactor
                    .getChangedAssets(updates)
                    .getOrElse { return Result.failure() }

                val files = coroutineScope {
                    assets.mapValues { (_, url) ->
                        async {
                            interactor
                                .downloadAsset(url)
                                .getOrNull()
                        }
                    }
                }

                processFiles(
                    files.mapValues { (_, items) ->
                        items.await() ?: return Result.failure()
                    }
                )
            }
        }
    }

    private suspend fun processFiles(assets: Map<Directory, List<ByteArray>>): Result {
        val latestVersion = inputData.getString(Constants.KEY_LATEST_VERSION)!!
        val latestSha = inputData.getString(Constants.KEY_LATEST_SHA)!!

        assets.forEach { (directory, files) ->
            when(directory) {
                Directory.BUILDINGS -> {
                    interactor
                        .decodeFiles<BuildingDto>(files)
                        .getOrElse { return Result.failure() }
                        .let { items ->
                            interactor.saveData.asBuildings(items)
                        }
                }
            }
        }

        interactor.updateSync(latestVersion, latestSha)
        return Result.success()
    }

    override suspend fun getForegroundInfo(): ForegroundInfo {
        return ForegroundInfo(
            AsyncNotification.NOTIFICATION_ID,
            AsyncNotification.notification(applicationContext, id)
        )
    }

    companion object {
        val create = OneTimeWorkRequestBuilder<SyncWorker>()
            .setExpedited(OutOfQuotaPolicy.RUN_AS_NON_EXPEDITED_WORK_REQUEST)
            .setConstraints(
                Constraints.Builder()
                    .setRequiredNetworkType(NetworkType.UNMETERED)
                    .build()
            )
            .build()
    }
}