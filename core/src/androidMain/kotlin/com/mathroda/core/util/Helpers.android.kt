package com.mathroda.core.util

import java.util.UUID

actual fun generateUUID(): String {
    return UUID.randomUUID().toString()
}