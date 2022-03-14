package com.iliasg.startrekfleetcommand.stfcpadd.sync.domain.workers

import android.content.Context
import androidx.hilt.work.HiltWorker
import androidx.work.*
import com.iliasg.startrekfleetcommand.stfcpadd.sync.domain.repository.ApiRepository
import com.iliasg.startrekfleetcommand.stfcpadd.sync.domain.repository.SyncPreferenceRepository
import com.iliasg.startrekfleetcommand.stfcpadd.sync.domain.utils.AsyncNotification
import com.iliasg.startrekfleetcommand.stfcpadd.sync.domain.utils.Constants
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import timber.log.Timber
import kotlin.time.DurationUnit
import kotlin.time.toDuration

@HiltWorker
class ConfigureWorker @AssistedInject constructor(
    @Assisted appContext: Context,
    @Assisted workerParams: WorkerParameters,
    private val syncRepo: SyncPreferenceRepository,
    private val apiRepo: ApiRepository
) : CoroutineWorker(appContext, workerParams) {

    override suspend fun doWork(): Result {
        val lastUpdate: Long? = syncRepo.getLatestUpdate()

        var initial = false

        if (lastUpdate == null) {
            initial = true
            Timber.d("No update preference available. Initialize first usage download.")
        } else {
            val timeDiff = System.currentTimeMillis() - lastUpdate
            val minutesSinceLastUpdate = timeDiff.toDuration(DurationUnit.MILLISECONDS).inWholeMinutes

            Timber.d("Minutes since last update check: $minutesSinceLastUpdate.")

            // Only allow to check for an update if the last check was at least 15 minutes ago.
            // This helps preventing to hit the api rate limit.
            if (minutesSinceLastUpdate < 15) return Result.failure()
        }

        val tag = apiRepo.getLatestTag().getOrElse { return Result.failure() }

        // When the usage is not initial, check if there is an update available, otherwise cancel.
        if (!initial && syncRepo.getLastVersion() == tag.version) {
            Timber.d("Version ${tag.version} is up-to-date.")

            syncRepo.putLatestUpdate() // Only allow an update check every 15 minutes.
            return Result.failure()
        }

        return Result.success(
            workDataOf(
                Constants.KEY_INITIAL to initial,
                Constants.KEY_LATEST_VERSION to tag.version,
                Constants.KEY_LATEST_SHA to tag.lastSha,
                Constants.KEY_SAVED_SHA to syncRepo.getLastSha()
            )
        )
    }

    override suspend fun getForegroundInfo(): ForegroundInfo {
        return ForegroundInfo(
            AsyncNotification.NOTIFICATION_ID,
            AsyncNotification.notification(applicationContext, id)
        )
    }

    companion object {
        val create = OneTimeWorkRequestBuilder<ConfigureWorker>()
            .setConstraints(
                Constraints.Builder()
                    .setRequiredNetworkType(NetworkType.UNMETERED)
                    .build()
            )
            .setExpedited(OutOfQuotaPolicy.RUN_AS_NON_EXPEDITED_WORK_REQUEST)
            .build()
    }
}