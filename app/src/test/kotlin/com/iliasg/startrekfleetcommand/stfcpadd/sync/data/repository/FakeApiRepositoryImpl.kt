package com.iliasg.startrekfleetcommand.stfcpadd.sync.data.repository

import com.iliasg.startrekfleetcommand.stfcpadd.sync.data.datasources.remote.dto.CompareDto
import com.iliasg.startrekfleetcommand.stfcpadd.sync.data.datasources.remote.dto.ReleaseDto
import com.iliasg.startrekfleetcommand.stfcpadd.sync.data.datasources.remote.dto.TagDto
import com.iliasg.startrekfleetcommand.stfcpadd.sync.domain.models.Tag
import com.iliasg.startrekfleetcommand.stfcpadd.sync.domain.repository.ApiRepository
import com.squareup.moshi.Moshi
import com.squareup.moshi.adapter
import kotlinx.coroutines.runBlocking
import okhttp3.ResponseBody
import java.io.File

@Suppress("BlockingMethodInNonBlockingContext")
@ExperimentalStdlibApi
class FakeApiRepositoryImpl : ApiRepository {

    private val moshi = Moshi.Builder().build()

    override suspend fun getLatestTag(): Result<Tag> = runBlocking {
        val adapter = moshi.adapter<List<TagDto>>()

        val response = File("src/debug/assets/response_tags.json").inputStream().use { stream ->
            stream.readBytes().decodeToString()
        }

        val tag = adapter.fromJson(response)!!.first()

        return@runBlocking Result.success(
            Tag(
                version = tag.name,
                lastSha = tag.commit.sha
            )
        )
    }

    override suspend fun getLatestReleaseAssets(): Result<List<ReleaseDto.ReleaseAsset>> {
        return runBlocking {
            val adapter = moshi.adapter<ReleaseDto>()

            val response = File("src/debug/assets/response_release.json")
                .inputStream()
                .use { stream ->
                    stream.readBytes().decodeToString()
                }

            val release = adapter.fromJson(response)!!

            return@runBlocking Result.success(release.assets)
        }
    }

    override suspend fun getUpdates(
        savedSha: String,
        latestSha: String
    ): Result<List<CompareDto.FileMetaDto>> = runBlocking {
        val adapter = moshi.adapter<CompareDto>()

        val response = File("src/debug/assets/response_compare.json")
            .inputStream()
            .use { stream ->
                stream.readBytes().decodeToString()
            }

        val compare = adapter.fromJson(response)!!

        return@runBlocking Result.success(compare.files)
    }

    override suspend fun downloadLatestDbVersion(version: String): Result<ResponseBody> {
        TODO("Not yet implemented")
    }

    override suspend fun downloadAsset(url: String): Result<ResponseBody> {
        TODO("Not yet implemented")
    }
}