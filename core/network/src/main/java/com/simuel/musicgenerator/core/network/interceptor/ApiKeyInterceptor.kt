package com.simuel.musicgenerator.core.network.interceptor

import com.simuel.musicgenerator.core.network.BuildConfig
import okhttp3.Interceptor
import okhttp3.Response

class ApiKeyInterceptor : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val originalRequest = chain.request()
        val requestWithApiKey = originalRequest.newBuilder()
            .addHeader("API-KEY", BuildConfig.LOUDLY_API_KEY)
            .build()
        
        return chain.proceed(requestWithApiKey)
    }
}