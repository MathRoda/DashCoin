package com.mathroda.network.interceptor

import android.util.Log
import okhttp3.Interceptor
import okhttp3.Response

internal class OkhttpInterceptor: Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val originalRequest = chain.request()
        val request = originalRequest.newBuilder().url(originalRequest.url).build()
        Log.d("Network", request.toString())
        return chain.proceed(request)
    }
}