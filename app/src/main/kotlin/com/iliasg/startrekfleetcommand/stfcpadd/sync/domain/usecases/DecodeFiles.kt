package com.iliasg.startrekfleetcommand.stfcpadd.sync.domain.usecases

import com.squareup.moshi.JsonDataException
import com.squareup.moshi.Moshi
import com.squareup.moshi.adapter
import timber.log.Timber

class DecodeFiles(val moshi: Moshi) {

    @ExperimentalStdlibApi
    inline operator fun <reified T> invoke(files: List<ByteArray>): Result<List<T>> {
        val jsonAdapter = moshi.adapter<T>()

        // Only keep files that aren't empty.
        return try {
            val results = files.mapNotNull { bytes ->
                jsonAdapter.fromJson(
                    bytes.decodeToString()
                )
            }
            Timber.d("Decoded ${results.size} files.")
            Result.success(results)

        } catch (e: JsonDataException) {
            Timber.e(e, "Requested data doesn't match json format.")
            Result.failure(e)
        }
    }
}