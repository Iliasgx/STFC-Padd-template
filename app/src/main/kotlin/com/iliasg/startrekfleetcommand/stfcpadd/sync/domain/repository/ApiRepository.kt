package com.iliasg.startrekfleetcommand.stfcpadd.sync.domain.repository

import com.iliasg.startrekfleetcommand.stfcpadd.sync.data.datasources.remote.dto.CompareDto.FileMetaDto
import com.iliasg.startrekfleetcommand.stfcpadd.sync.data.datasources.remote.dto.ReleaseDto.ReleaseAsset
import com.iliasg.startrekfleetcommand.stfcpadd.sync.domain.models.Tag
import okhttp3.ResponseBody

interface ApiRepository {

    suspend fun getLatestTag(): Result<Tag>

    suspend fun getLatestReleaseAssets(): Result<List<ReleaseAsset>>

    suspend fun getUpdates(savedSha: String, latestSha: String): Result<List<FileMetaDto>>

    suspend fun downloadLatestDbVersion(version: String): Result<ResponseBody>

    suspend fun downloadAsset(url: String): Result<ResponseBody>
}