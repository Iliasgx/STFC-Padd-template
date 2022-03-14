package com.iliasg.startrekfleetcommand.stfcpadd.sync.data.datasources.remote.dto

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class TagDto(val name: String, val commit: Commit) {
    @JsonClass(generateAdapter = true)
    data class Commit(val sha: String)
}