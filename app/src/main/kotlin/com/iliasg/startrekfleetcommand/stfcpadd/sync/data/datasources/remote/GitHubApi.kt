package com.iliasg.startrekfleetcommand.stfcpadd.sync.data.datasources.remote

import com.iliasg.startrekfleetcommand.stfcpadd.sync.data.datasources.remote.dto.CompareDto
import com.iliasg.startrekfleetcommand.stfcpadd.sync.data.datasources.remote.dto.ReleaseDto
import com.iliasg.startrekfleetcommand.stfcpadd.sync.data.datasources.remote.dto.TagDto
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.*

interface GitHubApi {

    @Headers(HEADER)
    @GET("tags?per_page=1")
    suspend fun getLatestTags(): List<TagDto>

    @Headers(HEADER)
    @GET("releases/latest")
    suspend fun getLatestRelease(): ReleaseDto

    @Headers(HEADER)
    @GET("compare/{baseSha}...{latestSha}")
    suspend fun compareChanges(
        @Path("baseSha") lastSavedSha: String,
        @Path("latestSha") latestUpdateSha: String
    ): CompareDto

    @Headers(HEADER_STREAM)
    @GET("https://github.com/Iliasgx/app-data/archive/refs/tags/{version}.zip")
    @Streaming
    fun getLatestArchivedDbVersion(@Path("version") versionTag: String): Call<ResponseBody>

    @Headers(HEADER_STREAM)
    @GET
    @Streaming
    fun getVersionAsset(@Url url: String): Call<ResponseBody>

    companion object {
        private const val HEADER = "Accept: application/vnd.github.v3+json"
        private const val HEADER_STREAM = "Accept: application/octet-stream"
        const val BASE_URL = "https://api.github.com/repos/Iliasgx/app-data/"
    }
}