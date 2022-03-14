package com.iliasg.startrekfleetcommand.stfcpadd.sync.domain.usecases

@ExperimentalStdlibApi
data class UseCaseInteractor(
    val decodeFiles: DecodeFiles,
    val downloadAsset: DownloadAsset,
    val downloadLatestVersion: DownloadLatestVersion,
    val getUpdates: GetUpdates,
    val getChangedAssets: GetChangedAssets,
    val saveData: SaveData,
    val updateSync: UpdateSync
)
