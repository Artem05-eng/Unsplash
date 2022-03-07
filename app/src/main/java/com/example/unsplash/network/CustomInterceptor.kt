package com.example.unsplash.network

import okhttp3.Interceptor
import okhttp3.Response

class CustomInterceptor : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val originalRequest = chain.request()
        val modifyRequest = originalRequest.newBuilder()
            .addHeader("Authorization", "Bearer ${Networking.accessToken}")
            .build()
        return chain.proceed(modifyRequest)
    }
}