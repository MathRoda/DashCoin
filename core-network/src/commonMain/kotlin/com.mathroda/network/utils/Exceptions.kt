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

class MissingAPIKeyException: Exception("Please add your API Key. get yours https://openapi.coinstats.app/login")