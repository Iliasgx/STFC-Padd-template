package com.iliasg.startrekfleetcommand.stfcpadd.sync.data.datasources.remote.dto

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class CompareDto(val files: List<FileMetaDto>) {

    @JsonClass(generateAdapter = true)
    data class FileMetaDto(val filename: String)
}