@file:OptIn(ExperimentalForeignApi::class)

package com.mathroda.datasource.platform

import dev.gitlive.firebase.storage.Data
import dev.gitlive.firebase.storage.StorageReference
import kotlinx.cinterop.ExperimentalForeignApi
import kotlinx.cinterop.addressOf
import kotlinx.cinterop.usePinned
import platform.Foundation.NSData
import platform.Foundation.create

internal actual suspend fun StorageReference.putByteArray(
    byteArray: ByteArray
) {
    val data = Data(
        data = byteArray.toNSData()
    )
    putData(data)
}

@ExperimentalForeignApi
private fun ByteArray.toNSData(): NSData {
    return this.usePinned { pinnedByteArray ->
        NSData.create(bytes = pinnedByteArray.addressOf(0), length = this.size.toULong())
    }
}