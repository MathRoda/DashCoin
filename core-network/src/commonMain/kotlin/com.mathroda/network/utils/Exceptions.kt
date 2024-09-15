package com.mathroda.network.utils

enum class DashCoinError {
    ServiceUnavailable,
    ClientError,
    ServerError,
    UnknownError
}

class DashCoinException(error: DashCoinError): Exception(
    "Something goes wrong: $error"
)