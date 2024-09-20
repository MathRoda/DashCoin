package com.mathroda.datasource.platform

import dev.gitlive.firebase.storage.StorageReference

internal expect suspend fun StorageReference.putByteArray(
    byteArray: ByteArray
)