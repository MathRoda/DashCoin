package com.mathroda.network.engine

import com.mathroda.network.interceptor.OkhttpInterceptor
import io.ktor.client.engine.HttpClientEngine
import io.ktor.client.engine.okhttp.OkHttp

actual fun platformEngine():HttpClientEngine {
    return OkHttp.create {
        addInterceptor(OkhttpInterceptor())
    }
}