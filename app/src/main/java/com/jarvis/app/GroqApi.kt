
package com.jarvis.app.data.api

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitClient {
    private const val BASE_URL = "https://api.groq.com/openai/"   // note the trailing slash
    const val GROQ_API_KEY = "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"

    val instance: GroqService by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(GroqService::class.java)
    }
}
