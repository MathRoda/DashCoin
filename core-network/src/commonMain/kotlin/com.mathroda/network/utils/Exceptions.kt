package com.mathroda.network.utils

import io.ktor.client.plugins.HttpRequestTimeoutException
import io.ktor.utils.io.errors.IOException

enum class DashCoinError {
    ServiceUnavailable,
    ClientError,
    ServerError,
    UnknownError
}

class DashCoinException(error: DashCoinError): Exception(
    "Something goes wrong: $error"
)