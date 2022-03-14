package com.iliasg.startrekfleetcommand.stfcpadd.sync.data.datasources.remote.dto

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class ReleaseDto(val assets: List<ReleaseAsset>) {

    @JsonClass(generateAdapter = true)
    data class ReleaseAsset(
        val name: String,
        val browser_download_url: String
    )
}
