package com.mathroda.datasource.platform

import dev.gitlive.firebase.storage.Data
import dev.gitlive.firebase.storage.StorageReference

internal actual suspend fun StorageReference.putByteArray(
    byteArray: ByteArray
) {
    putData(Data(byteArray))
}