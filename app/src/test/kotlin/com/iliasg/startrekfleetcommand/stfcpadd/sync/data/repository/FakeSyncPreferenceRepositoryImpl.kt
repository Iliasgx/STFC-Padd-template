package com.iliasg.startrekfleetcommand.stfcpadd.sync.data.repository

import com.iliasg.startrekfleetcommand.stfcpadd.sync.domain.repository.SyncPreferenceRepository

class FakeSyncPreferenceRepositoryImpl : SyncPreferenceRepository {

    private val preferences = mutableMapOf<String, String>()

    override fun getLatestUpdate(): Long? {
        return preferences["update"]?.toLong()
    }

    override fun getLastSha(): String {
        return preferences["sha"]!!
    }

    override fun getLastVersion(): String {
        return preferences["version"]!!
    }

    override suspend fun putLatestUpdate() {
        preferences["update"] = System.currentTimeMillis().toString()
    }

    override suspend fun putLatestSha(sha: String) {
        preferences["sha"] = sha
    }

    override suspend fun putLatestVersion(version: String) {
        preferences["version"] = version
    }
}