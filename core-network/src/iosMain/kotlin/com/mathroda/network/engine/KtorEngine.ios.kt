package com.mathroda.network.engine

import io.ktor.client.engine.HttpClientEngine
import io.ktor.client.engine.darwin.Darwin

actual fun platformEngine(): HttpClientEngine {
    return Darwin.create()
}