package com.mathroda.network.utils

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.HttpRequestBuilder
import io.ktor.client.request.get
import io.ktor.client.statement.HttpResponse
import io.ktor.utils.io.errors.IOException
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.withContext

suspend inline fun <reified T> handleErrors(
    crossinline response: suspend () -> HttpResponse
): T = withContext(Dispatchers.IO) {

    val result = try {
        response()
    } catch(e: IOException) {
        throw DashCoinException(DashCoinError.ServiceUnavailable)
    }

    when(result.status.value) {
        in 200..299 -> Unit
        in 400..499 -> throw DashCoinException(DashCoinError.ClientError)
        500 -> throw DashCoinException(DashCoinError.ServerError)
        else -> throw DashCoinException(DashCoinError.UnknownError)
    }

    return@withContext try {
        result.body()
    } catch(e: Exception) {
        throw DashCoinException(DashCoinError.ServerError)
    }

}