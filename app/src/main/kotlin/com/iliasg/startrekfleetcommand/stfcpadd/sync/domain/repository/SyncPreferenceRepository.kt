package com.iliasg.startrekfleetcommand.stfcpadd.sync.domain.repository

interface SyncPreferenceRepository {

    fun getLatestUpdate(): Long?

    fun getLastSha(): String

    fun getLastVersion(): String

    suspend fun putLatestUpdate()

    suspend fun putLatestSha(sha: String)

    suspend fun putLatestVersion(version: String)
}