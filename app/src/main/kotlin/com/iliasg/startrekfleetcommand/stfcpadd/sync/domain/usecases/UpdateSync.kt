package com.iliasg.startrekfleetcommand.stfcpadd.sync.domain.usecases

import com.iliasg.startrekfleetcommand.stfcpadd.sync.domain.repository.SyncPreferenceRepository
import timber.log.Timber

class UpdateSync(
    private val syncRepo: SyncPreferenceRepository
) {

    suspend operator fun invoke(version: String, sha: String) {
        syncRepo.putLatestUpdate()
        syncRepo.putLatestVersion(version)
        syncRepo.putLatestSha(sha)

        Timber.d("Sync preferences updated.")
    }
}